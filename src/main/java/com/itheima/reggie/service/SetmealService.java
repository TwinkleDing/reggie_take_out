package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author TwinkleDing
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     *
     * @param setmealDto 套餐信息
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除和套餐和菜品的关联数据
     *
     * @param ids id
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 修改套餐的销售状态
     *
     * @param ids    id
     * @param status 状态
     */
   public void updateStatus(List<Long> ids, int status);
}
