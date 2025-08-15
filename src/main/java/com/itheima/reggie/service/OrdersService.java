package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Orders;

/**
 * @author TwinkleDing
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders 订单信息
     */
    public void submit(Orders orders);
}
