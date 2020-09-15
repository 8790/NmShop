package com.wz8790.nmshop.controller;

import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.form.UserRegisterForm;
import com.wz8790.nmshop.form.UserLoginForm;
import com.wz8790.nmshop.pojo.User;
import com.wz8790.nmshop.service.IUserService;
import com.wz8790.nmshop.vo.ResponseVo;
import com.wz8790.nmshop.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.wz8790.nmshop.consts.NmShopConsts.CURRENT_USER;

@RestController
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/user/register")
    public ResponseVo<UserVo> register(@Valid @RequestBody UserRegisterForm userRegisterForm,
                                       BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseVo.error(StatusCodeEnum.PARAM_ERROR, bindingResult);
        }

        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        return userService.register(user);
    }

    @PostMapping("/user/login")
    public ResponseVo<UserVo> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                    BindingResult bindingResult,
                                    HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return ResponseVo.error(StatusCodeEnum.PARAM_ERROR, bindingResult);
        }

        ResponseVo<UserVo> login = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
        httpSession.setAttribute(CURRENT_USER, login.getData());

        return login;
    }

    @GetMapping("/user")
    public ResponseVo<UserVo> userInfo(HttpSession session) {
        UserVo userVo = (UserVo) session.getAttribute(CURRENT_USER);

        if (userVo == null) {
            return ResponseVo.error(StatusCodeEnum.NEED_LOGIN_ERROR);
        }

        return ResponseVo.success(userVo);
    }
}
