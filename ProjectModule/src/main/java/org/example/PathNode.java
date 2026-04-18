package org.example;

//class used to calculate the best path using dijkstras algorithm//
public class PathNode implements Comparable<PathNode> {
    Tile tile;
    int cost;

    public PathNode(Tile tile, int cost) {
        this.tile = tile;
        this.cost = cost;
    }

    @Override
    public int compareTo(PathNode o) {
        return Integer.compare(this.cost, o.cost);
    }
}
