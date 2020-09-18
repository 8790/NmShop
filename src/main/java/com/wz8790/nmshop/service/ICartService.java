package com.wz8790.nmshop.service;

import com.wz8790.nmshop.form.CartAddForm;
import com.wz8790.nmshop.vo.CartVo;
import com.wz8790.nmshop.vo.ResponseVo;

public interface ICartService {

    ResponseVo<CartVo> add(Integer uid, CartAddForm form);
}
