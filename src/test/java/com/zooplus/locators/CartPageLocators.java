package com.zooplus.locators;

public class CartPageLocators {

    // Added to cart product
    public static final String ADDED_TO_CART_PRODUCT_INFO = "[data-zta='productInfo']";
    public static final String ADDED_TO_CART_PRODUCT_MAIN_BOX = "[data-zta='standard_article']";
    public static final String ADDED_TO_CART_PRODUCT_NAME = "[data-zta='productName']";
    public static final String ADDED_TO_CART_PRODUCT_PRICE = "div[data-zta='articlePrice'] div[class='z-price__price-wrap']";
    public static final String ADDED_TO_CART_PRODUCT_INCREMENT_BUTTON = "[data-zta='quantityStepperIncrementButton']";
    public static final String ADDED_TO_CART_PRODUCT_REMOVE_BUTTON = "button[data-zta='quantityStepperDecrementButton']";
    public static final String ADDED_TO_CART_PRODUCT_QUANTITY_FIELD = "input[data-zta='quantityStepperInput']";
    public static final String ADDED_TO_CART_PRODUCT_SUBTOTAL_PRICE = "div[data-zta='articleQuantitySubtotal']";

    // Cart summary
    public static final String CART_SUMMARY_SECTION = "div[id='cartSummary']";
    public static final String CART_SUBTOTAL_PRICE = "div[id='cartSummary'] p[data-zta='overviewSubTotalValue']";
    public static final String CART_SHIPPING_FEE = "div[id='cartSummary'] p[data-zta='shippingCostValueOverview']";
    public static final String CART_TOTAL_PRICE = "div[id='cartSummary'] div [data-zta='total__price__value']";
    public static final String CART_SHIPPING_COUNTRY_SELECTED = "a[data-zta='shippingCountryName']";
    public static final String CART_SHIPPING_COUNTRY_DROPDOWN_BUTTON = "button[data-zta='dropdownMenuTriggerButton']";
    public static final String CART_SHIPPING_COUNTRY_DROPDOWN_LIST_ITEM = "div[data-zta='dropdownMenuMenu'] ul button li p";
    public static final String CART_SHIPPING_COUNTRY_POSTCODE_FIELD = "div[data-zta='shippingCostZipcode'] input";
    public static final String CART_SHIPPING_COUNTRY_UPDATE_BUTTON = "button[data-zta='shippingCostPopoverAction']";
    public static final String CART_COUPON_CODE_BUTTON = "a[data-zta='enterCouponBtn']";
    public static final String CART_COUPON_CODE_FIELD = "input[data-zta='couponCode']";
    public static final String CART_COUPON_REDEEM_BUTTON = "button[data-zta='redeemCode']";
    public static final String CART_COUPON_INLINE_MESSAGE = "span[data-zta='inlineCouponMessage'] p";
    public static final String CART_PROCEED_BUTTON = "button[data-zta='gotoPreviewBottom']";

    // Cart notifications
    public static final String CART_ERROR_MESSAGE = "div[data-zta='cartMessage-error'] div[data-zta='alertText'] p";
    public static final String CART_ALERT_MESSAGE = "div[data-zta='alertText'] p";
    public static final String UNDO_LAST_PRODUCT_REMOVAL_BUTTON = "div[data-zta='alertText'] a[data-zta='reAddArticleBtn']";
}
