package com.wz8790.nmshop.controller;

import com.wz8790.nmshop.form.UserLoginForm;
import com.wz8790.nmshop.form.UserRegisterForm;
import com.wz8790.nmshop.pojo.User;
import com.wz8790.nmshop.service.IUserService;
import com.wz8790.nmshop.vo.ResponseVo;
import com.wz8790.nmshop.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.wz8790.nmshop.consts.NmShopConsts.CURRENT_USER;

@Slf4j
@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/user/register")
    public ResponseVo<UserVo> register(@Valid @RequestBody UserRegisterForm userRegisterForm) {

        log.info("/user/register -------> {}", userRegisterForm.getUsername());

        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        return userService.register(user);
    }

    @PostMapping("/user/login")
    public ResponseVo<UserVo> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                    HttpSession httpSession) {

        log.info("/user/login -------> {}", userLoginForm.getUsername());

        ResponseVo<UserVo> login = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
        httpSession.setAttribute(CURRENT_USER, login.getData());

        return login;
    }

    @GetMapping("/user")
    public ResponseVo<UserVo> userInfo(HttpSession session) {

        log.info("/user -------> ");

        UserVo userVo = (UserVo) session.getAttribute(CURRENT_USER);

        return ResponseVo.success(userVo);
    }

    @GetMapping("/user/logout")
    public ResponseVo<UserVo> logout(HttpSession session) {

        log.info("/user/logout -------> ");

        session.removeAttribute(CURRENT_USER);

        return ResponseVo.success();
    }
}
