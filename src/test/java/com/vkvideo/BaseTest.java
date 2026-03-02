package com.vkvideo;

import com.codeborne.selenide.WebDriverRunner;
import com.vkvideo.config.DriverConfig;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    protected AndroidDriver driver;

    @BeforeClass
    public void setUp() {
        driver = DriverConfig.createDriver();
        WebDriverRunner.setWebDriver(driver);
        driver.activateApp("com.vk.vkvideo");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
