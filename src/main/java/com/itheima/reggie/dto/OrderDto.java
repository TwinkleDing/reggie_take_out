package com.itheima.reggie.dto;

import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TwinkleDing
 */
public class OrderDto extends Orders {
    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    private List<OrderDetail> orderDetails = new ArrayList<>();
}
