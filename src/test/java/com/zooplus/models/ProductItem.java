package com.zooplus.models;

import lombok.Data;

@Data
public class ProductItem {

    private String itemId;
    private double itemPrice;
    private ItemSubtotal itemSubtotal;

    public ProductItem(String itemId, double itemPrice) {
        this.itemId = itemId;
        this.itemPrice = itemPrice;
    }

    @Data
    public static class ItemSubtotal {
        private int itemCount;
        private double subtotalPrice;

        public ItemSubtotal(int itemCount, double subtotalPrice) {
            this.itemCount = itemCount;
            this.subtotalPrice = subtotalPrice;
        }
    }
}
