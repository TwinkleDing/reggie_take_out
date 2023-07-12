package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author TwinkleDing
 * 分类管理
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category 分类
     * @return 新增结果
     */
    @PostMapping
    public Request<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return Request.success("新增分类成功！");
    }

    /**
     * 分页查询
     *
     * @param page     页码
     * @param pageSize 页数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Request<Page<Category>> page(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> query = new LambdaQueryWrapper<>();
        query.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, query);
        return Request.success(pageInfo);
    }

    /**
     * 根据id删除分类
     *
     * @param ids ids
     * @return 是否删除成功
     */
    @DeleteMapping
    public Request<String> delete(Long ids) {
        categoryService.remove(ids);
        return Request.success("刪除成功！");
    }

    /**
     * 更新分类
     *
     * @param category 分类信息
     * @return 修改结果
     */
    @PutMapping
    public Request<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return Request.success("修改分类信息成功！");

    }
}
