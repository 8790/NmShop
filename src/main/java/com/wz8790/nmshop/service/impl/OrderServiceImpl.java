package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.dao.OrderItemMapper;
import com.wz8790.nmshop.dao.OrderMapper;
import com.wz8790.nmshop.dao.ProductMapper;
import com.wz8790.nmshop.dao.ShippingMapper;
import com.wz8790.nmshop.enums.OrderStatusEnum;
import com.wz8790.nmshop.enums.PaymentTypeEnum;
import com.wz8790.nmshop.enums.ProductStatusEnum;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.pojo.*;
import com.wz8790.nmshop.service.ICartService;
import com.wz8790.nmshop.service.IOrderService;
import com.wz8790.nmshop.vo.OrderItemVo;
import com.wz8790.nmshop.vo.OrderVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    private ShippingMapper shippingMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Autowired
    private ICartService cartService;


    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        // 1.查出收货地址
        Shipping shipping = shippingMapper.selectByShippingIdAndUid(uid, shippingId);
        if (shipping == null) {
            return ResponseVo.error(StatusCodeEnum.SHIPPING_NOT_EXIST_ERROR);
        }

        // 2.获取购物车并选出选中的商品
        // （校验商品以及库存）
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());

        //  校验购物车有无选中的商品
        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(StatusCodeEnum.CART_SELECT_IS_EMPTY_ERROR);
        }

        //  获取选中的商品
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);

        //  将product构造成map对象方便校验
        //  {
        //      key: productId,
        //      value: product对象
        //  }
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        //  开始校验商品以及库存
        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();

        for (Cart cart : cartList) {
            Product product = map.get(cart.getProductId());
            // 是否有商品
            if (product == null) {
                return ResponseVo.error(StatusCodeEnum.PRODUCT_NOT_EXIST_ERROR);
            }
            //商品是否上架
            if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())) {
                return ResponseVo.error(StatusCodeEnum.PRODUCT_OFF_SALE_OR_DELETED);
            }
            //库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(StatusCodeEnum.PRODUCT_STOCK_ERROR, product.getName() + " 库存不足");
            }
            // 减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0) {
                return ResponseVo.error(StatusCodeEnum.SERVER_ERROR);
            }

            //构造orderItem
            OrderItem item = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(item);

        }

        // 3.构造Order计算总价格（计算的是被选中的商品）
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);

        // 4.生成订单，入库，因为要同时写入两个表，所以要使用事务的方式控制
        int orderRow = orderMapper.insertSelective(order);
        if (orderRow  <= 0) {
            return ResponseVo.error(StatusCodeEnum.SERVER_ERROR);
        }

        int orderItemRow = orderItemMapper.batchInsert(orderItemList);
        if (orderItemRow  <= 0) {
            return ResponseVo.error(StatusCodeEnum.SERVER_ERROR);
        }

        // 5.清除已经下单了的购物车
        // Redis的事务不能回滚，所以放在最后
        for (Cart cart : cartList) {
            cartService.delete(uid, cart.getProductId());
        }

        // 6.构造vo返回给前端
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(orderItemVoList);

        orderVo.setShippingId(shipping.getId());
        orderVo.setShippingVo(shipping);

        return orderVo;
    }


    private Order buildOrder(Integer uid,
                             Long orderNo,
                             Integer shippingId,
                             List<OrderItem> orderItemList) {

        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());

        return order;
    }

    /**
     * 企业级要使用分布式唯一id的方式解决
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() * 1000 + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }

}
