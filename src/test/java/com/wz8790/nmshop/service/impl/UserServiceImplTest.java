package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.NmShopApplicationTests;
import com.wz8790.nmshop.enums.RoleEnum;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.pojo.User;
import com.wz8790.nmshop.service.IUserService;
import com.wz8790.nmshop.vo.ResponseVo;
import com.wz8790.nmshop.vo.UserVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceImplTest extends NmShopApplicationTests {

    public static final String USERNAME = "小明";
    public static final String PASSWORD = "1234";

    @Autowired
    private IUserService userService;

    @Test
    @Before
    public void register() {
        User user = new User(USERNAME, PASSWORD, "xm@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }

    @Test
    public void login() {
        ResponseVo<UserVo> login = userService.login(USERNAME, PASSWORD);
        Assert.assertEquals(StatusCodeEnum.SUCCESS.getCode(), login.getStatus());
    }
}