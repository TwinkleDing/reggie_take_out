package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
        request.getSession().getAttribute("employ");

        return Request.success(emp);
    }

    @PostMapping("/logout")
    public Request<String> logout(HttpServletRequest request) {
        // 清理session中的数据
        request.getSession().removeAttribute("employ");
        return Request.success("退出成功");
    }
}
