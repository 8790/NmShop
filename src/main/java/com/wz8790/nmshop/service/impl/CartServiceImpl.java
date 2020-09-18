package com.wz8790.nmshop.service.impl;

import com.google.gson.Gson;
import com.wz8790.nmshop.dao.ProductMapper;
import com.wz8790.nmshop.enums.ProductStatusEnum;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.form.CartAddForm;
import com.wz8790.nmshop.pojo.Cart;
import com.wz8790.nmshop.pojo.Product;
import com.wz8790.nmshop.service.ICartService;
import com.wz8790.nmshop.vo.CartVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

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
        }

        //写入结果到Redis
        opsForHash.put(redisKey, productId, gson.toJson(cart));

        return null;
    }
}
