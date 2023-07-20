package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TwinkleDing
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     *
     * @param setmealDto 套餐信息
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本信息
        this.save(setmealDto);

        List<SetmealDish> setmealDishList = setmealDto.getSetmealDishes();
        setmealDishList.stream().map((item) -> {
            item.setSetmealId((setmealDto.getId()));
            return item;
        }).collect(Collectors.toList());
        // 保存套餐和菜品的关联关系
        setmealDishService.saveBatch(setmealDishList);
    }

    /**
     * 删除套餐，同时删除和套餐和菜品的关联数据
     *
     * @param ids id
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        // 查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(queryWrapper);
        // 如果不能删除，抛出一个业务异常
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，不能删除！");
        }
        // 如果可以删除，先删除套餐表中的数据
        this.removeByIds(ids);
        // 删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId, ids);
        // 删除关系表中的数据---setmeal_dish
        setmealDishService.remove(queryWrapper1);
    }

    /**
     * 修改套餐的销售状态
     *
     * @param ids    id
     * @param status 状态
     */
    @Override
    public void updateStatus(List<Long> ids, int status) {
        for (Long id : ids) {
            Setmeal setmeal = setmealService.getById(id);
            if (!status.equals(setmeal.getStatus())) {
                setmeal.setStatus(status);
                setmealService.updateById(setmeal);
            }
        }
    }
}
