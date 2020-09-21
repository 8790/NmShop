package com.wz8790.nmshop.service;

import com.wz8790.nmshop.vo.OrderVo;
import com.wz8790.nmshop.vo.ResponseVo;

public interface IOrderService {

    ResponseVo<OrderVo> create(Integer uid, Integer shippingId);
}
