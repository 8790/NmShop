package com.wz8790.nmshop.service;

import com.github.pagehelper.PageInfo;
import com.wz8790.nmshop.vo.OrderVo;
import com.wz8790.nmshop.vo.ResponseVo;

public interface IOrderService {

    ResponseVo<OrderVo> create(Integer uid, Integer shippingId);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    ResponseVo<OrderVo> detail(Integer uid, Long orderNo);
}
