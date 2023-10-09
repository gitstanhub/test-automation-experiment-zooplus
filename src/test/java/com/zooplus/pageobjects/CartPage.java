package com.zooplus.pageobjects;

import com.codeborne.selenide.Selenide;
import com.zooplus.pageobjects.commons.CookiesPopup;
import io.qameta.allure.Step;

public class CartPage {

    CookiesPopup cookiesPopup = new CookiesPopup();

    @Step
    public CartPage openCartPage() {
        Selenide.open("https://www.zooplus.com/checkout/cart");
        cookiesPopup.handleCookiesPopup();
        return this;
    }
}
