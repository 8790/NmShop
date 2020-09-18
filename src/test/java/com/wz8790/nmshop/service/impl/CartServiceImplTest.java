package com.wz8790.nmshop.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wz8790.nmshop.NmShopApplicationTests;
import com.wz8790.nmshop.form.CartAddForm;
import com.wz8790.nmshop.service.ICartService;
import com.wz8790.nmshop.vo.CartVo;
import com.wz8790.nmshop.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

@Slf4j
public class CartServiceImplTest extends NmShopApplicationTests {

    @Autowired
    private ICartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(27);
        cartAddForm.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(1, cartAddForm);
        log.info("list={}", gson.toJson(responseVo));
    }

    @Test
    public void list() {
        ResponseVo<CartVo> list = cartService.list(1);
        log.info("list={}", gson.toJson(list));
    }
}