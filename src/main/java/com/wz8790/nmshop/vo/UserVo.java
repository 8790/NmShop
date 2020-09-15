package com.wz8790.nmshop.vo;

import lombok.Data;

import java.util.Date;

@Data
public class UserVo {

    private Integer id;

    private String username;

    private String email;

    private String phone;

    private Integer role;

    private Date createTime;

    private Date updateTime;
}
