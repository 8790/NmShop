package com.wz8790.nmshop.service;

import com.wz8790.nmshop.vo.ProductVo;
import com.wz8790.nmshop.vo.ResponseVo;

import java.util.List;

public interface IProductService {

    ResponseVo<List<ProductVo>> list(Integer categoryId, Integer pageNum, Integer pageSize);
}
