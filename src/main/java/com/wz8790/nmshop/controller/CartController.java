package com.wz8790.nmshop.controller;

import com.wz8790.nmshop.form.CartAddForm;
import com.wz8790.nmshop.vo.CartVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class CartController {

    @PostMapping("/carts")
    public ResponseVo<CartVo> carts(@Valid @RequestBody CartAddForm cartAddForm) {
        return null;
    }
}
