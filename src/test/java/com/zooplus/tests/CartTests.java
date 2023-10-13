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
                .verifySubTotalPricePerProduct()
                .verifySubtotalPriceForCart();

        cartPage
                .verifyCartPageUrl();
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void addedToCartProductAmountCanBeAdjusted() {
        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendations()
                .addProductFromTopRecommendations(4)
                .addProductFromBottomRecommendations(4);

        cartPage
                .deleteHighestPricedProduct(1)
                .increaseLowestPricedProductCountByOne(3)
                .verifySubTotalPricePerProduct()
                .verifySubtotalPriceForCart();

        cartPage
                .verifyCartPageUrl();
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
                .addProductFromEmptyCartRecommendations();

        cartPage
                .selectShippingCountry(firstCountryName)
                .verifyShippingCountry(firstCountryName)
                .verifyShippingFee(firstCountryShippingFee);

        cartPage
                .selectShippingCountry(secondCountryName, secondCountryPostcode)
                .verifyShippingCountry(secondCountryName, secondCountryPostcode)
                .verifyShippingFee(secondCountryShippingFee)
                .verifyTotalPriceForCart();

        cartPage
                .verifyCartPageUrl();
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void freeShippingCanBeApplied() {
        String shippingCountryName = ShippingCountries.GERMANY.getCountryName();
        double initialShippingFee = ShippingCountries.GERMANY.getShippingFee();
        String updatedShippingFee = ShippingCountries.freeShippingFee;
        double freeShippingCap = 50.00;

        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendationsBelowPrice(freeShippingCap);

        cartPage
                .selectShippingCountry(shippingCountryName)
                .verifyShippingFee(initialShippingFee)
                .addNewProductsUntilSubtotal(freeShippingCap)
                .verifyShippingFee(updatedShippingFee);

        cartPage
                .verifyCartPageUrl();
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void cannotProceedWithOrderBelowMinimal() {
        double minimalOrderPrice = 19.00;

        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendationsBelowPrice(minimalOrderPrice)
                .verifyMinimumOrderError(minimalOrderPrice)
                .verifyProceedButtonIsDisabled();

        cartPage
                .verifyCartPageUrl();
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void lastRemovedProductCanBeRestored() {

        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendations()
                .deleteHighestPricedProduct(1)
                .verifyEmptyCartMessageIsDisplayed()
                .undoLastProductRemoval()
                .verifyCartIsNotEmpty();
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void cannotAddProductItemsAboveLimit() {

        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendations()
                .updateLowestPricedProductCountByNumber(1, 20)
                .verifyIncreaseProductCountButtonIsDisabled();

        cartPage
                .verifyCartPageUrl();
    }

    @Test
    @SidCookie(sidCookieValue = "stanislav-dmitruk-test")
    void invalidCouponCodeCannotBeApplied() {
        String couponCode = "TestAbc123";

        cartPage
                .openCartPage()
                .addProductFromEmptyCartRecommendations()
                .clickCouponCodeButton()
                .submitCouponCode(couponCode)
                .verifyCouponCodeErrorMessage(couponCode);
    }
}
