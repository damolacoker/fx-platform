package com.worldfirst.fx.enums;

public enum Currency {
    GBP_USD("GBP/USD"),
    USD_GBP("USD/GBP"),
    EUR_USD("EUR/USD");

    private String value;

    Currency(String currency) {

        this.value = currency;
    }

    public String value() {

        return value;
    }
}
