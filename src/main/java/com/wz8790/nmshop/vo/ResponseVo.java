package com.wz8790.nmshop.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVo<T> {

    private Integer code;

    private String msg;

    private T data;

    public ResponseVo(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static <T> ResponseVo<T> success() {
        return new ResponseVo<>(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getDesc());
    }

    public static <T> ResponseVo<T> error(StatusCodeEnum statusCodeEnum) {
        return new ResponseVo<>(statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> ResponseVo<T> error(StatusCodeEnum statusCodeEnum, BindingResult bindingResult) {
        return new ResponseVo<>(
                statusCodeEnum.getCode(),
                statusCodeEnum.getDesc()
                        + ", " + Objects.requireNonNull(bindingResult.getFieldError()).getField()
                        + " " + bindingResult.getFieldError().getDefaultMessage()
        );
    }
}
