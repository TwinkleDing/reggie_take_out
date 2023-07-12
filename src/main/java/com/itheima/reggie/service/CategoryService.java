package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @author TinkleDing
 */
public interface CategoryService extends IService<Category> {
    /**
     * 判断删除
     * @param id id
     */
    void remove(Long id);
}
