package com.vkvideo.pages;

import com.codeborne.selenide.ElementsCollection;
import io.appium.java_client.AppiumBy;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MainPage extends BasePage {

    private static final String VIDEO_ITEM_ID    = "com.vk.vkvideo:id/content";
    private static final String SKIP_BUTTON_ID  = "com.vk.vkvideo:id/fast_login_tertiary_btn";
    private static final String OFFLINE_TITLE_ID = "com.vk.vkvideo:id/placeholder_view_title";
    private static final String TAB_MAIN_ID     = "com.vk.vkvideo:id/tab_main";

    public boolean isLoaded() {
        long deadline = System.currentTimeMillis() + 20_000;
        while (System.currentTimeMillis() < deadline) {
            if (isVisible(VIDEO_ITEM_ID, Duration.ofSeconds(1))) {
                return true;
            }
            if (isVisible(SKIP_BUTTON_ID, Duration.ofSeconds(1))) {
                try {
                    $(AppiumBy.id(SKIP_BUTTON_ID)).click();
                } catch (Throwable ignored) {}
            }
        }
        return false;
    }

    public boolean isOfflineModeShown() {
        try {
            $(AppiumBy.id(TAB_MAIN_ID)).click();
        } catch (Throwable ignored) {}
        return isVisible(OFFLINE_TITLE_ID, Duration.ofSeconds(10));
    }

    public VideoPlayerPage openFirstVideo() {
        ElementsCollection items = $$(AppiumBy.id(VIDEO_ITEM_ID));
        items.first().shouldBe(visible, Duration.ofSeconds(15)).click();
        return new VideoPlayerPage();
    }
}
