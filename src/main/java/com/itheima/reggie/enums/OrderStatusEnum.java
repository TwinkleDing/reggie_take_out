package com.itheima.reggie.enums;

/**
 * @author TwinkleDing
 */

public enum OrderStatusEnum {

    WAIT_PAY(1, "待付款"),
    SENDING(2, "正在派送"),
    HAS_SENDED(3, "已派送"),
    HAS_FINISH(4, "已完成"),
    HAS_CANCEL(5, "已取消");

    private final int code;
    private final String desc;


    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
