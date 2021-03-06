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

    PRODUCT_NOT_EXIST_ERROR(13, "商品不存在"),

    PRODUCT_STOCK_ERROR(14, "商品库存不足"),

    CART_PRODUCT_NOT_EXIST_ERROR(15, "购物车中无此商品"),

    DELETE_SHIPPING_ERROR(16, "删除地址失败"),

    SHIPPING_NOT_EXIST_ERROR(17, "地址不存在"),

    CART_SELECT_IS_EMPTY_ERROR(18, "未选中商品"),

    ORDER_NOT_EXIST_ERROR(19, "订单不存在"),

    ORDER_STATUS_ERROR(20, "订单状态有误"),

    SERVER_ERROR(-1, "服务端异常"),

    ;

    Integer code;

    String desc ;

    StatusCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
