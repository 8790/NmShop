package com.wz8790.nmshop;

import com.wz8790.nmshop.consts.NmShopConsts;
import com.wz8790.nmshop.exception.UserLoginException;
import com.wz8790.nmshop.vo.UserVo;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserLoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserVo userVo = (UserVo) request.getSession().getAttribute(NmShopConsts.CURRENT_USER);
        if (userVo == null) {
            throw new UserLoginException();
        }
        return true;
    }
}
