package com.worldfirst.fx.enums;

public enum OrderStatus {
    PENDING("PENDING"),
    EXECUTED("EXECUTED"),
    CANCELLED("CANCELLED");

    private String value;

    OrderStatus(String status){

        this.value=status;
    }

    public String value() {

        return value;
    }
}
