package com.wz8790.nmshop.service.impl;

import com.github.pagehelper.PageInfo;
import com.wz8790.nmshop.NmShopApplicationTests;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.service.IProductService;
import com.wz8790.nmshop.vo.ProductDetailVo;
import com.wz8790.nmshop.vo.ProductVo;
import com.wz8790.nmshop.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ProductServiceImplTest extends NmShopApplicationTests {

    @Autowired
    private IProductService productService;

    @Test
    public void list() {
        ResponseVo<PageInfo<ProductVo>> list = productService.list(null, 1, 2);
        log.info("list1 = {}", list);
        ResponseVo<PageInfo<ProductVo>> list2 = productService.list(100002, 2, 2);
        log.info("list2 = {}", list2);
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), list.getStatus());
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), list2.getStatus());
    }

    @Test
    public void detail() {
        ResponseVo<ProductDetailVo> detail = productService.detail(26);
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), detail.getStatus());
    }
}