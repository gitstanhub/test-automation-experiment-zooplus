package com.zooplus.pageobjects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.zooplus.constants.commons.PriceSortingTypes;
import com.zooplus.models.RecommendationCarouselItem;
import com.zooplus.pageobjects.base.SelenidePage;
import com.zooplus.pageobjects.commons.CookiesPopup;
import io.qameta.allure.Step;

import java.util.*;
import java.util.stream.Collectors;

import static com.zooplus.constants.commons.RecommendationsCarouselConstants.EMPTY_CART_RECOMMENDATION_CAROUSEL_TITLE;
import static com.zooplus.locators.commons.RecommendationsCarouselLocators.*;

public class CartPage extends SelenidePage {

    CookiesPopup cookiesPopup = new CookiesPopup();

    @Step
    public CartPage openCartPage() {
        Selenide.open("https://www.zooplus.com/checkout/cart");
        cookiesPopup.handleCookiesPopup();
        return this;
    }

    public List<RecommendationCarouselItem> getItemsFromEmptyCartCarousel(int desiredItemsAmount, PriceSortingTypes sortBy) {

        List<RecommendationCarouselItem> carouselWithItems = getAllRecommendationCarouselsWithSortedItems().get(EMPTY_CART_RECOMMENDATION_CAROUSEL_TITLE);

       return switch(sortBy) {
            case HIGHEST -> getHighestPricedItems(carouselWithItems, desiredItemsAmount);
            case LOWEST -> getLowestPricedItems(carouselWithItems, desiredItemsAmount);
        };
    }

    private List<RecommendationCarouselItem> getHighestPricedItems(List<RecommendationCarouselItem> itemsList, int desiredItemsAmount) {
        return itemsList.stream()
                .limit(desiredItemsAmount)
                .collect(Collectors.toList());
    }

    private List<RecommendationCarouselItem> getLowestPricedItems(List<RecommendationCarouselItem> itemsList, int desiredItemsAmount) {
        return itemsList.stream()
                .sorted(Comparator.comparing(RecommendationCarouselItem::getItemPrice).reversed())
                .limit(desiredItemsAmount)
                .collect(Collectors.toList());
    }

    private Map<String, List<RecommendationCarouselItem>> getAllRecommendationCarouselsWithSortedItems() {
        Map<String, List<RecommendationCarouselItem>> allCarouselsWithSortedItems = new HashMap<>();

        ElementsCollection carousels = elementActions.findAllCarouselsOfSameType(RECOMMENDATIONS_CAROUSEL);

        for (SelenideElement carousel : carousels) {
            String rawCarouselTitle = carousel.parent().$(RECOMMENDATIONS_CAROUSEL_TITLE).getText();
            ElementsCollection rawCarouselItems = elementActions.findAllCarouselItems(carousel, RECOMMENDATIONS_CAROUSEL_ITEM);

            List<RecommendationCarouselItem> recommendationCarouselItems = new ArrayList<>();
            for (SelenideElement rawCarouselItem : rawCarouselItems) {
                String itemId = "";
                double itemPrice = 4.55;

                recommendationCarouselItems.add(new RecommendationCarouselItem(itemId, itemPrice));
            }

            recommendationCarouselItems.sort(Comparator.comparing(RecommendationCarouselItem::getItemPrice));

            allCarouselsWithSortedItems.put(rawCarouselTitle, recommendationCarouselItems);
        }

        return allCarouselsWithSortedItems;
    }

}
