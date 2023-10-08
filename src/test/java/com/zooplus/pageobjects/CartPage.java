package com.zooplus.pageobjects;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

public class CartPage {

    @Step
    public CartPage openCartPage() {
        Selenide.open();
        return this;
    }
}
