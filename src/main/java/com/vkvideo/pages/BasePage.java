package com.vkvideo.pages;

import com.codeborne.selenide.SelenideElement;
import io.appium.java_client.AppiumBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage {

    protected SelenideElement byId(String resourceId) {
        return $(AppiumBy.id(resourceId));
    }

    protected SelenideElement byUiAutomator(String selector) {
        return $(AppiumBy.androidUIAutomator(selector));
    }

    protected boolean isVisible(String resourceId, Duration timeout) {
        try {
            byId(resourceId).shouldBe(visible, timeout);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }
}
