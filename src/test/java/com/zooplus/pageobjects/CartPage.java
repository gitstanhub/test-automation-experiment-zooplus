package com.zooplus.pageobjects;

import com.codeborne.selenide.*;
import com.zooplus.constants.commons.PriceSortingTypes;
import com.zooplus.constants.commons.ShippingCountries;
import com.zooplus.models.ProductItem;
import com.zooplus.pageobjects.base.SelenidePage;
import com.zooplus.pageobjects.commons.CookiesPopup;
import com.zooplus.utils.BrowserActions;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.zooplus.constants.commons.RecommendationsCarouselConstants.*;
import static com.zooplus.locators.CartPageLocators.*;
import static com.zooplus.locators.commons.RecommendationsCarouselLocators.*;

public class CartPage extends SelenidePage {

    CookiesPopup cookiesPopup = new CookiesPopup();
    BrowserActions browserActions = new BrowserActions();

    @Step
    public CartPage openCartPage() {
        Selenide.open("https://www.zooplus.com/checkout/cart");
        cookiesPopup.handleCookiesPopup();
        return this;
    }

    @Step
    public CartPage addProductFromEmptyCartRecommendations() {
        getRecommendationCarouselItem().shouldBe(Condition.visible, Duration.ofMillis(5000));
        addFirstProductFromCarouselToCart(EMPTY_CART_RECOMMENDATION_CAROUSEL_TITLE);
        return this;
    }

    @Step
    public CartPage addProductFromTopRecommendations(int productsToAdd) {
        getProductRemoveButton().shouldBe(Condition.visible, Duration.ofMillis(5000));
        getRecommendationCarouselItem().shouldBe(Condition.visible, Duration.ofMillis(5000));
        for (int i = 0; i <= productsToAdd; i++) {
            addFirstProductFromCarouselToCart(ACTIVE_CART_FIRST_RECOMMENDATION_CAROUSEL_TITLE);
        }
        return this;
    }

    @Step
    public CartPage addProductFromBottomRecommendations(int productsToAdd) {
        getProductRemoveButton().shouldBe(Condition.visible, Duration.ofMillis(5000));
        getRecommendationCarouselItem().shouldBe(Condition.visible, Duration.ofMillis(5000));
        getCarousel(ACTIVE_CART_SECOND_RECOMMENDATION_CAROUSEL_TITLE).scrollIntoView("{behavior: \"smooth\"}");
        getCarousel(ACTIVE_CART_SECOND_RECOMMENDATION_CAROUSEL_TITLE).shouldBe(Condition.visible, Duration.ofMillis(5000));

        for (int i = 0; i <= productsToAdd; i++) {
            addFirstProductFromCarouselToCart(ACTIVE_CART_SECOND_RECOMMENDATION_CAROUSEL_TITLE);
        }
        return this;
    }

    private List<ProductItem> getItemsFromRecommendationCarousel(String carouselTitle, int desiredItemsAmount, PriceSortingTypes sortBy) {
        List<ProductItem> carouselWithItems = getAllRecommendationCarouselsWithSortedItems().get(carouselTitle);

        return switch (sortBy) {
            case HIGHEST -> getHighestPricedItems(carouselWithItems, desiredItemsAmount);
            case LOWEST -> getLowestPricedItems(carouselWithItems, desiredItemsAmount);
        };
    }

    public CartPage verifySubTotalPricePerProduct() {
        List<ProductItem> addedToCartProducts = getAllAddedToCartProductsWithCountAndSubtotal();

        for (ProductItem addedToCartProduct : addedToCartProducts) {
            double expectedSubtotalPrice = addedToCartProduct.getItemPrice() * addedToCartProduct.getItemSubtotal().getItemCount();
            double actualSubtotalPrice = addedToCartProduct.getItemSubtotal().getSubtotalPrice();
            Assertions.assertEquals(expectedSubtotalPrice, actualSubtotalPrice);
        }

        return this;
    }

