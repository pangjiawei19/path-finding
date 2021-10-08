package com.mt.server.pathfinding.map;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pangjiawei
 * @created 2021-08-11 22:09:23
 */
public class MapData {

    private final int width;
    private final int height;
    private final Map<Integer, Node> nodeMap = new HashMap<>();
    private final NodeType[][] mapArray;
    private final int[][] mapArrayOrdinal;

    public MapData(NodeType[][] mapArray) {
        this.height = mapArray.length;
        this.width = mapArray[0].length;
        this.mapArray = mapArray;
        this.mapArrayOrdinal = new int[height][];

        for (int i = 0; i < mapArray.length; i++) {
            mapArrayOrdinal[i] = new int[width];
            for (int j = 0; j < mapArray[i].length; j++) {
                NodeType nodeType = mapArray[i][j];
                Node node = new Node(j, i, nodeType);
                mapArrayOrdinal[i][j] = nodeType.ordinal();
                nodeMap.put(node.calculatePositionValue(), node);
            }
        }
    }

    public Node getNodeByColAndRow(int col, int row) {
        return getNodeByPositionValue(Node.calculatePositionValue(row, col));
    }

    public Node getNodeByPositionValue(int value) {
        return nodeMap.get(value);
    }

    public int[][] getMapArrayOrdinal() {
        return mapArrayOrdinal;
    }

    public Point randomCanMoveOnPoint() {
        int col = 0;
        int row = 0;
        do {
            col = (int) (Math.random() * width);
            row = (int) (Math.random() * height);
        } while (!mapArray[row][col].canMoveOn());

        return new Point(col, row);
    }
}
