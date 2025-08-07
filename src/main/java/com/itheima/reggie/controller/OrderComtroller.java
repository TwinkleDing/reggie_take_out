package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.entity.Order;
import com.itheima.reggie.service.OrderService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单管理
 *
 * @author TwinkleDing
 */
@RestController
@RequestMapping("/order")
public class OrderComtroller {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private OrderService orderService;

    /**
     * 订单分页查询
     *
     * @param page     页码
     * @param pageSize 页数
     * @param number   订单号
     * @return
     */
    @GetMapping("/page")
    public Request<Page<Order>> getList(int page, int pageSize, Long number) {
        Page<Order> pageInfo = new Page<>(page, pageSize);

        return Request.success(pageInfo);
    }
}
