package com.zooplus.constants.commons;

import lombok.Getter;

@Getter
public enum ShippingCountries {

    ESTONIA("Estonia", 9.99),
    GERMANY("Germany", 4.99),
    PORTUGAL("Portugal", 6.99),
    SWEDEN("Sweden", 7.99),
    SWITZERLAND("Switzerland (from EU)", 9.99);

    private final String countryName;

    private final double shippingFee;

    public static final String freeShippingFee = "FREE";

    ShippingCountries(String countryName, double shippingFee) {
        this.countryName = countryName;
        this.shippingFee = shippingFee;
    }
}
