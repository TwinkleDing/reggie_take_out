package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author TwinkleDing
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Request<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        // 设置用户id，指定当前为哪个用户的数据
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(String.valueOf(currentId));
        // 查询当前菜品或套餐是否已存在在购物车，如果已存在，在数量上+1，不存在，数量设置为1
        Long disId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if (disId != null) {
            // 添加的类型是菜品
            queryWrapper.eq(ShoppingCart::getUserId, currentId);
        } else {
            // 添加的类型是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        // SQL: select * from shopping_cart where user_id = ? and dish_id = ?
        shoppingCartService.getOne(queryWrapper);

        return null;
    }

}
