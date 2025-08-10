package com.itheima.reggie.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @author TwinkleDing
 */
@Data
public class User implements Serializable {

    private Long id;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

    private String avatar;

    private Integer status;
}
