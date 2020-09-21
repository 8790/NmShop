package com.wz8790.nmshop.service;

import com.github.pagehelper.PageInfo;
import com.wz8790.nmshop.form.ShippingForm;
import com.wz8790.nmshop.vo.ResponseVo;

import java.util.Map;

public interface IShippingService {

    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form);

    ResponseVo delete(Integer uid, Integer shippingId);

    ResponseVo update(Integer uid, Integer shippingId, ShippingForm shippingForm);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);
}