    public CartPage verifySubtotalPriceForCart() {
        flowOverTheCartItems();

        List<ProductItem> addedToCartProducts = getAllAddedToCartProductsWithCountAndSubtotal();

        double addedProductsSubtotalSum = addedToCartProducts.stream()
                .map(ProductItem::getItemSubtotal)
                .filter(Objects::nonNull)
                .mapToDouble(ProductItem.ItemSubtotal::getSubtotalPrice)
                .sum();

        System.out.println("Debug 777666: " + addedProductsSubtotalSum);

        double addedProductsSubtotalSumFormatted = (int) (addedProductsSubtotalSum * 100) / 100.0;

        double actualCartSubtotal = Double.parseDouble(getCartSubtotal().getText().replaceAll("[^0-9.]", ""));

        Assertions.assertEquals(addedProductsSubtotalSumFormatted, actualCartSubtotal);

        return this;
    }

    public CartPage verifyTotalPriceForCart() {
        double actualCartSubtotal = Double.parseDouble(getCartSubtotal().getText().replaceAll("[^0-9.]", ""));
        double actualCartTotal = Double.parseDouble(getCartTotalPrice().getText().replaceAll("[^0-9.]", ""));
        String actualShippingFee = getShippingFeeElement().getText();

        if (!actualShippingFee.equals(ShippingCountries.freeShippingFee)) {
            double actualShippingFeeFormatted = Double.parseDouble(actualShippingFee.replaceAll("[^0-9.]", ""));
            Assertions.assertEquals(actualShippingFeeFormatted + actualCartSubtotal, actualCartTotal);
        } else {
            Assertions.assertEquals(actualCartSubtotal, actualCartTotal);
        }
        return this;
    }

    public CartPage increaseLowestPricedProductCountByOne(int productsToIncrease) {
        List<ProductItem> addedProducts = getAllAddedToCartProducts();
        List<ProductItem> lowestPricedProducts = getLowestPricedItems(addedProducts, productsToIncrease);

        for (ProductItem lowestPricedProduct : lowestPricedProducts) {
            clickIncrementButtonForProduct(lowestPricedProduct.getItemId());
        }
        return this;
    }

    public CartPage addNewProductsUntilSubtotal(double desiredSubtotal) {
        double currentSubtotal = Double.parseDouble(getCartSubtotal().getText().replaceAll("[^0-9.]", ""));

        while (currentSubtotal < desiredSubtotal) {
            addProductFromTopRecommendations(1);
            getCartSubtotal().shouldBe(Condition.partialText("€"));
            currentSubtotal = Double.parseDouble(getCartSubtotal().getText().replaceAll("[^0-9.]", ""));
            System.out.println("Debug 000: " + currentSubtotal);
        }
        return this;
    }

    public CartPage deleteHighestPricedProduct(int productsToDelete) {
        getAddedToCartItem().scrollIntoView("{behavior: \"smooth\"}");
        getAddedToCartItem().shouldBe(Condition.visible, Duration.ofMillis(5000));
        getRecommendationCarouselItem().shouldBe(Condition.visible, Duration.ofMillis(5000));

        List<ProductItem> addedProducts = getAllAddedToCartProducts();
        List<ProductItem> highestPricedProducts = getHighestPricedItems(addedProducts, productsToDelete);

        for (ProductItem highestPricedProduct : highestPricedProducts) {
            getProductRemoveButton().shouldBe(Condition.visible, Duration.ofMillis(5000));
            clickDeleteButtonForProduct(highestPricedProduct.getItemId());
        }

        return this;
    }

    public CartPage verifyCartPageUrl() {
        browserActions.verifyUrlContains("/cart");
        return this;
    }

    public CartPage selectShippingCountry(String countryName) {
        getSelectedShippingCountryElement().click();
        getShippingCountryDropdownButton().click();
        $$(CART_SHIPPING_COUNTRY_DROPDOWN_LIST_ITEM).findBy(Condition.text(countryName)).click();
        getShippingCountrySubmitButton().click();

        getSelectedShippingCountryElement().shouldHave(Condition.text(countryName));

        return this;
    }

    public CartPage selectShippingCountry(String countryName, String postCode) {
        getSelectedShippingCountryElement().click();
        getShippingCountryDropdownButton().click();
        $$(CART_SHIPPING_COUNTRY_DROPDOWN_LIST_ITEM).findBy(Condition.text(countryName)).click();
        getShippingCountryPostcodeField().setValue(String.valueOf(postCode));
        getShippingCountrySubmitButton().click();

        getSelectedShippingCountryElement().shouldHave(Condition.text(countryName));

        return this;
    }

