package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.dto.OrderDto;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrdersService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单管理
 *
 * @author TwinkleDing
 */
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private OrdersService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 订单分页查询
     *
     * @param page     页码
     * @param pageSize 页数
     * @param number   订单号
     * @return 订单列表
     */
    @GetMapping("/page")
    public Request<Page<Orders>> getList(int page, int pageSize, String number) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        if (number != null) {
            queryWrapper.like(Orders::getNumber, number);
        }
        ;
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        queryWrapper.orderByDesc(Orders::getOrderTime);
        List<Orders> orderList = orderService.list(queryWrapper);
        pageInfo.setRecords(orderList);

        return Request.success(pageInfo);
    }

    /**
     * 更新订单状态
     *
     * @param orders 订单
     * @return 订单
     */
    @PutMapping
    public Request<Orders> updateOrders(@RequestBody Orders orders) {
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Orders::getId, orders.getId());
        wrapper.set(Orders::getStatus, orders.getStatus());
        orderService.update(wrapper);
        return Request.success(orders);
    };

    /**
     * 用户订单查询
     *
     * @param page     页码
     * @param pageSize 页数
     * @return 订单列表
     */
    @GetMapping("/userPage")
    public Request<Page<OrderDto>> userList(ServletRequest servletRequest, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        queryWrapper.orderByDesc(Orders::getOrderTime);

        List<Orders> list = orderService.list(queryWrapper);
        List<OrderDto> orderDtoList = list.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);
            LambdaQueryWrapper<OrderDetail> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(wrapper);
            orderDto.setOrderDetails(orderDetails);
            return orderDto;
        }).collect(Collectors.toList());
        // SQL select * from order_detail where user_id = ? and order_id = ?
        Page<OrderDto> pageInfo = new Page<>(page, pageSize);
        pageInfo.setRecords(orderDtoList);

        return Request.success(pageInfo);
    }

    /**
     * 用户下单
     *
     * @param orders 订单信息
     * @return 下单成功
     */
    @PostMapping("/submit")
    public Request<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return Request.success("下单成功！");
    }
}
