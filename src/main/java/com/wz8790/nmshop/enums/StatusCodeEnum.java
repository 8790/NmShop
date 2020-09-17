package com.wz8790.nmshop.enums;

import lombok.Getter;

@Getter
public enum  StatusCodeEnum {

    SUCCESS(0, "成功"),

    PASSWORD_ERROR(1, "密码错误"),

    EXIST_USERNAME_ERROR(2, "用户名已存在"),

    PARAM_ERROR(3, "参数错误"),

    EXIST_EMAIL_ERROR(4, "邮箱已存在"),

    NEED_LOGIN_ERROR(10, "用户未登录"),

    USERNAME_OR_PASSWORD_ERROR(11, "用户名或密码错误"),

    PRODUCT_OFF_SALE_OR_DELETED(12, "商品已下架或删除"),

    SERVER_ERROR(-1, "服务端异常"),

    ;

    Integer code;

    String desc ;

    StatusCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
