package com.itheima.reggie.entity;

import com.itheima.reggie.enums.OrderStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author TwinkleDing
 * 订单
 */
@Data
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long number;

    private OrderStatusEnum status;

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    /**
     * 用户名
     */
    private String userName;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 下单时间
     */
    private String orderTime;

    /**
     * 实收金额
     */
    private String amount;

    /**
     * 菜品列表
     */
    private ArrayList<Dish> dishList;

    /**
     * 套餐列表
     */
    private ArrayList<Setmeal> setmeal;

}
