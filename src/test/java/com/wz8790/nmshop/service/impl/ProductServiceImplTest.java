package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.NmShopApplicationTests;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.service.IProductService;
import com.wz8790.nmshop.vo.ProductVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ProductServiceImplTest extends NmShopApplicationTests {

    @Autowired
    private IProductService productService;

    @Test
    public void list() {
        ResponseVo<List<ProductVo>> list = productService.list(null, 1, 10);
        ResponseVo<List<ProductVo>> list2 = productService.list(100002, 1, 10);
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), list.getStatus());
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), list2.getStatus());
    }
}