package com.vkvideo.config;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverConfig {

    private static final String APPIUM_SERVER = "http://127.0.0.1:4723";
    private static final String APP_PACKAGE = "com.vk.vkvideo";
    private static final String APP_ACTIVITY = "com.vk.video.screens.main.MainActivity";

    public static AndroidDriver createDriver() {
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName("Pixel_5_Play")
                .setPlatformVersion("11")
                .setAppPackage(APP_PACKAGE)
                .setAppActivity(APP_ACTIVITY)
                .setNoReset(true)
                .setAutoGrantPermissions(true)
                .setNewCommandTimeout(Duration.ofSeconds(60));

        try {
            return new AndroidDriver(new URL(APPIUM_SERVER), options);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
