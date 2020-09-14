package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.dao.UserMapper;
import com.wz8790.nmshop.pojo.User;
import com.wz8790.nmshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(User user) {

        //用户名不能为空
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0) {
            throw new RuntimeException("用户已存在！");
        }

        //密码不能为空
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0) {
            throw new RuntimeException("邮箱已存在！");
        }

        //对密码进行MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        //写入数据库
        int code = userMapper.insertSelective(user);
        if (code == 0) {
            throw new RuntimeException("注册失败！");
        }
    }
}
