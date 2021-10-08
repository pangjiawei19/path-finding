package com.mt.server.pathfinding.map;

/**
 * @author pangjiawei
 * @created 2021-08-11 22:14:03
 */
public class Point {

    private int col;
    private int row;

    public Point(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return "Point{" +
                "col=" + col +
                ", row=" + row +
                '}';
    }
}
