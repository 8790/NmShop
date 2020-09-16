package com.wz8790.nmshop.exception;

import com.wz8790.nmshop.enums.StatusCodeEnum;
import com.wz8790.nmshop.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseVo handle(RuntimeException e) {
        log.info("服务器错误：{}", e.getMessage());
        return ResponseVo.error(StatusCodeEnum.SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle() {
        log.info("用户未登录！");
        return ResponseVo.error(StatusCodeEnum.NEED_LOGIN_ERROR);
    }
}
