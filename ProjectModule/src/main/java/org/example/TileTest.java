package org.example;

import javafx.scene.shape.Rectangle;
import org.junit.Assert;
import org.junit.Test;

/* Unit tests for the Tile Class, which contains main and the basic
 *
 * Note: This test class does not test the JavaFX side of the game, as we are only
 * looking at testing the controller logic.
 */
public class TileTest {

    /* Test to ensure getter method for checking a player's ID works
     * Use of assert Equals and False methods for examples that should
     * output passed and failed tests.
     */
    @Test
    public void getIdTest(){
        String id = "001";
        Tile example = new Tile("001", Tile.TileType.OCTAGON, 1, 4);

        Assert.assertEquals(id, example.getId());
        Assert.assertFalse(example.getId().equals("002"));
    }

    /* Test to ensure getter method for checking the correct Tile type has been selected when a new Tile is created
     * Use of assert Equals and False methods for both the OCTAGON and RHOMBUS types to ensure all possible outcomes
     * have been tested
     */
    @Test
    public void getTypeTest(){
        Tile.TileType type1 = Tile.TileType.OCTAGON;
        Tile example = new Tile("001", Tile.TileType.OCTAGON, 1, 4);
        Assert.assertEquals(type1, example.getType());
        Assert.assertFalse(example.getType().equals("RHOMBUS"));

        Tile.TileType type2 = Tile.TileType.RHOMBUS;
        example = new Tile("002", Tile.TileType.RHOMBUS, 1, 4);
        Assert.assertEquals(type2, example.getType());
        Assert.assertFalse(example.getType().equals("RHOMBUS"));
    }

    /* When a new Tile is created, it's type should be initialised to EMPTY, as a user has obviously not started
     * the game yet and chosen where to place a tile.
     * This test uses assertEquals to ensure that this type is EMPTY when created
     */
    @Test
    public void tileColourEmptyTest(){
        Tile example = new Tile("001", Tile.TileType.OCTAGON, 1, 4);
        Assert.assertEquals(Tile.TileColor.EMPTY, example.getColor());
    }

    /* Test to ensure setter method works when a player chooses a tile in the game and the tile colour is set to
     * the player's colour, tests for both colours with correct and failed test to ensure that all possible
     * outcomes are accounted for.
     */
    @Test
    public void setColourTest(){
        Tile.TileColor colour1 = Tile.TileColor.BLACK;
        Tile example1 = new Tile("001", Tile.TileType.OCTAGON, 3, 5);
        example1.setColor(colour1);
        Assert.assertEquals(colour1, example1.getColor());

        Tile.TileColor colour2 = Tile.TileColor.WHITE;
        example1.setColor(colour2);
        Assert.assertEquals(colour2, example1.getColor());
    }

    /* Tests to ensure that getter methods work for ensuring the Tile is created in the correct row
     * Uses assert Equals and False with both types of tile to ensure that all possible outcomes have
     * been accounted for. Along with the getter method for the columns, this allows the board to be
     * successfully created.
     */
    @Test
    public void getTileRowTest(){
        int rowExample1 = 1;
        Tile example = new Tile("001", Tile.TileType.OCTAGON, 1, 4);
        Assert.assertEquals(rowExample1, example.getTileRow());

        int rowExample2 = 2;
        Tile example2 = new Tile("003", Tile.TileType.OCTAGON, 6, 2);
        Assert.assertFalse(example2.getTileRow() == rowExample2);

        int rowExample3 = 3;
        Tile example3 = new Tile("005", Tile.TileType.RHOMBUS, 3, 3);
        Assert.assertEquals(rowExample3, example3.getTileRow());

        int rowExample4 = 4;
        Tile example4 = new Tile("008", Tile.TileType.OCTAGON, 2, 4);
        Assert.assertFalse(example4.getTileRow() == rowExample4);
    }

    /* Tests to ensure that getter methods work for ensuring the Tile is created in the correct column
     * Uses assert Equals and False with both types of tile to ensure that all possible outcomes have
     * been accounted for. Along with the getter method for the rows, this allows the board to be
     * successfully created.
     */
    @Test
    public void getTileColTest(){
        int colExample1 = 1;
        Tile example = new Tile("001", Tile.TileType.OCTAGON, 1, 1);
        Assert.assertEquals(colExample1, example.getTileRow());

        int colExample2 = 2;
        Tile example2 = new Tile("003", Tile.TileType.OCTAGON, 8, 6);
        Assert.assertFalse(example2.getTileRow() == colExample2);

        int colExample3 = 3;
        Tile example3 = new Tile("005", Tile.TileType.RHOMBUS, 3, 3);
        Assert.assertEquals(colExample3, example3.getTileRow());

        int colExample4 = 4;
        Tile example4 = new Tile("008", Tile.TileType.RHOMBUS, 2, 9);
        Assert.assertFalse(example4.getTileRow() == colExample4);
    }

    /* Tests to ensure that neighbours are correctly added to a tile in the game when placed
     * beside each other.
     * Use of assert equals and true to test that the size of the neighbours List array
     * is correct and that it contains the correct elements.
     */
    @Test
    public void getNeighboursTest(){
        Tile center = new Tile("001", Tile.TileType.OCTAGON, 5, 5);
        Tile left = new Tile("002", Tile.TileType.RHOMBUS, 5, 5);
        Tile right = new Tile("003", Tile.TileType.OCTAGON, 5, 6);

        center.addNeighbour(left);
        center.addNeighbour(right);

        Assert.assertEquals(2, center.getNeighbours().size());
        Assert.assertTrue(center.getNeighbours().contains(left));
        Assert.assertTrue(center.getNeighbours().contains(right));
    }

    /* Tests to make sure when neighbouring tiles are added, that they are added in the
     * correct order, by whichever one is added first.
     * Use of assert equals to ensure that tiles added to list are in correct order
     */
    @Test
    public void addNeighboursPreservesOrderTest(){
        Tile tile = new Tile("001", Tile.TileType.OCTAGON, 5, 5);
        Tile first = new Tile("002", Tile.TileType.OCTAGON, 5, 5);
        Tile second = new Tile("003", Tile.TileType.OCTAGON, 5, 6);

        tile.addNeighbour(first);
        tile.addNeighbour(second);

        Assert.assertEquals(first, tile.getNeighbours().get(0));
        Assert.assertEquals(second, tile.getNeighbours().get(1));
    }

    /* Simple test to ensure the getter and setter methods for shapes work in the Tile class
     * Use of assertEquals shows that both these methods work as intended.
     */
    @Test
    public void getterAndSetterShapeTest(){
        Tile tile = new Tile("001", Tile.TileType.OCTAGON, 5, 5);
        Rectangle shape = new  Rectangle();
        tile.setShape(shape);

        Assert.assertEquals(shape, tile.getShape());
    }

}
