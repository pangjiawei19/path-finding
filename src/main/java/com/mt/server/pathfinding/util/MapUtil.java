package com.mt.server.pathfinding.util;

import com.mt.server.pathfinding.map.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pangjiawei
 * @created 2021-08-10 18:44:49
 */
public class MapUtil {

    public static List<Node> mergePath(List<Node> path) {
        if (path.size() < 3) {
            return path;
        }
        List<Node> newPath = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            Node node = path.get(i);
            if (i != 0 && i != path.size() - 1) {
                Node preNode = newPath.get(newPath.size() - 1);
                Node nextNode = path.get(i + 1);
                int colDiff1 = node.getCol() - preNode.getCol();
                int colDiff2 = nextNode.getCol() - node.getCol();
                int rowDiff1 = node.getRow() - preNode.getRow();
                int rowDiff2 = nextNode.getRow() - node.getRow();
                if ((colDiff1 == 0 && colDiff2 == 0) || (rowDiff1 == 0 && rowDiff2 == 0)) {
                    continue;
                }
                if (colDiff1 * rowDiff2 == colDiff2 * rowDiff1) {
                    // colDiff1    rowDiff1
                    // -------- == --------
                    // colDiff2    rowDiff2
                    continue;
                }
            }
            newPath.add(node);
        }
        return newPath;
    }
}
