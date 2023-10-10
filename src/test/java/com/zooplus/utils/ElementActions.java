package com.zooplus.utils;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;

public class ElementActions {

    public ElementsCollection findAllCarouselsOfSameType(String carouselLocator) {
        return $$(carouselLocator);
    }

    public ElementsCollection findAllCarouselItems(SelenideElement carousel, String carouselItemLocator) {
        return carousel.$$(carouselItemLocator);
    }
}
