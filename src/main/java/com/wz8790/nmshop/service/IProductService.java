package com.wz8790.nmshop.service;

import com.github.pagehelper.PageInfo;
import com.wz8790.nmshop.vo.ProductDetailVo;
import com.wz8790.nmshop.vo.ProductVo;
import com.wz8790.nmshop.vo.ResponseVo;

public interface IProductService {

    ResponseVo<PageInfo<ProductVo>> list(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail(Integer productId);
}
