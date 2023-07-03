package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author TwinkleDing
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 进行异常处理方法
     *
     * @return 返回异常
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Request<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        String d = "Duplicate entry";
        if (ex.getMessage().contains(d)) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return Request.error(msg);
        }

        return Request.error("失败了！");
    }
}