    public CartPage verifyShippingFee(double expectedShippingFee) {
        String actualShippingFee = getShippingFeeElement().getText();
        double actualShippingFeeFormatted = Double.parseDouble(actualShippingFee.replaceAll("[^0-9.]", ""));

        Assertions.assertEquals(expectedShippingFee, actualShippingFeeFormatted);

        return this;
    }

    public CartPage verifyShippingFee(String expectedShippingFee) {
        String actualShippingFee = getShippingFeeElement().getText();

        Assertions.assertEquals(expectedShippingFee, actualShippingFee);

        return this;
    }

    public CartPage addProductBelowPrice(double priceCap) {
        getRecommendationCarouselItem().shouldBe(Condition.visible, Duration.ofMillis(5000));

        boolean isProductFound = false;

        int visibleItemsPerSlide = getAllRecommendationCarouselsWithSortedItems().get(EMPTY_CART_RECOMMENDATION_CAROUSEL_TITLE).size();
        int totalAmountOfItemsInCarousel = Integer.parseInt(getRecommendationCarouselItemWrapper().getAttribute("aria-label").split(" ")[2]);
        double totalAmountOfSlidesInCarousel = (double) totalAmountOfItemsInCarousel / visibleItemsPerSlide;

        System.out.println("DEBUGER HERE ###: " + visibleItemsPerSlide + " " + totalAmountOfItemsInCarousel + " " + totalAmountOfSlidesInCarousel);

        for (int i = 0; i < totalAmountOfSlidesInCarousel - 1; i++) {
            List<ProductItem> carouselItems = getItemsFromRecommendationCarousel(
                    EMPTY_CART_RECOMMENDATION_CAROUSEL_TITLE, 1, PriceSortingTypes.LOWEST);

            ProductItem fetchedItem = carouselItems.get(0);

            if (fetchedItem.getItemPrice() < priceCap) {
                getCarousel(EMPTY_CART_RECOMMENDATION_CAROUSEL_TITLE).$(String.format(RECOMMENDATIONS_CAROUSEL_SPECIFIC_ITEM_ADD_BUTTON, fetchedItem.getItemId())).click();
                isProductFound = true;
                break;
            } else {
                getNextSlideButton().click();
                getCarousel(EMPTY_CART_RECOMMENDATION_CAROUSEL_TITLE).$(RECOMMENDATION_CAROUSEL_PREVIOUS_SLIDE_BUTTON).shouldBe(Condition.visible, Duration.ofMillis(5000));
            }
        }

        if (!isProductFound) {
            throw new IllegalArgumentException("No element found below the specified price");
        }
        return this;
    }


    private List<ProductItem> getHighestPricedItems(List<ProductItem> itemsList, int desiredItemsAmount) {
        return itemsList.stream()
                .sorted(Comparator.comparing(ProductItem::getItemPrice).reversed())
                .limit(desiredItemsAmount)
                .collect(Collectors.toList());
    }

    private List<ProductItem> getLowestPricedItems(List<ProductItem> itemsList, int desiredItemsAmount) {
        return itemsList.stream()
                .limit(desiredItemsAmount)
                .collect(Collectors.toList());
    }

    private Map<String, List<ProductItem>> getAllRecommendationCarouselsWithSortedItems() {
        Map<String, List<ProductItem>> allCarouselsWithSortedItems = new HashMap<>();

        ElementsCollection carousels = elementActions.findAllCarouselsOfSameType(RECOMMENDATIONS_CAROUSEL);

        for (SelenideElement carousel : carousels) {
            String rawCarouselTitle = carousel.parent().$(RECOMMENDATIONS_CAROUSEL_TITLE).getText();

            ElementsCollection rawCarouselItems = elementActions.findAllCarouselItems(carousel, RECOMMENDATIONS_CAROUSEL_ITEM);
            ElementsCollection visibleCarouselItems = rawCarouselItems.filter(Condition.visible);

//            System.out.println("DEBUG HERE: " + visibleCarouselItems.stream().toList().size());
            List<ProductItem> productItems = new ArrayList<>();
            for (SelenideElement visibleCarouselItem : visibleCarouselItems) {
                String itemId = visibleCarouselItem.$(RECOMMENDATIONS_CAROUSEL_ITEM_LINK).getAttribute("id");
                double itemPrice = Double.parseDouble(visibleCarouselItem.$(RECOMMENDATIONS_CAROUSEL_ITEM_PRICE).getText().replaceAll("[^0-9.]", ""));

                productItems.add(new ProductItem(itemId, itemPrice));
                System.out.println("DEBUG HERE: " + productItems);
            }

            productItems.sort(Comparator.comparing(ProductItem::getItemPrice));

            allCarouselsWithSortedItems.put(rawCarouselTitle, productItems);
        }

        return allCarouselsWithSortedItems;
    }

