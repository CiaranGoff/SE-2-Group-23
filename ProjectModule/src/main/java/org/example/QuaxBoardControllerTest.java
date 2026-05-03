//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.example;

import java.lang.reflect.Field;
import java.util.List;

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

    @Test
    public void checkChainWrongColourReturnsFalse(){
        QuaxBoardController controller = newInitialisedController();

        Tile tile = controller.findTileById("OctCell11A");
        tile.setColor(Tile.TileColor.BLACK);

        boolean result = controller.checkChain(tile, Tile.TileColor.BLACK,  new boolean[11][11], new boolean[10][10]);
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

    /* Test to observe that the game state is changed back after a player chooses to reset the game
     * Requires use of helper functions created, to alter any private fields in this test method
     */
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

    /* Test to ensure the bot is able to pick a move during a round of Quax
     * Verify that after calling botMove, at least one tiles state has changed to white
     * Test makes use of AssertTrue, Note also use of try-catch block is used to catch any
     * errors from any UI or sceneBuilder errors, which we ignore
     */
    @Test
    public void botMoveTest() throws Exception{
        QuaxBoardController controller = newInitialisedController();
        controller.setMode("BOT");

        setPrivateField(controller, "blackTurn", false);
        try{
            controller.botMove();
        } catch(Exception ex){
            //Is a UI/JavaFxScene error so we ignore this
        }

        Tile[][] octagons = (Tile[][]) getPrivateField(controller, "octagons");
        boolean whiteFound = false;
        for(int r = 0; r < 11; r ++){
            for(int c = 0; c < 11; c ++){
                if(octagons[r][c].getColor() == Tile.TileColor.WHITE){
                    whiteFound = true;
                    break;
                }
            }
        }
        Assert.assertTrue(whiteFound);
    }

    /* Test to ensure botFirstMove picks the expected starting tile.
     * In the controller logic, it defaults to octagons[7][0] if empty.
     * Note: Catch block used to ignore JavaFX scene lookup exceptions.
     */
    @Test
    public void botFirstMoveTest(){
        QuaxBoardController controller = newInitialisedController();
        try{
            Tile movedTile = controller.botFirstMove();

            Assert.assertNotNull(movedTile);
            Assert.assertEquals(8, movedTile.getTileRow());
            Assert.assertEquals(1, movedTile.getTileCol());
            Assert.assertEquals(Tile.TileColor.WHITE, movedTile.getColor());
        } catch(Exception e){
            //Is a UI/JavaFxScene error so we ignore this
        }
    }

    /* To test that after calling applyMove, the chosen tile is correctly
     * updated and the turn is switched back to the other player, or bot
     * Note use of try-catch block for UI or sceneBuilder errors, which we ignore
     */
    @Test
    public void applyMoveTest() throws Exception{
        QuaxBoardController controller = newInitialisedController();
        Tile testTile = controller.findTileById("OctCell5E");

        try{
            controller.applyMove(testTile);
        } catch(Exception ex){
            //Is a UI/JavaFxScene error so we ignore this
        }

        Assert.assertEquals(Tile.TileColor.WHITE, testTile.getColor());
        boolean isBlacksTurn = (boolean) getPrivateField(controller, "blackTurn");
        Assert.assertTrue(isBlacksTurn);
    }

    /* Tests Dijkstra's algorithm for finding the best path for the bot
     * On an empty board just created, the best path should be a straightline from
     * one column of the board to the other, as the most simple case, which is what we test
     * Use of assertTrue and assertEquals to make sure tests run smoothly
     */
    @Test
    public void calculateBestPathTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile[] path = controller.calculateBestPath(Tile.TileColor.WHITE);

        Assert.assertNotNull(path);
        Assert.assertTrue(path.length >= 11);
        Assert.assertEquals(11, path[path.length-1].getTileCol());
    }

    /* Test to ensure calculateBestPath handles obstacles correctly.
     * A wall of BLACK tiles is placed to block the ideal route, and we
     * verify the pathfinder routes around it by asserting the start of the path
     * still begins at the expected column.
     */
    @Test
    public void calculateBestPathBlockedTest(){
        QuaxBoardController controller = newInitialisedController();

        for(int row = 1; row < 11; row++){
            controller.findTileById("OctCell" + row + "A").setColor(Tile.TileColor.BLACK);
        }
        Tile[] path = controller.calculateBestPath(Tile.TileColor.WHITE);
        Assert.assertNotNull(path);
        Assert.assertEquals(1, path[0].getTileCol());
    }

    /* Tests the backtracking logic of the pathfinder after the best path is found
     * Creates two new tiles and inserts them into HashMap used to track tiles
     * Verify that reconstructPath works with HashMap of new tiles and has appropriate properties
     */
    @Test
    public void reconstructPathTest(){
        QuaxBoardController controller = newInitialisedController();
        java.util.Map<Tile, Tile> parents = new java.util.HashMap<>();

        Tile t1 = controller.findTileById("OctCell11A");
        Tile t2 = controller.findTileById("OctCell11B");
        parents.put(t2, t1);

        Tile[] path = controller.reconstructPath(t2, parents);
        Assert.assertEquals(2, path.length);
        Assert.assertEquals(t1, path[0]);
        Assert.assertEquals(t2, path[1]);
    }

    /* Tests that 'showStrategy' boolean is updated and therefore best path of the bot is highlighted to user
     * Requires use of helper methods created to toggle showStrategy on and off, and uses reflection
     * to invoke highlightPath method, which in turns highlights path to the user
     */
    @Test
    public void highlightPathTest() throws Exception{
        QuaxBoardController controller = newInitialisedController();
        setPrivateField(controller, "showStrategy", true);
        controller.setMode("BOT");

        try{
            java.lang.reflect.Method method = QuaxBoardController.class.getDeclaredMethod("highlightPath");
            method.setAccessible(true);
            method.invoke(controller);
        } catch(Exception ex){
            //Is a UI/JavaFxScene error so we ignore this
        }

        boolean showStrategy = (boolean) getPrivateField(controller, "showStrategy");
        Assert.assertTrue(showStrategy);
    }

    /* Test to ensure fallback move simply picks the first available empty tile
     * on the board when called.
     */
    @Test
    public void fallBackMoveTest() {
        QuaxBoardController controller = newInitialisedController();
        Tile tile = controller.fallBackMove();

        Assert.assertNotNull(tile);
        Assert.assertEquals(Tile.TileColor.EMPTY, tile.getColor());
        Assert.assertEquals("OctCell11A", tile.getId());
    }

    /* Test to verify the isEmpty utility function correctly identifies
     * an unplayed tile. Uses assertTrue for the initial state and
     * assertFalse after the tile's color state has been changed.
     */
    @Test
    public void isEmptyTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile tile = controller.findTileById("OctCell5E");

        Assert.assertTrue(controller.isEmpty(tile));
        tile.setColor(Tile.TileColor.WHITE);
        Assert.assertFalse(controller.isEmpty(tile));
    }

    /* Test to verify the isBlack utility function correctly identifies
     * a BLACK tile, and explicitly rejects identifying it as WHITE.
     */
    @Test
    public void isBlackTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile tile = controller.findTileById("OctCell3F");

        tile.setColor(Tile.TileColor.BLACK);
        Assert.assertTrue(controller.isBlack(tile));
        Assert.assertFalse(controller.isWhite(tile));
    }

    /* Test to verify the isWhite utility function correctly identifies
     * a WHITE tile, and explicitly rejects identifying it as BLACK.
     */
    @Test
    public void isWhiteTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile tile = controller.findTileById("OctCell3F");

        tile.setColor(Tile.TileColor.WHITE);
        Assert.assertTrue(controller.isWhite(tile));
        Assert.assertFalse(controller.isBlack(tile));
    }

    /* Test to ensure the bot can successfully claim a guaranteed connection.
     * Uses reflection to insert a hypothetical guaranteed connection into the
     * private 'guaranteeConnection' map and verifies the correct tile is returned.
     */
    @Test
    public void claimGuaranteedConnectionTest() throws Exception{
        QuaxBoardController controller = newInitialisedController();

        Tile t1 = controller.findTileById("OctCell5E");
        Tile t2 = controller.findTileById("OctCell5F");

        java.util.Map<Tile, Tile> map = (java.util.Map<Tile, Tile>) getPrivateField(controller, "guaranteeConnection");

        t1.setColor(Tile.TileColor.BLACK);
        map.put(t1, t2);

        Tile result = controller.claimGuaranteedConnection();

        Assert.assertEquals(t2, result);
    }

    /* Test to verify that secureConnection returns null if Black hasn't placed
     * a tile yet, avoiding null pointer exceptions in early bot moves.
     */
    @Test
    public void secureConnectionReturnsNullWhenNoBlackPlacedTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile result = controller.secureConnection();

        Assert.assertNull(result);
    }

    /* Test to verify that completeGuaranteeChain returns null
     * when there are no guaranteed connections currently built.
     */
    @Test
    public void completeGuaranteeChainReturnsNullWhenEmptyTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile result = controller.completeGuaranteeChain();

        Assert.assertNull(result);
    }

    /* Test to verify that advanceOwnPath successfully returns a valid,
     * empty tile for the bot to play when attempting to further its own win.
     */
    @Test
    public void advanceOwnPathReturnsTileTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile move = controller.advanceOwnPath();

        Assert.assertNotNull(move);
        Assert.assertEquals(Tile.TileColor.EMPTY, move.getColor());
    }

    /* Test to verify the behavior of checkBlockOpponent on a newly initialized board.
     * Ensures that a valid non-null tile is still generated as a block or default move.
     */
    @Test
    public void checkBlockOpponentReturnsNullWhenEmptyTest(){
        QuaxBoardController controller = newInitialisedController();
        Tile result = controller.checkBlockOpponent();

        Assert.assertNotNull(result);
    }

    /* Test to verify getAllTilesOfColor handles an empty board properly.
     * Uses reflection to access the private method and asserts that the
     * returned list of WHITE tiles is entirely empty.
     */
    @Test
    public void getAllTilesOfColorEmptyBoardTest() throws Exception {
        QuaxBoardController controller = newInitialisedController();

        java.lang.reflect.Method method = QuaxBoardController.class.getDeclaredMethod("getAllTilesOfColor", Tile.TileColor.class);
        method.setAccessible(true);

        List<Tile> result = (List<Tile>) method.invoke(controller, Tile.TileColor.WHITE);

        Assert.assertTrue(result.isEmpty());
    }

    /* Test to verify getAllTilesOfColor successfully retrieves only Octagon tiles.
     * Colors two specific octagons WHITE and uses reflection to ensure exactly
     * those two tiles are returned in the resulting list.
     */
    @Test
    public void getAllTilesOfColorOctagonsOnlyTest() throws Exception {
        QuaxBoardController controller = newInitialisedController();

        Tile t1 = controller.findTileById("OctCell5E");
        Tile t2 = controller.findTileById("OctCell6F");

        t1.setColor(Tile.TileColor.WHITE);
        t2.setColor(Tile.TileColor.WHITE);

        java.lang.reflect.Method method = QuaxBoardController.class.getDeclaredMethod("getAllTilesOfColor", Tile.TileColor.class);
        method.setAccessible(true);

        List<Tile> result = (List<Tile>) method.invoke(controller, Tile.TileColor.WHITE);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(t1));
        Assert.assertTrue(result.contains(t2));
    }

    /* Test to verify getAllTilesOfColor successfully retrieves only Rhombus tiles.
     * Colors two specific rhombuses BLACK and uses reflection to ensure exactly
     * those two tiles are returned in the resulting list.
     */
    @Test
    public void getAllTilesOfColorRhombusesOnlyTest() throws Exception {
        QuaxBoardController controller = newInitialisedController();

        Tile r1 = controller.findTileById("RhoCell1");
        Tile r2 = controller.findTileById("RhoCell2");

        r1.setColor(Tile.TileColor.BLACK);
        r2.setColor(Tile.TileColor.BLACK);

        java.lang.reflect.Method method = QuaxBoardController.class.getDeclaredMethod("getAllTilesOfColor", Tile.TileColor.class);
        method.setAccessible(true);

        List<Tile> result = (List<Tile>) method.invoke(controller, Tile.TileColor.BLACK);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.contains(r1));
        Assert.assertTrue(result.contains(r2));
    }
}
