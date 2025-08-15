package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author TwinkleDing
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    private static String TYPE_ADD = "add";
    private static String TYPE_SUB = "sub";

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 获取购物车内容列表
     *
     * @return 购物车列表
     */
    @GetMapping("/list")
    public Request<List<ShoppingCart>> list() {
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        // SQL: select * from shopping_cart where user_id = ?
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return Request.success(list);
    }

    /**
     * 添加购物车
     *
     * @param shoppingCart 添加到购物车的信息
     * @return 添加成功的菜品或套餐
     */
    @PostMapping("/add")
    public Request<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        // 设置用户id，指定当前为哪个用户的数据
        ShoppingCart cartServiceOne = getCartServiceOne(shoppingCart, TYPE_ADD);

        if (cartServiceOne != null) {
            cartServiceOne.setNumber(cartServiceOne.getNumber() + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }

        return Request.success(cartServiceOne);
    }

    /**
     * 删除购物车的指定信息
     *
     * @param shoppingCart 被删除的购物车的信息
     * @return 被删除的购物车的信息
     */
    @PostMapping("/sub")
    public Request<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {

        ShoppingCart cartServiceOne = getCartServiceOne(shoppingCart, TYPE_SUB);

        if (cartServiceOne.getNumber() == 1) {
            shoppingCartService.removeById(cartServiceOne);
            cartServiceOne.setNumber(0);
        } else {
            cartServiceOne.setNumber(cartServiceOne.getNumber() - 1);
            shoppingCartService.updateById(cartServiceOne);
        }

        return Request.success(cartServiceOne);
    }

    /**
     * 根据购物车操作的项返回对应的菜品或者套餐
     *
     * @param shoppingCart 购物车操作的项
     * @return 菜品或者套餐
     */
    private ShoppingCart getCartServiceOne(@RequestBody ShoppingCart shoppingCart, String type) {
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(String.valueOf(currentId));
        Long disId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        if (disId != null) {
            if (Objects.equals(TYPE_ADD, type)) {
                queryWrapper.eq(ShoppingCart::getDishId, disId);
            } else {
                queryWrapper.eq(ShoppingCart::getDishId, disId);
            }
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        return shoppingCartService.getOne(queryWrapper);
    }

    /**
     * 清空购物车
     *
     * @return 清空信息
     */
    @DeleteMapping("/clean")
    private Request<String> cleanShoppingCart() {
        Long currentId = BaseContext.getCurrentId();
        // SQL: select * from shopping_cart where user_id = ?
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        try {
            // 调用Service的remove方法删除所有符合条件的记录
            boolean isRemoved = shoppingCartService.remove(queryWrapper);

            if (isRemoved) {
                return Request.success("清空购物车成功！");
            } else {
                return Request.error("清空购物车失败，可能购物车已为空");
            }
        } catch (Exception e) {
            // 捕获异常并返回错误信息
            return Request.error("清空购物车时发生错误：" + e.getMessage());
        }
    }
}
