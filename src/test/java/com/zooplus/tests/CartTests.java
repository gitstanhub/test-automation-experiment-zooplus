package com.zooplus.tests;

import com.codeborne.selenide.Selenide;
import com.zooplus.annotations.SidCookie;
import com.zooplus.tests.base.WebTest;
import org.junit.jupiter.api.Test;

public class CartTests extends WebTest {

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void testCheck() {
        Selenide.open("https://www.zooplus.com/checkout/cart");
        Selenide.open("https://www.zooplus.com/checkout/cart");
        Selenide.open("https://www.zooplus.com/checkout/cart");
    }
}
