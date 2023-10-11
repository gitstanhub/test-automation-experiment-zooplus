package com.zooplus.tests.base;

import com.codeborne.selenide.Selenide;
import com.zooplus.pageobjects.CartPage;
import org.junit.jupiter.api.AfterEach;

public class WebTest {

    protected CartPage cartPage = new CartPage();

//    @AfterEach
//    void tearDown() {
//        Selenide.closeWebDriver();
//    }
}
