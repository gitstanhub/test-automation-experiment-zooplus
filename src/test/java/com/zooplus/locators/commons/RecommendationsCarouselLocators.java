package com.zooplus.locators.commons;

public class RecommendationsCarouselLocators {

    public static final String RECOMMENDATIONS_CAROUSEL = "div[aria-label='cart.recommendations.slider.ariaLabel']";
    public static final String RECOMMENDATIONS_CAROUSEL_TITLE = "div[data-zta='zootopiaBoxEmpty'] h2";
    public static final String RECOMMENDATIONS_CAROUSEL_ITEM = "div[data-testid='recommendation-item']";
    public static final String RECOMMENDATIONS_CAROUSEL_ITEM_WRAPPER = "li:has(div[data-testid='recommendation-item'])";
    public static final String RECOMMENDATIONS_CAROUSEL_ITEM_LINK = "a";
    public static final String RECOMMENDATIONS_CAROUSEL_ITEM_PRICE = "a div[class='z-price__price-wrap'] span";
    public static final String RECOMMENDATIONS_CAROUSEL_ITEM_ADD_BUTTON = "[data-testid='recommendation-item'] button";
    public static final String RECOMMENDATIONS_CAROUSEL_SPECIFIC_ITEM_ADD_BUTTON = "a[title='%s'] ~ button";
    public static final String RECOMMENDATION_CAROUSEL_NEXT_SLIDE_BUTTON = "button[aria-label='Next slide']";
}
