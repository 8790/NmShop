package com.wz8790.nmshop.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wz8790.nmshop.NmShopApplicationTests;
import com.wz8790.nmshop.form.CartAddForm;
import com.wz8790.nmshop.form.CartUpdateForm;
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

    public static final int UID = 1;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() {
        CartAddForm cartAddForm = new CartAddForm();
        cartAddForm.setProductId(26);
        cartAddForm.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(UID, cartAddForm);
        log.info("list={}", gson.toJson(responseVo));
    }

    @Test
    public void list() {
        ResponseVo<CartVo> list = cartService.list(UID);
        log.info("list={}", gson.toJson(list));
    }

    @Test
    public void update() {
        CartUpdateForm form = new CartUpdateForm();
//        form.setQuantity(5);
        form.setSelected(false);
        ResponseVo<CartVo> list = cartService.update(UID, 26, form);
        log.info("list={}", gson.toJson(list));
    }

    @Test
    public void delete() {
        ResponseVo<CartVo> list = cartService.delete(UID, 29);
        log.info("list={}", gson.toJson(list));
    }

    @Test
    public void selectAll() {
        ResponseVo<CartVo> list = cartService.selectAll(UID);
        log.info("list={}", gson.toJson(list));
    }

    @Test
    public void unSelectAll() {
        ResponseVo<CartVo> list = cartService.unSelectAll(UID);
        log.info("list={}", gson.toJson(list));
    }

    @Test
    public void sum() {
        ResponseVo<Integer> sum = cartService.sum(UID);
        log.info("sum={}", gson.toJson(sum));
    }
}