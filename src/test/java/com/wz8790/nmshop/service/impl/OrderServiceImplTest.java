package com.wz8790.nmshop.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wz8790.nmshop.NmShopApplicationTests;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.form.CartAddForm;
import com.wz8790.nmshop.service.ICartService;
import com.wz8790.nmshop.service.IOrderService;
import com.wz8790.nmshop.vo.CartVo;
import com.wz8790.nmshop.vo.OrderVo;
import com.wz8790.nmshop.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class OrderServiceImplTest extends NmShopApplicationTests {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Integer uid = 1;

    private Integer shippingId = 5;

    private Integer productId = 27;

    @Before
    public void addCart() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(productId);
        cartAddForm.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(uid, cartAddForm);
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void create() {
        ResponseVo<OrderVo> responseVo = orderService.create(uid, shippingId);
        log.info("response = {}", gson.toJson(responseVo));
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo<PageInfo> list = orderService.list(uid, 1, 10);
        log.info("response = {}", gson.toJson(list));
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), list.getStatus());
    }

}