package com.wz8790.nmshop.controller;

import com.github.pagehelper.PageInfo;
import com.wz8790.nmshop.consts.NmShopConsts;
import com.wz8790.nmshop.form.OrderCreateForm;
import com.wz8790.nmshop.pojo.User;
import com.wz8790.nmshop.service.IOrderService;
import com.wz8790.nmshop.vo.OrderVo;
import com.wz8790.nmshop.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm form,
                                      HttpSession session) {

        User user = (User) session.getAttribute(NmShopConsts.CURRENT_USER);
        return orderService.create(user.getId(), form.getShippingId());
    }

    @GetMapping("/orders")
    public ResponseVo<PageInfo> list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                                     HttpSession session) {

        User user = (User) session.getAttribute(NmShopConsts.CURRENT_USER);
        return orderService.list(user.getId(), pageNum, pageSize);
    }

    @GetMapping("/orders/{orderNo}")
    public ResponseVo detail(@PathVariable Long orderNo, HttpSession session) {

        User user = (User) session.getAttribute(NmShopConsts.CURRENT_USER);
        return orderService.detail(user.getId(), orderNo);
    }

    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancel(@PathVariable Long orderNo, HttpSession session) {

        User user = (User) session.getAttribute(NmShopConsts.CURRENT_USER);
        return orderService.cancel(user.getId(), orderNo);
    }
} 