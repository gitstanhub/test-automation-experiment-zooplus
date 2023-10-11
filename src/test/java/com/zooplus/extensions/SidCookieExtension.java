package com.zooplus.extensions;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.impl.Randomizer;
import com.zooplus.annotations.SidCookie;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.Cookie;

import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

public class SidCookieExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        SidCookie annotation = context.getRequiredTestMethod().getAnnotation(SidCookie.class);

        if (annotation != null) {
            Selenide.open("https://www.zooplus.com/");
            String sidCookieValue = annotation.sidCookieValue();
            handleSidCookie(sidCookieValue);
        }
    }

    private void handleSidCookie(String sidCookieValue) {
        if (sidCookieValue != null) {

            String randomString = RandomString.make();
            String tempCookieValue = sidCookieValue + "-" + randomString;


                    WebDriverRunner.getWebDriver().manage().deleteCookieNamed("sid");

            Cookie newCookie = new Cookie.Builder("sid", tempCookieValue)
                    .domain("www.zooplus.com")
                    .path("/")
                    .isHttpOnly(true)
                    .build();

            WebDriverRunner.getWebDriver().manage().addCookie(newCookie);
        }
    }
}

