package com.zooplus.pageobjects.commons;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ElementNotFound;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.zooplus.locators.commons.CookiesPopupLocators.COOKIES_ACCEPT_BUTTON;
import static com.zooplus.locators.commons.CookiesPopupLocators.COOKIES_POPUP;


public class CookiesPopup {

    @Step
    public CookiesPopup handleCookiesPopup() {

        try {
            findCookiesPopup().shouldBe(Condition.visible, Duration.ofMillis(2000));
            findCookiesAcceptButton().click();
            findCookiesPopup().shouldBe(Condition.visible, Duration.ofMillis(2000));
        } catch (ElementNotFound e) {
//            log.info("No cookies popup was found. Proceeding further...");
        }

        return this;
    }

    private SelenideElement findCookiesPopup() {
        return $(COOKIES_POPUP);
    }

    private SelenideElement findCookiesAcceptButton() {
        return $(COOKIES_ACCEPT_BUTTON);
    }
}
