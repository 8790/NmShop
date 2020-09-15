package com.wz8790.nmshop.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wz8790.nmshop.enums.StatusCodeEnum;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVo<T> {

    private Integer status;

    private String msg;

    private T data;

    public ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    public static <T> ResponseVo<T> success() {
        return new ResponseVo<>(StatusCodeEnum.SUCCESS.getCode(), StatusCodeEnum.SUCCESS.getDesc());
    }

    public static <T> ResponseVo<T> success(T data) {
        return new ResponseVo<>(StatusCodeEnum.SUCCESS.getCode(), data);
    }


        public static <T> ResponseVo<T> error(StatusCodeEnum statusCodeEnum) {
        return new ResponseVo<>(statusCodeEnum.getCode(), statusCodeEnum.getDesc());
    }

    public static <T> ResponseVo<T> error(StatusCodeEnum statusCodeEnum, String msg) {
        return new ResponseVo<>(statusCodeEnum.getCode(), msg);
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
