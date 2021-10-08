package com.mt.server.pathfinding.map;

/**
 * @author pangjiawei
 * @created 2021-08-14 20:56:19
 */
public class Node {

    public static final int POSITION_VALUE_X_MULTIPLY = 1000;

    private final int col;
    private final int row;
    private final NodeType nodeType;

    public Node(int col, int row, NodeType nodeType) {
        this.col = col;
        this.row = row;
        this.nodeType = nodeType;
    }

    public static boolean isSameNode(Node node1, Node node2) {
        return node1.getRow() == node2.getRow()
                && node1.getCol() == node2.getCol();
    }

    public static int calculatePositionValue(int row, int col) {
        return row * POSITION_VALUE_X_MULTIPLY + col;
    }

    public int calculatePositionValue() {
        return calculatePositionValue(this.row, this.col);
    }

    public boolean canMoveOn() {
        return nodeType.canMoveOn();
    }

    public Point toPoint() {
        return new Point(col, row);
    }

    public NodeType getPointType() {
        return nodeType;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "Node{" +
                "col=" + col +
                ", row=" + row +
                ", nodeType=" + nodeType +
                '}';
    }
}
