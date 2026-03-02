package com.alchemy;

import com.alchemy.config.AlchemyDriverConfig;
import com.codeborne.selenide.WebDriverRunner;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.Map;

public class AlchemyBaseTest {

    protected AndroidDriver driver;

    @BeforeClass
    public void setUp() throws InterruptedException {
        driver = AlchemyDriverConfig.createDriver();
        WebDriverRunner.setWebDriver(driver);
        driver.terminateApp("com.ilyin.alchemy");
        driver.executeScript("mobile: shell", Map.of("command", "pm clear com.ilyin.alchemy"));
        Thread.sleep(1000);
        driver.activateApp("com.ilyin.alchemy");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
