package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Tile {
    public enum TileType{
        OCTAGON, RHOMBUS
    }
    public enum TileColor{
        EMPTY, BLACK, WHITE
    }

    private String id;
    private TileType type;
    private TileColor color;
    private int tileRow;
    private int tileCol;
    private List<Tile> neighbours;
    private javafx.scene.shape.Shape shape;

    public Tile(String id, TileType type, int tileRow, int tileCol){
        this.id = id;
        this.type = type;
        this.tileRow = tileRow;
        this.tileCol = tileCol;
        this.neighbours = new ArrayList<>();
        this.color = TileColor.EMPTY;
    }

    public String getId(){
        return id;
    }

    public TileType getType(){
        return type;
    }

    public TileColor getColor(){
        return color;
    }

    public void setColor(TileColor color){
        this.color = color;
    }

    public List<Tile> getNeighbours(){
        return neighbours;
    }

    public void addNeighbour(Tile t){
        neighbours.add(t);
    }

    public int getTileRow(){
        return tileRow;
    }

    public int getTileCol(){
        return tileCol;
    }

    public javafx.scene.shape.Shape getShape() {
        return shape;
    }

    public void setShape(javafx.scene.shape.Shape shape) {
        this.shape = shape;
    }

}
