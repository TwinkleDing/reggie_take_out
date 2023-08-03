package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 *
 * @author TwinkleDing
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增套餐
     *
     * @param setmealDto 套餐
     * @return 新增结果
     */
    @PostMapping
    public Request<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("asdasd", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return Request.success("新增套餐成功！");
    }

    /**
     * 套餐分页查询
     *
     * @param page     页码
     * @param pageSize 页数
     * @param name     套餐名称
     * @return 套餐列表
     */
    @GetMapping("/page")
    public Request<Page<SetmealDto>> getList(int page, int pageSize, String name) {
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return Request.success(dtoPage);
    }

    /**
     * 删除套餐
     *
     * @param ids 套餐id
     * @return 删除结果
     */
    @DeleteMapping
    public Request<String> delete(@RequestParam List<Long> ids) {
        setmealService.removeWithDish(ids);
        return Request.success("删除成功！");
    }

    @PostMapping("/status/{status}")
    public Request<String> status(@PathVariable int status, @RequestParam List<Long> ids) {
        setmealService.updateStatus(ids, status);
        if (status == 0) {
            return Request.success("停售成功！");
        } else {
            return Request.success("起售成功！");
        }
    }

    @GetMapping("/{id}")
    public Request<SetmealDto> get(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getWithDish(id);
        return Request.success(setmealDto);
    }

    @PutMapping
    public Request<String> upload(@RequestBody SetmealDto setmealDto) {
        setmealService.updateWithDish(setmealDto);
        return Request.success("修改套餐成功！");
    }
}
