package com.itheima.reggie.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.Request;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Request<User> login(HttpServletRequest request, @RequestBody Map map) {

        // 判断当前用户是否为新用户，新用户注册再登录

        String phone = "13201631863";
        // String code = map.get("code").toString();
        log.info("phone", phone);

        // 登录随便写写
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userService.getOne(queryWrapper);
        log.info("user", user == null);
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        // request.getSession().setAttribute("user", user.getPhone());

        return Request.success(user);
    }

}
