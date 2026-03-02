package com.alchemy.pages;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import com.vkvideo.pages.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class AlchemyGamePage extends BasePage {

    private static final Duration LOAD_TIMEOUT = Duration.ofSeconds(20);
    private static final Duration DIALOG_TIMEOUT = Duration.ofSeconds(2);
    private static final long AD_MAX_WAIT_MS = 120_000;
    private static final long AD_POLL_INTERVAL_MS = 3_000;
    private static final long MIN_AD_DURATION_MS = 15_000;

    private static final String[] AD_CLOSE_IDS = {
        "com.ilyin.alchemy:id/bigo_ad_btn_close",
        "com.ilyin.alchemy:id/inter_btn_close",
        "com.google.android.gms:id/rewarded_ad_close_button",
        "com.google.android.gms:id/rewarded_ads_close_button",
        "com.google.android.gms:id/close_ad_button",
        "com.google.android.gms:id/skip_ad_button",
        "com.ilyin.alchemy:id/close_btn",
        "com.ilyin.alchemy:id/skip_btn",
        "com.ilyin.alchemy:id/video_close",
        "com.ilyin.alchemy:id/btn_close",
    };

    public boolean isLoaded() {
        try {
            mixButton().shouldBe(visible, LOAD_TIMEOUT);
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public void openHintMenu() {
        hintButton().shouldBe(visible, LOAD_TIMEOUT).click();
    }

    public void openHintMenuIfNeeded() {
        try {
            yourHintsLabel().shouldBe(visible, DIALOG_TIMEOUT);
        } catch (Throwable e) {
            try {
                hintButton().shouldBe(visible, DIALOG_TIMEOUT).click();
            } catch (Throwable ignored) {}
        }
    }

    public int getHintCount() {
        String text = hintCountInDialog().shouldBe(visible, LOAD_TIMEOUT).getText().trim();
        return Integer.parseInt(text);
    }

    public void clickWatchAdForHint() {
        watchButton().shouldBe(visible, LOAD_TIMEOUT).click();
    }

    public void waitForHintCountToChange(int initialCount) throws InterruptedException {
        long adStartTime = System.currentTimeMillis();
        long deadline = adStartTime + AD_MAX_WAIT_MS;
        while (System.currentTimeMillis() < deadline) {
            Thread.sleep(AD_POLL_INTERVAL_MS);
            dismissSystemDialogIfPresent();
            boolean adHasPlayed = (System.currentTimeMillis() - adStartTime) >= MIN_AD_DURATION_MS;
            tryCloseAd(adHasPlayed);

            try {
                openHintMenuIfNeeded();
                if (getHintCount() != initialCount) {
                    return;
                }
            } catch (Throwable ignored) {}
        }
    }

    private void dismissSystemDialogIfPresent() {
        try {
            $(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"android:id/ok\")"))
                    .shouldBe(visible, DIALOG_TIMEOUT)
                    .click();
        } catch (Throwable ignored) {}
    }

    private void tryCloseAd(boolean adHasPlayed) throws InterruptedException {
        for (String id : AD_CLOSE_IDS) {
            if (tapById(id)) {
                Thread.sleep(1000);
                for (String id2 : AD_CLOSE_IDS) {
                    if (tapById(id2)) {
                        Thread.sleep(500);
                        tapById(id2);
                        return;
                    }
                }
                return;
            }
        }
        if (adHasPlayed) {
            ((AndroidDriver) WebDriverRunner.getWebDriver())
                    .pressKey(new KeyEvent(AndroidKey.BACK));
        }
    }

    private boolean tapById(String resourceId) {
        List<WebElement> elements = WebDriverRunner.getWebDriver()
                .findElements(AppiumBy.id(resourceId));
        if (elements.isEmpty()) return false;
        try {
            WebElement el = elements.get(0);
            Point loc = el.getLocation();
            Dimension size = el.getSize();
            tapAt(loc.getX() + size.getWidth() / 2, loc.getY() + size.getHeight() / 2);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private void tapAt(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(100)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        ((AppiumDriver) WebDriverRunner.getWebDriver()).perform(List.of(tap));
    }

    private SelenideElement mixButton() {
        return $(AppiumBy.androidUIAutomator("new UiSelector().description(\"Mix!\")"));
    }

    private SelenideElement hintButton() {
        return $(AppiumBy.androidUIAutomator(
                "new UiSelector().enabled(true).clickable(true)" +
                ".childSelector(new UiSelector().className(\"android.widget.TextView\").textMatches(\"[0-9]+\"))"));
    }

    private SelenideElement yourHintsLabel() {
        return $(AppiumBy.androidUIAutomator("new UiSelector().text(\"Your hints\")"));
    }

    private SelenideElement hintCountInDialog() {
        return $(AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"Your hints\")" +
                ".fromParent(new UiSelector().className(\"android.widget.TextView\").textMatches(\"[0-9]+\"))"));
    }

    private SelenideElement watchButton() {
        return $(AppiumBy.androidUIAutomator(
                "new UiSelector().enabled(true).clickable(true)" +
                ".childSelector(new UiSelector().text(\"Watch\"))"));
    }

}
