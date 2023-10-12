package com.zooplus.tests;

import com.zooplus.annotations.SidCookie;
import com.zooplus.tests.base.WebTest;
import com.zooplus.utils.BrowserActions;
import org.junit.jupiter.api.Test;

public class CartTests extends WebTest {

    BrowserActions browserActions = new BrowserActions();

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void draftTest() {
        cartPage.openCartPage();
        browserActions.verifyUrlContains("/cart");
        System.out.println("test");

        cartPage.addProductFromEmptyCartRecommendations();
        cartPage.addProductFromTopRecommendations(3);
        cartPage.addProductFromBottomRecommendations(4);
        System.out.println("test");
        cartPage.deleteHighestPricedProduct(1);
        cartPage.increaseLowestPricedProductCountByOne(3);
        cartPage.getAllAddedToCartProductsWithCountAndSubtotal();
        cartPage.getAllAddedToCartProductsWithCountAndSubtotal();
        cartPage.verifySubTotalPricePerProduct();
        cartPage.verifySubTotalPricePerProduct();
        cartPage.verifySubtotalPriceForCart();
        cartPage.verifySubtotalPriceForCart();


        System.out.println("test");


//        System.out.println("Here's the list: " + cartPage.getItemsFromEmptyCartCarousel(3, PriceSortingTypes.HIGHEST));
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void multipleProductsCanBeAddedToCart() {
        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendations()
                .addProductFromTopRecommendations(2)
                .addProductFromBottomRecommendations(3)
                .verifyCartPageUrl()
                .verifySubTotalPricePerProduct()
                .verifySubtotalPriceForCart();
    }
}
