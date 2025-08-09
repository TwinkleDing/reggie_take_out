package com.itheima.reggie.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class User implements Serializable {

    private Long id;

    private String name;

    private String phone;

    private String set;

    private String idNumber;

    private String avatar;

    private Integer status;
}
