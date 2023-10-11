package com.zooplus.pageobjects;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.zooplus.constants.commons.PriceSortingTypes;
import com.zooplus.models.ProductItem;
import com.zooplus.pageobjects.base.SelenidePage;
import com.zooplus.pageobjects.commons.CookiesPopup;
import io.qameta.allure.Step;

import java.util.*;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.$$;
import static com.zooplus.constants.commons.RecommendationsCarouselConstants.*;
import static com.zooplus.locators.CartPageLocators.*;
import static com.zooplus.locators.commons.RecommendationsCarouselLocators.*;

public class CartPage extends SelenidePage {

    //add method that will add desired amount of products from a recommended section

    CookiesPopup cookiesPopup = new CookiesPopup();

    @Step
    public CartPage openCartPage() {
        Selenide.open("https://www.zooplus.com/checkout/cart");
        cookiesPopup.handleCookiesPopup();
        return this;
    }

    @Step
    public CartPage addProductFromEmptyCartRecommendations() {
        addFirstProductFromCarouselToCart(EMPTY_CART_RECOMMENDATION_CAROUSEL_TITLE);
        return this;
    }

    @Step
    public CartPage addProductFromTopRecommendations(int itemCount) {
        for (int i = 0; i <= itemCount; i++) {
            addFirstProductFromCarouselToCart(ACTIVE_CART_FIRST_RECOMMENDATION_CAROUSEL_TITLE);
        }
        return this;
    }

    @Step
    public CartPage addProductFromBottomRecommendations(int itemCount) {
        for (int i = 0; i <= itemCount; i++) {
            addFirstProductFromCarouselToCart(ACTIVE_CART_SECOND_RECOMMENDATION_CAROUSEL_TITLE);
        }
        return this;
    }

    public List<ProductItem> getItemsFromEmptyCartCarousel(int desiredItemsAmount, PriceSortingTypes sortBy) {

        List<ProductItem> carouselWithItems = getAllRecommendationCarouselsWithSortedItems().get(EMPTY_CART_RECOMMENDATION_CAROUSEL_TITLE);

        return switch (sortBy) {
            case HIGHEST -> getHighestPricedItems(carouselWithItems, desiredItemsAmount);
            case LOWEST -> getLowestPricedItems(carouselWithItems, desiredItemsAmount);
        };
    }

    public void increaseLowestPricedProductCountByOne(int productAmount) {

        List<ProductItem> addedProducts = getAllAddedToCartProducts();
        List<ProductItem> lowestPricedProducts = getLowestPricedItems(addedProducts, productAmount);

        for (ProductItem lowestPricedProduct : lowestPricedProducts) {
            clickIncrementButtonForProduct(lowestPricedProduct.getItemId());
        }

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

            System.out.println("DEBUG HERE: " + visibleCarouselItems.stream().toList().size());
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
        getCarousel(carouselTitle).$$("[data-testid='recommendation-item'] button").first().click();
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
            if (actualProductName.contains(expectedProductName)) {
                addedItem.$(ADDED_TO_CART_PRODUCT_INCREMENT_BUTTON).click();
            }
        }
    }

    private ElementsCollection getAllAddedToCartProductsContainers() {
        return $$(ADDED_TO_CART_PRODUCT_MAIN_BOX);
    }
}
