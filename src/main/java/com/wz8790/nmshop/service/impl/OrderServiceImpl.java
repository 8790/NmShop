package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.dao.ProductMapper;
import com.wz8790.nmshop.dao.ShippingMapper;
import com.wz8790.nmshop.enums.ProductStatusEnum;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.pojo.Cart;
import com.wz8790.nmshop.pojo.Product;
import com.wz8790.nmshop.pojo.Shipping;
import com.wz8790.nmshop.service.ICartService;
import com.wz8790.nmshop.service.IOrderService;
import com.wz8790.nmshop.vo.OrderVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Resource
    private ShippingMapper shippingMapper;

    @Resource
    private ProductMapper productMapper;

    @Autowired
    private ICartService cartService;


    @Override
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

        }


        // 3.计算总价格（只计算被选中的商品）

        // 4.生成订单，入库，因为要同时写入两个表，所以要使用事务的方式控制

        // 5.减库存

        // 6.清除已经下单了的购物车

        // 7.构造vo返回给前端


        return null;
    }
}
