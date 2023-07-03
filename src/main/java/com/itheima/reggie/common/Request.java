package com.itheima.reggie.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TwinkleDing
 */
@Data
public class Request<T> {

    /**
     * 编码：1成功，0和其它数字为失败
     */
    private Integer code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 数据
     */

    private T data;

    /**
     * 动态数据
     */
    private Map<String, Object> map = new HashMap<>();

    public static <T> Request<T> success(T object) {
        Request<T> r = new Request<>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> Request<T> error(String msg) {
        Request<T> r = new Request<>();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public Request<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
