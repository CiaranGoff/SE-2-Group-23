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

    /* Three helper functions used to help access and change private variables contained
     * within QuaxBoardController.java. We also have an initialised controller method which
     * saves having to call controller.initialize in every test function
     */
    private Object getPrivateField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private QuaxBoardController newInitialisedController(){
        QuaxBoardController controller = new QuaxBoardController();
        controller.initialize();
        return controller;
    }

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

    /* Test to ensure that findTileById returns the correct information for a specified Octagon Tile
     * use of assertNotNull and assertEquals
     */
    @Test
    public void findTileByIdReturnsCorrectOctagonTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile tile = controller.findTileById("OctCell11A");

        Assert.assertNotNull(tile);
        Assert.assertEquals(Tile.TileType.OCTAGON, tile.getType());
        Assert.assertEquals(1, tile.getTileRow());
        Assert.assertEquals(1, tile.getTileCol());
    }

    /* Test to ensure that findTileById returns the correct information for a specified Rhombus Tile
     * use of assertNotNull and assertEquals
     */
    @Test
    public void findTileByIdReturnsCorrectRhombusTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile tile = controller.findTileById("RhoCell1");

        Assert.assertNotNull(tile);
        Assert.assertEquals(Tile.TileType.RHOMBUS, tile.getType());
        Assert.assertEquals(1, tile.getTileRow());
        Assert.assertEquals(1, tile.getTileCol());
    }

    /* Test to show that findTileById returns null for a non-existent tile, by assertNull */
    @Test
    public void findTileIdReturnsNullTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile tile = controller.findTileById("Tile doesn't exist");

        Assert.assertNull(tile);
    }

    /* Tests to highlight that tiles contain correct amount of neighbours depending on their
     * position within the board: corner pieces have 3 neighbours, side pieces have 7 neighbours,
     * and all other pieces have 8 neighbours.
     * Use of assertNotNull and assertTrue to show these tests pass
     */
    @Test
    public void octagonNeighbourLinkingTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile corner = controller.findTileById("OctCell1A");
        Tile middle = controller.findTileById("OctCell6F");

        Assert.assertNotNull(corner);
        Assert.assertNotNull(middle);
        Assert.assertTrue(corner.getNeighbours().size() >= 3);
        Assert.assertTrue(middle.getNeighbours().size() >= 8);
    }

    /* Test to show that check chain returns null when a game is created, as the board is empty, and contains
     * no tiles for which function can actually check.
     * Note checkChain function is made protected in QuaxBoardController.java to allow method to be accessed
     * Use of assertFalse
     */
    @Test
    public void checkChainReturnsNullBeginningOfGameTest(){
        QuaxBoardController controller = newInitialisedController();
        boolean result = controller.checkChain(null, Tile.TileColor.BLACK, new boolean[11][11], new boolean[10][10]);

        Assert.assertFalse(result);
    }

    /* Test to highlight that checkChain finds a winning path for Black correctly. Simple use of a horizontal
     * Black line for this example, as getNeighbours has been tested, which allows this method to work for
     * lines of any direction and size, as long as it reaches one end from the other.
     * Use of assertTrue
     */
    @Test
    public void checkChainFindsBlackWinningPathTest(){
        QuaxBoardController controller = newInitialisedController();
        for(int row = 11; row > 0; row--){
            controller.findTileById("OctCell" + row + "A").setColor(Tile.TileColor.BLACK);
        }
        Tile start = controller.findTileById("OctCell11A");
        boolean result = controller.checkChain(start, Tile.TileColor.BLACK, new boolean[11][11], new boolean[10][10]);

        Assert.assertTrue(result);
    }

    /* Test to highlight that checkChain finds a winning path for White correctly. Simple use of a vertical
     * White line for this example, as getNeighbours has been tested, which allows this method to work for
     * lines of any direction and size, as long as it reaches one end from the other.
     * Use of assertTrue
     */
    @Test
    public void checkChainFindsWhiteWinningPathTest(){
        QuaxBoardController controller = newInitialisedController();
        for(char col = 'A'; col < 'L'; col++){
            controller.findTileById("OctCell11" + col).setColor(Tile.TileColor.WHITE);
        }
        Tile start = controller.findTileById("OctCell11A");
        boolean result = controller.checkChain(start,  Tile.TileColor.WHITE, new boolean[11][11], new boolean[10][10]);

        Assert.assertTrue(result);
    }

    /* Test to show that checkWin returns false when a new game is started, as no tiles have been placed yet
     * Note: checkWin has been made a protected method to allow access for testing if method works as intended
     * use of assertFalse to show that neither player wins when a game is started
     */
    @Test
    public void checkWinReturnsFalseBeginningOfGameTest(){
        QuaxBoardController controller = newInitialisedController();

        Assert.assertFalse(controller.checkWin(Tile.TileColor.BLACK));
        Assert.assertFalse(controller.checkWin(Tile.TileColor.WHITE));
    }

    /* Simple test to show that checkWin returns false for edge cases, this being only one tile placed on the board
     * Use of assertFalse for this test
     */
    @Test
    public void checkWinReturnsFalseEdgeCaseBeginningTest(){
        QuaxBoardController controller = newInitialisedController();
        controller.findTileById("OctCell11A").setColor(Tile.TileColor.BLACK);

        Assert.assertFalse(controller.checkWin(Tile.TileColor.BLACK));
    }

    /* Test to show CheckWin returns true when Black has a winning chain, simple example of a Black
     * horizontal chain used: use of assertTrue
     */
    @Test
    public void checkWinReturnsTrueForBlackTest(){
        QuaxBoardController controller = newInitialisedController();
        for(int row = 11; row > 0; row--){
            controller.findTileById("OctCell" + row + "A").setColor(Tile.TileColor.BLACK);
        }

        Assert.assertTrue(controller.checkWin(Tile.TileColor.BLACK));
    }

    /* Test to show CheckWin returns true when White has a winning chain, simple example of a White
     * vertical chain used: use of assertTrue
     */
    @Test
    public void checkWinReturnsTrueForWhiteTest(){
        QuaxBoardController controller = newInitialisedController();
        for(char col = 'A'; col < 'L'; col++){
            controller.findTileById("OctCell11" + col ).setColor(Tile.TileColor.WHITE);
        }

        Assert.assertTrue(controller.checkWin(Tile.TileColor.WHITE));
    }

    /* Test to show CheckWin returns false when Black does not have a winning chain,
     * simple example of a Black vertical chain used: use of assertFalse
     */
    @Test
    public void checkWinReturnsFalseForBlackVerticalTest(){
        QuaxBoardController controller = newInitialisedController();
        for(char col = 'A'; col < 'L'; col++){
            controller.findTileById("OctCell11" + col).setColor(Tile.TileColor.BLACK);
        }

        Assert.assertFalse(controller.checkWin(Tile.TileColor.BLACK));
    }

    /* Test to show CheckWin returns false when White does not have a winning chain,
     * simple example of a White horizontal chain used: use of assertFalse
     */
    @Test
    public void checkWinReturnsFalseForWhiteHorizontalTest(){
        QuaxBoardController controller = newInitialisedController();
        for(int row = 11; row > 0; row--){
            controller.findTileById("OctCell" + row + "A").setColor(Tile.TileColor.WHITE);
        }

        Assert.assertFalse(controller.checkWin(Tile.TileColor.WHITE));
    }

    /* Test to show CheckWin returns false when Black does not have a winning chain; instead a broken chain
     * simple example of a Black horizontal chain used, with one tile missing: use of assertFalse
     */
    @Test
    public void checkWinReturnsFalseBrokenBlackChainTest(){
        QuaxBoardController controller = newInitialisedController();
        for(int row = 11; row > 0; row--){
            if(row != 3){
                controller.findTileById("OctCell" + row + "A").setColor(Tile.TileColor.BLACK);
            }
        }

        Assert.assertFalse(controller.checkWin(Tile.TileColor.BLACK));
    }

    /* Test to show CheckWin returns false when White does not have a winning chain; instead a broken chain
     * simple example of a White vertical chain used, with one tile missing: use of assertFalse
     */
    @Test
    public void checkWinReturnsFalseBrokenWhiteChainTest(){
        QuaxBoardController controller = newInitialisedController();
        for(char col = 'A'; col < 'L'; col++){
            if(col != 'E'){
                controller.findTileById("OctCell11" + col).setColor(Tile.TileColor.WHITE);
            }
        }

        Assert.assertFalse(controller.checkWin(Tile.TileColor.WHITE));
    }

    /* Test to show that initialiseShapes function works as intended, and creates a physical
     * representation of the board, with each tile assigned its correct shape and ID.
     * Use of assertNotNull to ensure tiles are not empty, along with assertEquals
     * to show tiles are assigned the correct ID
     */
    @Test
    public void initialiseShapesTest() throws Exception{
        QuaxBoardController controller = new QuaxBoardController();
        controller.initialize();

        Tile[][] octagons = (Tile[][]) getPrivateField(controller, "octagons");
        Tile[][] rhombuses = (Tile[][]) getPrivateField(controller, "rhombuses");

        Assert.assertNotNull(octagons[0][0]);
        Assert.assertNotNull(rhombuses[0][0]);
        Assert.assertNotNull(octagons[10][10]);
        Assert.assertNotNull(rhombuses[9][9]);

        Assert.assertEquals("OctCell11A", octagons[0][0].getId());
        Assert.assertEquals("OctCell1K", octagons[10][10].getId());
        Assert.assertEquals("RhoCell1", rhombuses[0][0].getId());
        Assert.assertEquals("RhoCell100", rhombuses[9][9].getId());
    }

    @Test
    public void resetGameResetsStateTest() throws Exception {
        QuaxBoardController controller = new QuaxBoardController();
        controller.initialize();

        Tile[][] octagons = (Tile[][]) getPrivateField(controller, "octagons");
        Tile[][] rhombuses = (Tile[][]) getPrivateField(controller, "rhombuses");

        octagons[0][0].setColor(Tile.TileColor.BLACK);
        octagons[5][5].setColor(Tile.TileColor.WHITE);
        rhombuses[0][0].setColor(Tile.TileColor.BLACK);

        setPrivateField(controller, "blackTurn", false);
        setPrivateField(controller, "blackFirstMove", octagons[0][0]);
        setPrivateField(controller, "pieRuleAvailable", false);

        try{
            controller.resetGame();
        } catch(Exception e){
            // Here ignore any errors that could occur as a scene is not attached,
            // Only looking at testing internal game state and logic works
        }

        Assert.assertTrue((boolean) getPrivateField(controller, "blackTurn"));
        Assert.assertNull(getPrivateField(controller, "blackFirstMove"));
        Assert.assertTrue((boolean) getPrivateField(controller, "pieRuleAvailable"));
    }
}
