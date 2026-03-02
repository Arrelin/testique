package com.alchemy;

import com.alchemy.pages.AlchemyGamePage;
import com.alchemy.pages.AlchemyMainMenuPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AlchemyHintTest extends AlchemyBaseTest {

    @Test
    public void hintCountShouldIncreaseBy2AfterWatchingAd() throws InterruptedException {
        System.out.println("Running aclhemyTest");
        AlchemyMainMenuPage mainMenu = new AlchemyMainMenuPage();
        Assert.assertTrue(mainMenu.isLoaded(), "Main menu did not load");

        AlchemyGamePage game = mainMenu.clickPlay();
        Assert.assertTrue(game.isLoaded(), "Game screen did not load");

        game.openHintMenu();
        int initialCount = game.getHintCount();

        game.clickWatchAdForHint();
        game.waitForHintCountToChange(initialCount);

        int finalCount = game.getHintCount();

        Assert.assertTrue(finalCount > initialCount,
                "Hint count should have increased, but was " + initialCount + " before and " + finalCount + " after");
        Assert.assertEquals(finalCount, 4,
                "Hint count should be 4 after watching ad, but was " + finalCount);
    }
}
