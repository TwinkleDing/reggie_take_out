package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

/**
 * @author TwinkleDing
 */
public interface DishService extends IService<Dish> {

    /**
     * 新增菜品，同时插入菜品对应的口味数据，操作dish和dish_flavor两张表
     *
     * @param dishDto 数据
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 根据菜品id查询信息
     *
     * @param id id
     * @return 信息
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 修改菜品信息
     *
     * @param dishDto 信息
     */
    public void updateWithFlavor(DishDto dishDto);
}
