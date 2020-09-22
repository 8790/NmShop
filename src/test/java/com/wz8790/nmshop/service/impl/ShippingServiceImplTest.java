package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.NmShopApplicationTests;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.form.ShippingForm;
import com.wz8790.nmshop.service.IShippingService;
import com.wz8790.nmshop.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Transactional
public class ShippingServiceImplTest extends NmShopApplicationTests {

    @Autowired
    private IShippingService shippingService;

    private Integer uid = 1;

    private ShippingForm form;

    private Integer shippingId;

    @Before
    public void before() {
        ShippingForm form = new ShippingForm();
        form.setReceiverName("张三");
        form.setReceiverAddress("天津师范大学");
        form.setReceiverCity("天津市");
        form.setReceiverMobile("13233334444");
        form.setReceiverProvince("天津");
        form.setReceiverZip("300380");
        form.setReceiverDistrict("西青区");
        this.form = form;
        add();
    }

    public void add() {
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid, form);
        log.info("response = {}", responseVo);
        this.shippingId = responseVo.getData().get("shippingId");
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @After
    public void delete() {
        ResponseVo responseVo = shippingService.delete(uid, shippingId);
        log.info("response = {}", responseVo);
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void update() {
        form.setReceiverAddress("天津理工大学");
        ResponseVo responseVo = shippingService.update(uid, shippingId, form);
        log.info("response = {}", responseVo);
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo responseVo = shippingService.list(uid, 1, 10);
        log.info("response = {}", responseVo);
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}