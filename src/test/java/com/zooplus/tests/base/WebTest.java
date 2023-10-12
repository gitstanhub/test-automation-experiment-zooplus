package com.zooplus.tests.base;

import com.codeborne.selenide.Configuration;
import com.zooplus.pageobjects.CartPage;
import org.junit.jupiter.api.BeforeAll;

public class WebTest {

    protected CartPage cartPage = new CartPage();

    @BeforeAll
    static void setUp() {
        Configuration.timeout = 5000;
    }
}
