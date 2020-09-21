package com.wz8790.nmshop.service;

import com.wz8790.nmshop.form.CartAddForm;
import com.wz8790.nmshop.form.CartUpdateForm;
import com.wz8790.nmshop.pojo.Cart;
import com.wz8790.nmshop.vo.CartVo;
import com.wz8790.nmshop.vo.ResponseVo;

import java.util.List;

public interface ICartService {

    ResponseVo<CartVo> list(Integer uid);

    ResponseVo<CartVo> add(Integer uid, CartAddForm form);

    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);

    ResponseVo<CartVo> delete(Integer uid, Integer productId);

    ResponseVo<CartVo> selectAll(Integer uid);

    ResponseVo<CartVo> unSelectAll(Integer uid);

    ResponseVo<Integer> sum(Integer uid);

    List<Cart> listForCart(Integer uid);

}
