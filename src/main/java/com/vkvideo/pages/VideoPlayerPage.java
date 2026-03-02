package com.vkvideo.pages;

import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.time.Duration;
import java.util.Arrays;

import static com.codeborne.selenide.Condition.visible;

public class VideoPlayerPage extends BasePage {

    private static final String PLAYER_CONTAINER_ID = "com.vk.vkvideo:id/playerContainer";
    private static final String PLAY_BUTTON_ID      = "com.vk.vkvideo:id/video_play_button";
    private static final String SNACKBAR_ID         = "com.vk.vkvideo:id/vk_video_snackbar_container";

    public VideoPlayerPage waitForPlayer() {
        byId(PLAYER_CONTAINER_ID).shouldBe(visible, Duration.ofSeconds(15));
        if (isVisible(PLAY_BUTTON_ID, Duration.ofSeconds(3))) {
            try { byId(PLAY_BUTTON_ID).click(); } catch (Throwable ignored) {}
        }
        return this;
    }

    public boolean isPlaying() {
        try {
            String desc = byId(PLAY_BUTTON_ID).getAttribute("content-desc");
            return !"Play".equals(desc) && !isSnackbarShowing();
        } catch (Throwable e) {
            return !isSnackbarShowing();
        }
    }

    public boolean hasError() {
        return isSnackbarShowing();
    }

    public boolean isProgressAdvancing() throws InterruptedException {
        Thread.sleep(5000);
        byte[] before = ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
        Thread.sleep(3000);
        byte[] after = ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES);
        return !Arrays.equals(before, after);
    }

    private boolean isSnackbarShowing() {
        try {
            String bounds = byId(SNACKBAR_ID).getAttribute("bounds");
            return bounds != null && !bounds.equals("[0,0][0,0]");
        } catch (Throwable e) {
            return false;
        }
    }
}
