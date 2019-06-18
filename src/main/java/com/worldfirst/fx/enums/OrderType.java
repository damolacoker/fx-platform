package com.worldfirst.fx.enums;

public enum OrderType {
    BID("BID"),
    ASK("ASK");

    private String value;

    OrderType(String type) {

        this.value = type;
    }

    public String value() {

        return value;
    }
}
