package com.mt.server.pathfinding.map;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pangjiawei
 * @created 2021-08-14 21:00:08
 */
public enum Direction {

    Four {
        @Override
        public List<Node> getSurroundingNodes(MapData mapData, Node current) {
            List<Node> nodes = new ArrayList<>(4);

            addNode(nodes, mapData, current.getCol() - 1, current.getRow()); // 左
            addNode(nodes, mapData, current.getCol() + 1, current.getRow()); // 右
            addNode(nodes, mapData, current.getCol(), current.getRow() - 1); // 上
            addNode(nodes, mapData, current.getCol(), current.getRow() + 1); // 下

            return nodes;
        }
    },
    Eight {
        @Override
        public List<Node> getSurroundingNodes(MapData mapData, Node current) {
            List<Node> nodes = new ArrayList<>(8);

            boolean leftFlag = addNode(nodes, mapData, current.getCol() - 1, current.getRow()); // 左
            boolean rightFlag = addNode(nodes, mapData, current.getCol() + 1, current.getRow()); // 右
            boolean upFlag = addNode(nodes, mapData, current.getCol(), current.getRow() - 1); // 上
            boolean downFlag = addNode(nodes, mapData, current.getCol(), current.getRow() + 1); // 下

            if (leftFlag && upFlag) {
                addNode(nodes, mapData, current.getCol() - 1, current.getRow() - 1); // 左上
            }
            if (leftFlag && downFlag) {
                addNode(nodes, mapData, current.getCol() - 1, current.getRow() + 1); // 左下
            }
            if (rightFlag && upFlag) {
                addNode(nodes, mapData, current.getCol() + 1, current.getRow() - 1); // 右上
            }
            if (rightFlag && downFlag) {
                addNode(nodes, mapData, current.getCol() + 1, current.getRow() + 1); // 右下
            }

            return nodes;
        }
    },
    ;

    static boolean addNode(List<Node> nodes, MapData mapData, int newCol, int newRow) {
        Node newNode = mapData.getNodeByPositionValue(Node.calculatePositionValue(newRow, newCol));
        if (newNode != null && newNode.canMoveOn()) {
            nodes.add(newNode);
            return true;
        }
        return false;
    }

    public abstract List<Node> getSurroundingNodes(MapData mapData, Node current);
}
