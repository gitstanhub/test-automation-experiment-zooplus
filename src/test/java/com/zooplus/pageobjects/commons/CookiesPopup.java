package com.zooplus.pageobjects.commons;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.zooplus.locators.commons.CookiesPopupLocators.COOKIES_ACCEPT_BUTTON;
import static com.zooplus.locators.commons.CookiesPopupLocators.COOKIES_POPUP;

@Slf4j
public class CookiesPopup {

    @Step
    public CookiesPopup handleCookiesPopup() {

        try {
            getCookiesPopup().shouldBe(Condition.visible, Duration.ofMillis(5000));
            getCookiesAcceptButton().click();
        } catch (ElementNotFound e) {
            log.info("No cookies popup was found. Proceeding further...");
        }

        return this;
    }

    private SelenideElement getCookiesPopup() {
        return $(COOKIES_POPUP);
    }

    private SelenideElement getCookiesAcceptButton() {
        return $(COOKIES_ACCEPT_BUTTON);
    }
}
