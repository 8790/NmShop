package com.wz8790.nmshop.controller;

import com.wz8790.nmshop.consts.NmShopConsts;
import com.wz8790.nmshop.form.ShippingForm;
import com.wz8790.nmshop.service.IShippingService;
import com.wz8790.nmshop.vo.ResponseVo;
import com.wz8790.nmshop.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@RestController
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    @PostMapping("/shippings")
    public ResponseVo<Map<String, Integer>> add(@Valid @RequestBody ShippingForm form,
                                                HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return shippingService.add(userVo.getId(), form);
    }

    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId, HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return shippingService.delete(userVo.getId(), shippingId);
    }

    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId,
                             @Valid @RequestBody ShippingForm form,
                             HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return shippingService.update(userVo.getId(), shippingId, form);
    }

    @GetMapping("/shippings")
    public ResponseVo list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(NmShopConsts.CURRENT_USER);
        return shippingService.list(userVo.getId(), pageNum, pageSize);
    }
}
