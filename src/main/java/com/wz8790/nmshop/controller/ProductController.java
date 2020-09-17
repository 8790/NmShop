package com.wz8790.nmshop.controller;

import com.github.pagehelper.PageInfo;
import com.wz8790.nmshop.service.IProductService;
import com.wz8790.nmshop.vo.ProductVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/products")
    public ResponseVo<PageInfo<ProductVo>> products(@RequestParam(required = false) Integer categoryId,
                                                    @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                                    @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        ResponseVo<PageInfo<ProductVo>> list = productService.list(categoryId, pageNum, pageSize);
        return list;
    }
}
