package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.entity.Order;
import com.itheima.reggie.service.OrderService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

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
     * @return 订单列表
     */
    @GetMapping("/page")
    public Request<Page<Order>> getList(int page, int pageSize, Long number) {
        Page<Order> pageInfo = new Page<>(page, pageSize);

        return Request.success(pageInfo);
    }

    /**
     * 用户订单查询
     *
     * @param page     页码
     * @param pageSize 页数
     * @return 订单列表
     */
    @GetMapping("/userPage")
    public Request<Page<Order>> userList(ServletRequest servletRequest, int page, int pageSize) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String userId = (String) request.getSession().getAttribute("user");

        Page<Order> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        queryWrapper.orderByDesc(Order::getOrderTime);
        Page<Order> result = orderService.page(pageInfo, queryWrapper);

        return Request.success(result);
    }
}
