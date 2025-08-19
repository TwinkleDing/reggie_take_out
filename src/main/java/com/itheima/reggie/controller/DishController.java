package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TwinkleDing
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品
     *
     * @param dishDto 菜品
     * @return 新增结果
     */
    @PostMapping
    public Request<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return Request.success("新增菜品成功成功！");
    }

    /**
     * 获取菜品列表
     *
     * @param page     页码
     * @param pageSize 页数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Request<Page<DishDto>> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> query = new LambdaQueryWrapper<>();

        query.like(name != null, Dish::getName, name);
        query.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo, query);

        Page<DishDto> dishDtoPage = new Page<>();
        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);
        return Request.success(dishDtoPage);
    }

    /**
     * 根据菜品id查询菜品信息
     *
     * @param id id
     * @return 菜品信息
     */
    @GetMapping("/{id}")
    public Request<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return Request.success(dishDto);
    }

    /**
     * 修改菜品信息
     *
     * @param dishDto 菜品信息
     * @return 修改结果
     */
    @PutMapping
    public Request<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return Request.success("新增菜品成功成功！");
    }

    /**
     * 根据条件查询对应的菜品数据
     *
     * @param dish 菜品信息
     * @return
     */
    @GetMapping("/list")
    public Request<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            // 当前菜品Id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, dishId);
            // SQL: select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavorList);

            return dishDto;
        }).collect(Collectors.toList());

        return Request.success(dishDtoList);
    }

    /*
    @GetMapping("/list")
    public Request<List<Dish>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);
        return Request.success(list);
    }
    *
     */

    /**
     * 修改菜品状态
     *
     * @param ids id
     * @return 修改成功
     */
    @PostMapping("/status/{status}")
    public Request<String> changeStatus(@RequestParam List<Long> ids, @PathVariable Integer status) {
        dishService.updateStatus(ids, status);
        if (status == 0) {
            return Request.success("停售成功！");
        } else {
            return Request.success("起售成功！");
        }
    };

}
