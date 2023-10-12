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
        getRecommendationCarouselItem().shouldBe(Condition.visible, Duration.ofMillis(5000));
        getCarousel(ACTIVE_CART_SECOND_RECOMMENDATION_CAROUSEL_TITLE).scrollIntoView("{behavior: \"smooth\"}");
        getCarousel(ACTIVE_CART_SECOND_RECOMMENDATION_CAROUSEL_TITLE).shouldBe(Condition.visible, Duration.ofMillis(5000));

        for (int i = 0; i <= productsToAdd; i++) {
            getProductRemoveButton().shouldBe(Condition.visible, Duration.ofMillis(5000));
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

    public void verifySubTotalPricePerProduct() {
        List<ProductItem> addedToCartProducts = getAllAddedToCartProductsWithCountAndSubtotal();

        for (ProductItem addedToCartProduct : addedToCartProducts) {
            double expectedSubtotalPrice = addedToCartProduct.getItemPrice() * addedToCartProduct.getItemSubtotal().getItemCount();
            double actualSubtotalPrice = addedToCartProduct.getItemSubtotal().getSubtotalPrice();
            Assertions.assertEquals(expectedSubtotalPrice, actualSubtotalPrice);
        }
    }

    public void verifySubtotalPriceForCart() {
        List<ProductItem> addedToCartProducts = getAllAddedToCartProductsWithCountAndSubtotal();

        double addedProductsSubtotalSum = addedToCartProducts.stream()
                .map(ProductItem::getItemSubtotal)
                .filter(Objects::nonNull)
                .mapToDouble(ProductItem.ItemSubtotal::getSubtotalPrice)
                .sum();

        double addedProductsSubtotalSumFormatted = (int)(addedProductsSubtotalSum * 100) / 100.0;

        double actualCartSubtotal = Double.parseDouble(getCartSubtotal().getText().replaceAll("[^0-9.]", ""));

        Assertions.assertEquals(addedProductsSubtotalSumFormatted, actualCartSubtotal);
    }

    public void increaseLowestPricedProductCountByOne(int productsToIncrease) {
        List<ProductItem> addedProducts = getAllAddedToCartProducts();
        List<ProductItem> lowestPricedProducts = getLowestPricedItems(addedProducts, productsToIncrease);

        for (ProductItem lowestPricedProduct : lowestPricedProducts) {
            clickIncrementButtonForProduct(lowestPricedProduct.getItemId());
        }

    }

    public void deleteHighestPricedProduct(int productsToDelete) {
        getAddedToCartItem().scrollIntoView("{behavior: \"smooth\"}");
        getAddedToCartItem().shouldBe(Condition.visible, Duration.ofMillis(5000));
        getRecommendationCarouselItem().shouldBe(Condition.visible, Duration.ofMillis(5000));

        List<ProductItem> addedProducts = getAllAddedToCartProducts();
        List<ProductItem> highestPricedProducts = getHighestPricedItems(addedProducts, productsToDelete);

        for (ProductItem highestPricedProduct : highestPricedProducts) {
            getProductRemoveButton().shouldBe(Condition.visible, Duration.ofMillis(5000));
            clickDeleteButtonForProduct(highestPricedProduct.getItemId());
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

    public List<ProductItem> getAllAddedToCartProductsWithCountAndSubtotal() {
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
            int itemCount = Integer.parseInt(addedItem.$(ADDED_TO_CART_PRODUCT_COUNTER).getAttribute("value"));
            double initialProductSubtotal = Double.parseDouble(addedItem.$(ADDED_TO_CART_PRODUCT_SUBTOTAL_PRICE).getText().replaceAll("[^0-9.]", ""));
            double initialProductSubtotalFormatted = (int)(initialProductSubtotal * 100) / 100.0;

            if (actualProductName.contains(expectedProductName)) {
                addedItem.$(ADDED_TO_CART_PRODUCT_INCREMENT_BUTTON).click();
                addedItem.$(ADDED_TO_CART_PRODUCT_COUNTER).shouldHave(Condition.attribute("value", String.valueOf(itemCount+1)));
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
        return $$(ADDED_TO_CART_PRODUCT_MAIN_BOX);
    }

    private SelenideElement getCartSubtotal(){
        return $(CART_SUBTOTAL_PRICE);
    }

    private SelenideElement getRecommendationCarouselItem() {
        return $(RECOMMENDATIONS_CAROUSEL_ITEM);
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
}
