package com.wz8790.nmshop.controller;

import com.wz8790.nmshop.form.UserForm;
import com.wz8790.nmshop.pojo.User;
import com.wz8790.nmshop.service.IUserService;
import com.wz8790.nmshop.vo.ResponseVo;
import com.wz8790.nmshop.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/user/register")
    public ResponseVo<UserVo> register(@RequestBody UserForm userForm) {

        User user = new User();
        BeanUtils.copyProperties(userForm, user);
        return userService.register(user);
    }
}
