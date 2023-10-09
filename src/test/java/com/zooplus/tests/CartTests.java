package com.zooplus.tests;

import com.zooplus.annotations.SidCookie;
import com.zooplus.tests.base.WebTest;
import org.junit.jupiter.api.Test;

public class CartTests extends WebTest {

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void testCheck() {

        cartPage.openCartPage();
    }
}
