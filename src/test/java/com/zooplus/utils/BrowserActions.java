package com.zooplus.utils;

import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Assertions;

public class BrowserActions {

    public void verifyUrlContains(String expectedText) {
        String currentUrl = WebDriverRunner.url();

        Assertions.assertTrue(currentUrl.contains(expectedText));
    }
}
