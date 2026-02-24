//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Test;

public class QuaxBoardControllerTest {
    @Test
    public void handleHumanTest() {
        QuaxBoardController controller = new QuaxBoardController();
        controller.setMode("HUMAN");

        try {
            Field field = QuaxBoardController.class.getDeclaredField("mode");
            field.setAccessible(true);
            String modeValue = (String)field.get(controller);
            Assert.assertEquals("HUMAN", modeValue);
        } catch (Exception var4) {
            System.out.println("Failed to access the private mode field to set mode to Human");
        }

    }

    @Test
    public void handleBotTest() {
        QuaxBoardController controller = new QuaxBoardController();
        controller.setMode("BOT");

        try {
            Field field = QuaxBoardController.class.getDeclaredField("mode");
            field.setAccessible(true);
            String modeValue = (String)field.get(controller);
            Assert.assertEquals("BOT", modeValue);
        } catch (Exception var4) {
            System.out.println("Failed to access the private mode field to set mode to Bot");
        }

    }
}
