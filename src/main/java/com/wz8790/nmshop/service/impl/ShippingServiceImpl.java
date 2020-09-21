package com.wz8790.nmshop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wz8790.nmshop.dao.ShippingMapper;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.form.ShippingForm;
import com.wz8790.nmshop.pojo.Shipping;
import com.wz8790.nmshop.service.IShippingService;
import com.wz8790.nmshop.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShippingServiceImpl implements IShippingService {

    @Resource
    private ShippingMapper shippingMapper;


    @Override
    public ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form, shipping);
        shipping.setUserId(uid);
        int row = shippingMapper.insertSelective(shipping);
        if (row == 0) {
            return ResponseVo.error(StatusCodeEnum.SERVER_ERROR);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("shippingId", shipping.getId());
        return ResponseVo.success(map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        int i = shippingMapper.deleteByIdAndUid(uid, shippingId);
        if (i == 0) {
            return ResponseVo.error(StatusCodeEnum.DELETE_SHIPPING_ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingForm form) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(form, shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int row = shippingMapper.updateByPrimaryKeySelective(shipping);
        if (row == 0) {
            return ResponseVo.error(StatusCodeEnum.SERVER_ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippings = shippingMapper.selectByUid(uid);
        PageInfo pageInfo = new PageInfo(shippings);
        return ResponseVo.success(pageInfo);
    }
}
