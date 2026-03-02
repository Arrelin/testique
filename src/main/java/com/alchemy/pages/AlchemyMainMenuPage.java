package com.alchemy.pages;

import com.codeborne.selenide.SelenideElement;
import com.vkvideo.pages.BasePage;
import io.appium.java_client.AppiumBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class AlchemyMainMenuPage extends BasePage {

    private static final Duration LOAD_TIMEOUT = Duration.ofSeconds(15);

    public boolean isLoaded() {
        try {
            playButton().shouldBe(visible, LOAD_TIMEOUT);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public AlchemyGamePage clickPlay() {
        playButton().shouldBe(visible, LOAD_TIMEOUT).click();
        return new AlchemyGamePage();
    }

    private SelenideElement playButton() {
        return $(AppiumBy.androidUIAutomator("new UiSelector().clickable(true).childSelector(new UiSelector().text(\"Play\"))"));
    }
}
