package com.zooplus.tests;

import com.zooplus.annotations.SidCookie;
import com.zooplus.constants.commons.ShippingCountries;
import com.zooplus.tests.base.WebTest;
import org.junit.jupiter.api.Test;

public class CartTests extends WebTest {

//    @Test
//    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
//    void draftTest() {
//        cartPage.openCartPage();
//        browserActions.verifyUrlContains("/cart");
//        System.out.println("test");
//
//        cartPage.addProductFromEmptyCartRecommendations();
//        cartPage.addProductFromTopRecommendations(3);
//        cartPage.addProductFromBottomRecommendations(4);
//        System.out.println("test");
//        cartPage.deleteHighestPricedProduct(1);
//        cartPage.increaseLowestPricedProductCountByOne(3);
//        cartPage.getAllAddedToCartProductsWithCountAndSubtotal();
//        cartPage.getAllAddedToCartProductsWithCountAndSubtotal();
//        cartPage.verifySubTotalPricePerProduct();
//        cartPage.verifySubTotalPricePerProduct();
//        cartPage.verifySubtotalPriceForCart();
//        cartPage.verifySubtotalPriceForCart();
//
//
//        System.out.println("test");
//    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void multipleProductsCanBeAddedToCart() {
        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendations()
                .addProductFromTopRecommendations(1)
                .addProductFromBottomRecommendations(2)
                .verifyCartPageUrl()
                .verifySubTotalPricePerProduct()
                .verifySubtotalPriceForCart();
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void addedToCartProductAmountCanBeAdjusted() {
        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendations()
                .addProductFromTopRecommendations(4)
                .addProductFromBottomRecommendations(4)
                .deleteHighestPricedProduct(1)
                .increaseLowestPricedProductCountByOne(3)
                .verifyCartPageUrl()
                .verifySubTotalPricePerProduct()
                .verifySubtotalPriceForCart();
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void shippingCountryCanBeChanged() {
        String firstCountryName = ShippingCountries.SWEDEN.getCountryName();
        double firstCountryShippingFee = ShippingCountries.SWEDEN.getShippingFee();

        String secondCountryName = ShippingCountries.PORTUGAL.getCountryName();
        double secondCountryShippingFee = ShippingCountries.PORTUGAL.getShippingFee();
        String secondCountryPostcode = "5000";

        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendations()
                .selectShippingCountry(firstCountryName)
                .verifyShippingFee(firstCountryShippingFee)
                .selectShippingCountry(secondCountryName, secondCountryPostcode)
                .verifyShippingFee(secondCountryShippingFee)
                .verifyTotalPriceForCart();
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void freeShippingCanBeApplied() {
        cartPage
                .openCartPage()
                .addProductBelowPrice(0.01);
        System.out.println("test");

    }
}
