//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Test;

/* Unit tests for the QuaxBoardController Class, which contains main and the basic
 * logic functions that determine how a game progresses, such as the getter and
 * setter methods, and ensuring players take their turns in the right order.
 * Note: This test class does not test the JavaFX side of the game, as we are only
 * looking at testing the controller logic.
 */
public class QuaxBoardControllerTest {

    /* Test to ensure the setMode() and getMode() functions works for HUMAN mode
     * Use of assertEquals to show functions work correctly and show same mode
     */
    @Test
    public void handleHumanTest() {
        QuaxBoardController controller = new QuaxBoardController();
        controller.setMode("HUMAN");

        Assert.assertEquals("HUMAN", controller.getMode());
    }

    /* Test to ensure the setMode() and getMode() functions works for BOT mode
     * Use of assertEquals to show functions work correctly and show same mode
     */
    @Test
    public void handleBotTest() {
        QuaxBoardController controller = new QuaxBoardController();
        controller.setMode("BOT");

        Assert.assertEquals("BOT", controller.getMode());
    }

    /* Test to show that when a new game is created, that player BLACK will go first
     * Use of a field to set it to what the boolean value of "blackTurn" is in QuaxBoardController
     * assertTrue to show that value for "blackTurn" is true when a new game is created
     */
    @Test
    public void blackPlayerGoesFirstTest() throws Exception {
        QuaxBoardController controller = new QuaxBoardController();

        Field field = QuaxBoardController.class.getDeclaredField("blackTurn");
        field.setAccessible(true);

        boolean value = (boolean) field.get(controller);
        Assert.assertTrue(value);
    }

    /* Test to show that when a new game is created, that the pie rule will appear after BLACK'S turn
     * Use of a field to set it to what the boolean value of "pieRuleAvailable" is in QuaxBoardController
     * assertTrue to show that value for "pieRuleAvailable" is true when a new game is created
     */
    @Test
    public void pieRuleAppearsTest() throws Exception{
        QuaxBoardController controller = new QuaxBoardController();

        Field field = QuaxBoardController.class.getDeclaredField("pieRuleAvailable");
        field.setAccessible(true);

        boolean value = (boolean) field.get(controller);
        Assert.assertTrue(value);
    }

    /* Test to show that when a new game is created, that whatever tile player BLACK chooses
     * for their first move, that it is null. This highlights that BLACK can pick from any tiles
     * when a new game is created.
     * Use of a field to set it to what the Object value of "blackFirstMove" is in QuaxBoardController
     * assertNull to show that value for "BlackFirstMove" is null when a new game is created, and that BLACK
     * can choose any tile when they start a game
     */
    @Test
    public void blackFirstMoveIsNullTest() throws Exception{
        QuaxBoardController controller = new QuaxBoardController();

        Field field = QuaxBoardController.class.getDeclaredField("blackFirstMove");
        field.setAccessible(true);

        Object value = field.get(controller);
        Assert.assertNull(value);
    }

    @Test
    public void checkChainTest(){

    }

    @Test
    public void checkWinTest(){

    }

    @Test
    public void updateTurnTest(){

    }

    @Test
    public void initialiseShapesTest(){

    }
}
