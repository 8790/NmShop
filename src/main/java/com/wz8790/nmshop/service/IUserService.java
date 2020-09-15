package com.wz8790.nmshop.service;

import com.wz8790.nmshop.pojo.User;
import com.wz8790.nmshop.vo.ResponseVo;
import com.wz8790.nmshop.vo.UserVo;

public interface IUserService {


    /**
     * 注册
     */
    ResponseVo<UserVo> register(User user);

    /**
     * 登录
     */
    ResponseVo<UserVo> login(String username, String password);
}
