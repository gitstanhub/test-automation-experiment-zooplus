package com.zooplus.tests;

import com.zooplus.annotations.SidCookie;
import com.zooplus.tests.base.WebTest;
import com.zooplus.utils.BrowserActions;
import org.junit.jupiter.api.Test;

public class CartTests extends WebTest {

    BrowserActions browserActions = new BrowserActions();

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void multipleProductsCanBeAddedToCart() {
        cartPage.openCartPage();
        browserActions.verifyUrlContains("/cart");

    }



}
