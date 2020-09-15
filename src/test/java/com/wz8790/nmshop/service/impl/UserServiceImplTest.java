package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.NmShopApplicationTests;
import com.wz8790.nmshop.enums.RoleEnum;
import com.wz8790.nmshop.pojo.User;
import com.wz8790.nmshop.service.IUserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceImplTest extends NmShopApplicationTests {

    @Autowired
    private IUserService userService;

    @Test
    public void register() {
        User user = new User("小明", "1234", "xm@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }
}