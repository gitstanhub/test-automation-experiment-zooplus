package com.zooplus.models;

import lombok.Data;

@Data
public class RecommendationCarouselItem {

    private String itemId;
    private double itemPrice;

    public RecommendationCarouselItem(String itemId, double itemPrice) {
        this.itemId = itemId;
        this.itemPrice = itemPrice;
    }
}