    public List<ProductItem> getAllAddedToCartProductsWithCountAndSubtotal() {
        flowOverTheCartItems();

        List<ProductItem> addedToCartProducts = new ArrayList<>();

        ElementsCollection addedItems = getAllAddedToCartProductsContainers();

        for (SelenideElement addedItem : addedItems) {
            String itemId = addedItem.$(ADDED_TO_CART_PRODUCT_NAME).getText();
            double itemPrice = Double.parseDouble(addedItem.$(ADDED_TO_CART_PRODUCT_PRICE).getText().replaceAll("[^0-9.]", ""));

            int itemCount = Integer.parseInt(addedItem.$(ADDED_TO_CART_PRODUCT_COUNTER).getAttribute("value"));
            double subtotalPrice = Double.parseDouble(addedItem.$(ADDED_TO_CART_PRODUCT_SUBTOTAL_PRICE).getText().replaceAll("[^0-9.]", ""));

            ProductItem productItem = new ProductItem(itemId, itemPrice);
            ProductItem.ItemSubtotal itemSubtotal = new ProductItem.ItemSubtotal(itemCount, subtotalPrice);
            productItem.setItemSubtotal(itemSubtotal);

            addedToCartProducts.add(productItem);

            System.out.println("DEBUG HERE 9999: " + addedToCartProducts);
        }

        addedToCartProducts.sort(Comparator.comparing(ProductItem::getItemPrice));
        System.out.println("DEBUG HERE 989898: " + addedToCartProducts);
        return addedToCartProducts;
    }

    public List<ProductItem> getAllAddedToCartProducts() {
        List<ProductItem> addedToCartProducts = new ArrayList<>();

        ElementsCollection addedItems = getAllAddedToCartProductsContainers();

        for (SelenideElement addedItem : addedItems) {
            String itemId = addedItem.$(ADDED_TO_CART_PRODUCT_NAME).getText();
            double itemPrice = Double.parseDouble(addedItem.$(ADDED_TO_CART_PRODUCT_PRICE).getText().replaceAll("[^0-9.]", ""));

            addedToCartProducts.add(new ProductItem(itemId, itemPrice));
            System.out.println("DEBUG HERE 777: " + addedToCartProducts);
        }

        addedToCartProducts.sort(Comparator.comparing(ProductItem::getItemPrice));
        System.out.println("DEBUG HERE 888: " + addedToCartProducts);
        return addedToCartProducts;
    }

    private void addFirstProductFromCarouselToCart(String carouselTitle) {
        SelenideElement firstCarouselItemButton = getCarousel(carouselTitle).$$(RECOMMENDATIONS_CAROUSEL_ITEM_ADD_BUTTON).first();
        firstCarouselItemButton.click();
        getCartSubtotal().shouldBe(Condition.partialText("€"));
    }

    private SelenideElement getCarousel(String carouselTitle) {
        ElementsCollection carousels = elementActions.findAllCarouselsOfSameType(RECOMMENDATIONS_CAROUSEL);

        for (SelenideElement carousel : carousels) {
            if (carousel.parent().$(RECOMMENDATIONS_CAROUSEL_TITLE).has(Condition.partialText(carouselTitle))) {
                return carousel;
            }
        }
        return null;
    }

