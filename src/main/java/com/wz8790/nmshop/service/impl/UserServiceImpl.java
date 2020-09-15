package com.wz8790.nmshop.service.impl;

import com.wz8790.nmshop.dao.UserMapper;
import com.wz8790.nmshop.enums.RoleEnum;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.pojo.User;
import com.wz8790.nmshop.service.IUserService;
import com.wz8790.nmshop.vo.ResponseVo;
import com.wz8790.nmshop.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseVo<UserVo> register(User user) {

        //用户是否存在
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0) {
            return ResponseVo.error(StatusCodeEnum.EXIST_USERNAME_ERROR);
        }

        //密码是否存在
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0) {
            return ResponseVo.error(StatusCodeEnum.EXIST_EMAIL_ERROR);
        }

        //对密码进行MD5加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));

        user.setRole(RoleEnum.CUSTOMER.getCode());

        //写入数据库
        int code = userMapper.insertSelective(user);
        if (code == 0) {
            throw new RuntimeException("注册失败！");
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<UserVo> login(String username, String password) {
        User user = userMapper.selectByUsername(username);

        //判断用户是否存在
        if (user == null) {
            return ResponseVo.error(StatusCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        //判断密码是否正确
        if (!user.getPassword().equalsIgnoreCase(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)))) {
            return ResponseVo.error(StatusCodeEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return ResponseVo.success(userVo);
    }


}