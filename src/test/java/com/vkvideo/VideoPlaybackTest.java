package com.vkvideo;

import com.vkvideo.pages.MainPage;
import com.vkvideo.pages.VideoPlayerPage;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;


public class VideoPlaybackTest extends BaseTest {

    @Test(priority = 1)
    public void videoShouldPlay() {
        System.out.println("123 Running videShouldPlayTest");
        MainPage mainPage = new MainPage();
        Assert.assertTrue(mainPage.isLoaded(), "Main feed did not load");

        VideoPlayerPage player = mainPage.openFirstVideo().waitForPlayer();

        Assert.assertTrue(player.isPlaying(), "Video is not playing");
        Assert.assertFalse(player.hasError(), "Unexpected error on player");
    }

//    @Test(priority = 2)
//    public void videoProgressShouldAdvance() throws InterruptedException {
//        System.out.println("Running videoProgressShouldAdvance");
//        MainPage mainPage = new MainPage();
//        Assert.assertTrue(mainPage.isLoaded(), "Main feed did not load");
//
//        VideoPlayerPage player = mainPage.openFirstVideo().waitForPlayer();
//        Assert.assertTrue(player.isPlaying(), "Video did not start playing");
//        Assert.assertTrue(player.isProgressAdvancing(), "Video progress is not advancing");
//    }

    @Test(priority = 2)
    public void videoShouldFailGracefullyWithoutNetwork() throws InterruptedException {
        System.out.println("123 Running failt test");
        disableNetwork();
        Thread.sleep(1000);

        if (isWifiActuallyEnabled()) {
            enableNetwork();
            throw new SkipException("Cannot disable WiFi on this device — skipping offline test");
        }

        try {
            driver.terminateApp("com.vk.vkvideo");
            Thread.sleep(500);
            driver.activateApp("com.vk.vkvideo");

            MainPage mainPage = new MainPage();
            Assert.assertTrue(mainPage.isOfflineModeShown(), "App should show offline mode without network");
        } finally {
            enableNetwork();
        }
    }

    @AfterMethod
    public void restoreNetworkIfNeeded() {
        enableNetwork();
    }

    private boolean isWifiActuallyEnabled() {
        Object result = driver.executeScript("mobile: shell", java.util.Map.of("command", "dumpsys wifi | grep 'Wi-Fi is'"));
        return result != null && result.toString().contains("Wi-Fi is enabled");
    }

    private void disableNetwork() {
        if (driver.getConnection().isWiFiEnabled()) {
            driver.toggleWifi();
        }
    }

    private void enableNetwork() {
        if (!driver.getConnection().isWiFiEnabled()) {
            driver.toggleWifi();
        }
    }
}
