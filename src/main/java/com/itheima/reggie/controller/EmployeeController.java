package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author TwinkleDing
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request  http
     * @param employee employee
     * @return 登录结果
     */
    @PostMapping("/login")
    public Request<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 账户没有，返回失败
        if (emp == null) {
            return Request.error("登录失败");
        }

        // 账户有
        if (!emp.getPassword().equals(password)) {
            return Request.error("登录失败");
        }

        if (emp.getStatus() == 0) {
            return Request.error("账号已禁用");
        }

        // 登录成功
        request.getSession().setAttribute("employee", emp.getId());

        return Request.success(emp);
    }

    @PostMapping("/logout")
    public Request<String> logout(HttpServletRequest request) {
        // 清理session中的数据
        request.getSession().removeAttribute("employ");
        return Request.success("退出成功");
    }

    @PostMapping
    public Request<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        Long empId = (long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);

        return Request.success("新增员工成功！");
    }

    /**
     * 员工信息分页查询
     *
     * @param page     页码
     * @param pageSize 页数量
     * @param name     筛选条件
     * @return 员工分页信息
     */
    @GetMapping("/page")
    public Request<Page<Employee>> page(int page, int pageSize, String name) {

        // 构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, queryWrapper);

        return Request.success(pageInfo);
    }

    /**
     * 根据员工id修改状态
     *
     * @param employee 员工信息
     * @return 修改结果
     */
    @PutMapping
    public Request<String> update(HttpServletRequest request, @RequestBody Employee employee) {

        Long emId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(emId);
        employeeService.updateById(employee);

        return Request.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public Request<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return Request.success(employee);
    }

}