    private void clickIncrementButtonForProduct(String expectedProductName) {
        ElementsCollection addedItems = getAllAddedToCartProductsContainers();

        for (SelenideElement addedItem : addedItems) {
            String actualProductName = addedItem.$(ADDED_TO_CART_PRODUCT_NAME).getText();
            int itemCount = Integer.parseInt(addedItem.$(ADDED_TO_CART_PRODUCT_COUNTER).getAttribute("value"));
            double initialProductSubtotal = Double.parseDouble(addedItem.$(ADDED_TO_CART_PRODUCT_SUBTOTAL_PRICE).getText().replaceAll("[^0-9.]", ""));
            double initialProductSubtotalFormatted = (int) (initialProductSubtotal * 100) / 100.0;

            if (actualProductName.contains(expectedProductName)) {
                addedItem.$(ADDED_TO_CART_PRODUCT_INCREMENT_BUTTON).click();
                addedItem.$(ADDED_TO_CART_PRODUCT_COUNTER).shouldHave(Condition.attribute("value", String.valueOf(itemCount + 1)));
                addedItem.$(ADDED_TO_CART_PRODUCT_SUBTOTAL_PRICE).shouldBe(Condition.not(Condition.text(String.valueOf("€" + initialProductSubtotalFormatted))), Duration.ofMillis(5000));
                break;
            }
        }
    }

    private void clickDeleteButtonForProduct(String expectedProductName) {
        ElementsCollection addedItems = getAllAddedToCartProductsContainers();

        for (SelenideElement addedItem : addedItems) {
            String actualProductName = addedItem.$(ADDED_TO_CART_PRODUCT_NAME).getText();
            if (actualProductName.contains(expectedProductName)) {
                addedItem.$(ADDED_TO_CART_PRODUCT_REMOVE_BUTTON).click();
            }
        }
    }

    private ElementsCollection getAllAddedToCartProductsContainers() {
        flowOverTheCartItems();

        return $$(ADDED_TO_CART_PRODUCT_MAIN_BOX);
    }

    private SelenideElement getCartSubtotal() {
        return $(CART_SUBTOTAL_PRICE);
    }

    private SelenideElement getRecommendationCarouselItem() {
        return $(RECOMMENDATIONS_CAROUSEL_ITEM);
    }

    private SelenideElement getRecommendationCarouselItemWrapper() {
        return $(RECOMMENDATIONS_CAROUSEL_ITEM_WRAPPER);
    }

    private SelenideElement getCartSummarySection() {
        return $(CART_SUMMARY_SECTION);
    }

    private SelenideElement getProductRemoveButton() {
        return $(ADDED_TO_CART_PRODUCT_REMOVE_BUTTON);
    }

    private SelenideElement getAddedToCartItem() {
        return $(ADDED_TO_CART_PRODUCT_MAIN_BOX);
    }

    private SelenideElement getProductIncrementButton() {
        return $(ADDED_TO_CART_PRODUCT_INCREMENT_BUTTON);
    }

    private void flowOverTheCartItems() {
        $$(ADDED_TO_CART_PRODUCT_MAIN_BOX).last().scrollIntoView("{behavior: \"smooth\"}");
        $$(ADDED_TO_CART_PRODUCT_MAIN_BOX).first().scrollIntoView("{behavior: \"smooth\"}");
        getAddedToCartItem().scrollIntoView("{behavior: \"smooth\"}");
        getAddedToCartItem().shouldBe(Condition.visible);
    }

    private SelenideElement getShippingFeeElement() {
        return $(CART_SHIPPING_FEE);
    }

    private SelenideElement getSelectedShippingCountryElement() {
        return $(CART_SHIPPING_COUNTRY_SELECTED);
    }

    private SelenideElement getShippingCountryDropdownButton() {
        return $(CART_SHIPPING_COUNTRY_DROPDOWN_BUTTON);
    }

    private SelenideElement getShippingCountryPostcodeField() {
        return $(CART_SHIPPING_COUNTRY_POSTCODE_FIELD);
    }

    private SelenideElement getShippingCountrySubmitButton() {
        return $(CART_SHIPPING_COUNTRY_UPDATE_BUTTON);
    }

    private SelenideElement getCartTotalPrice() {
        return $(CART_TOTAL_PRICE);
    }

    private SelenideElement getNextSlideButton() {
        return $(RECOMMENDATION_CAROUSEL_NEXT_SLIDE_BUTTON);
    }

    private SelenideElement getPreviousSlideButton() {
        return $(RECOMMENDATION_CAROUSEL_PREVIOUS_SLIDE_BUTTON);
    }
}
