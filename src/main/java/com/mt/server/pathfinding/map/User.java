package com.mt.server.pathfinding.map;

/**
 * @author pangjiawei
 * @created 2021-08-16 17:39:40
 */
public class User {

    private int id;
    private String mapKey;
    private int col;
    private int row;

    public User(int id, String mapKey, int col, int row) {
        this.id = id;
        this.mapKey = mapKey;
        this.col = col;
        this.row = row;
    }


    public int getId() {
        return id;
    }

    public String getMapKey() {
        return mapKey;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mapKey='" + mapKey + '\'' +
                ", col=" + col +
                ", row=" + row +
                '}';
    }
}
