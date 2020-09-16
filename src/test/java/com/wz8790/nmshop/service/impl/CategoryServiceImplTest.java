package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.NmShopApplicationTests;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.service.ICategoryService;
import com.wz8790.nmshop.vo.CategoryVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class CategoryServiceImplTest extends NmShopApplicationTests {

    @Autowired
    private ICategoryService categoryService;

    @Test
    public void categories() {
        ResponseVo<List<CategoryVo>> categories = categoryService.categories();
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), categories.getStatus());
    }
}