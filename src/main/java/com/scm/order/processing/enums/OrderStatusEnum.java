package com.scm.order.processing.enums;

public enum OrderStatusEnum {
    ACCEPTED("A"),
    DISPATCHED("D"),
    DELIVERED("DL"),
    INTRANSIT("IT"),
    PLACED("P");

    public final String code;
    private OrderStatusEnum(String code){
        this.code = code;
    }
}
