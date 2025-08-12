package com.itheima.reggie.entity;

import com.itheima.reggie.enums.OrderStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author TwinkleDing
 * 订单
 */
@Data
public class Orders implements Serializable {
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
     * 用户Id
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 联系人
     */
    private String consignee;

    /**
     * 地址ID
     */
    private String addressBookId;

    /**
     * 地址
     */
    private String address;

    /**
     * 下单时间
     */
    private LocalDateTime orderTime;

    private LocalDateTime checkoutTime;

    /**
     * 支付方式
     */
    private Integer payMethod;

    /**
     * 订单金额
     */
    private String amount;

    /**
     * 备注
     */
    private String remark;
}
