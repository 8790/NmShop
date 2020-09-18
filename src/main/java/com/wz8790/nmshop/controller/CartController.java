package com.wz8790.nmshop.controller;

import com.wz8790.nmshop.consts.NmShopConsts;
import com.wz8790.nmshop.form.CartAddForm;
import com.wz8790.nmshop.form.CartUpdateForm;
import com.wz8790.nmshop.service.ICartService;
import com.wz8790.nmshop.vo.CartVo;
import com.wz8790.nmshop.vo.ResponseVo;
import com.wz8790.nmshop.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class CartController {

    @Autowired
    private ICartService cartService;


    @GetMapping("/carts")
    public ResponseVo<CartVo> carts(HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return cartService.list(userVo.getId());
    }

    @PostMapping("/carts")
    public ResponseVo<CartVo> carts(@Valid @RequestBody CartAddForm cartAddForm, HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return cartService.add(userVo.getId(), cartAddForm);
    }

    @PutMapping("/carts/{productId}")
    public ResponseVo<CartVo> update(@Valid @RequestBody CartUpdateForm cartUpdateForm,
                                     @PathVariable Integer productId,
                                     HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return cartService.update(userVo.getId(), productId, cartUpdateForm);
    }

    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,
                                     HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return cartService.delete(userVo.getId(), productId);
    }

    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return cartService.selectAll(userVo.getId());
    }

    @PutMapping("/carts/unSelectAll")
    public ResponseVo<CartVo> unSelectAll(HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return cartService.unSelectAll(userVo.getId());
    }

    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return cartService.sum(userVo.getId());
    }
}
