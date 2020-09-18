package com.wz8790.nmshop.service.impl;

import com.google.gson.Gson;
import com.wz8790.nmshop.dao.ProductMapper;
import com.wz8790.nmshop.enums.ProductStatusEnum;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.form.CartAddForm;
import com.wz8790.nmshop.form.CartUpdateForm;
import com.wz8790.nmshop.pojo.Cart;
import com.wz8790.nmshop.pojo.Product;
import com.wz8790.nmshop.service.ICartService;
import com.wz8790.nmshop.vo.CartProductVo;
import com.wz8790.nmshop.vo.CartVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements ICartService {

    public static final String CART_REDIS_KEY_TEMPLATE = "cart_%d";

    @Resource
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Gson gson;

    @Override
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String, String , String > opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        CartVo cartVo = new CartVo();

        //该用户购物车中所有的商品
        Map<String, String> entries = opsForHash.entries(redisKey);

        //遍历每个商品
        ArrayList<CartProductVo> cartProductVos = new ArrayList<>();

        boolean selectAll = true;
        int cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;

        //TODO: 将数据库访问操作放在for循环外，使用sql中的in操作代替
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            //查出该商品的所有信息
            Product product = productMapper.selectByPrimaryKey(Integer.valueOf(entry.getKey()));
            //查出该商品购物车信息
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            //如果有该商品
            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(
                        product.getId(),
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );

                cartProductVos.add(cartProductVo);
                cartTotalQuantity += cart.getQuantity();
                //只计算选中的商品的总价
                if (cart.getProductSelected()) {
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }

                if (!cart.getProductSelected()) {
                    selectAll = false;
                }

            }
        }

        cartVo.setCartProductVoList(cartProductVos);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setSelectedAll(selectAll);

        return ResponseVo.success(cartVo);
    }

    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {
        //该方法添加购物车数量
        Integer quantity = 1;

        Product product = productMapper.selectByPrimaryKey(form.getProductId());

        //商品是否存在
        if (product == null) {
            return ResponseVo.error(StatusCodeEnum.PRODUCT_NOT_EXIST_ERROR);
        }

        //商品是否在售
        if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())) {
            return ResponseVo.error(StatusCodeEnum.PRODUCT_OFF_SALE_OR_DELETED);
        }

        //商品库存是否充足
        if (product.getStock() < 1) {
            return ResponseVo.error(StatusCodeEnum.PRODUCT_STOCK_ERROR);
        }

        //购物车写入到Redis中
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        String productId = String.valueOf(product.getId());

        //判断购物车中是否有这件商品
        Cart cart;
        if (StringUtils.isEmpty(opsForHash.get(redisKey, productId))) {
            //如果没有 添加该商品
            cart = new Cart(product.getId(), quantity, form.getSelected());
        } else {
            //如果有，数量 + 1
            cart = gson.fromJson(opsForHash.get(redisKey, productId), Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
            cart.setProductSelected(form.getSelected());
        }

        //写入结果到Redis
        opsForHash.put(redisKey, productId, gson.toJson(cart));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {

        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        //判断购物车中是否有这件商品
        String productIdInRedis = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(productIdInRedis)) {
            //如果没有 出错
            return ResponseVo.error(StatusCodeEnum.CART_PRODUCT_NOT_EXIST_ERROR);
        }
        //如果有，修改对应数据
        Cart cart = gson.fromJson(productIdInRedis, Cart.class);
        if (form.getQuantity() != null && form.getQuantity() >= 0) {
            cart.setQuantity(form.getQuantity());
        }
        if (form.getSelected() != null) {
            cart.setProductSelected(form.getSelected());
        }

        //写入结果到Redis
        opsForHash.put(redisKey, String.valueOf(productId), gson.toJson(cart));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        //判断购物车中是否有这件商品
        String productIdInRedis = opsForHash.get(redisKey, String.valueOf(productId));
        if (StringUtils.isEmpty(productIdInRedis)) {
            //如果没有 出错
            return ResponseVo.error(StatusCodeEnum.CART_PRODUCT_NOT_EXIST_ERROR);
        }

        //在Redis中删除
        opsForHash.delete(redisKey, String.valueOf(productId));

        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart)
            );
        }
        return list(uid);
    }

    @Override
    public ResponseVo<CartVo> unSelectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey,
                    String.valueOf(cart.getProductId()),
                    gson.toJson(cart)
            );
        }
        return list(uid);
    }

    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = listForCart(uid).stream().map(Cart::getQuantity).reduce(0, Integer::sum);
        return ResponseVo.success(sum);
    }

    private List<Cart> listForCart(Integer uid) {
        HashOperations<String, String , String > opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        //该用户购物车中所有的商品
        Map<String, String> entries = opsForHash.entries(redisKey);
        ArrayList<Cart> carts = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            carts.add(gson.fromJson(entry.getValue(), Cart.class));
        }

        return carts;
    }
}
