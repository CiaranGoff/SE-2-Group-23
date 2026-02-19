package org.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class QuaxBoardControllerTest {

    @Test
    public void setModeHumanTest(){
        QuaxBoardController controller = new QuaxBoardController();
        controller.setMode("HUMAN");

        assertEquals("HUMAN", controller.getMode());
    }

    @Test
    public void setModeBotTest(){
        QuaxBoardController controller = new QuaxBoardController();
        controller.setMode("BOT");

       assertEquals("BOT", controller.getMode());
    }

}
