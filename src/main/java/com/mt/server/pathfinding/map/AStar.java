package com.mt.server.pathfinding.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author pangjiawei
 * @created 2021-08-14 20:49:05
 */
public class AStar {

    public static List<Node> find(MapData mapData, Direction direction, Node start, Node end) {

        PriorityQueue queue = new PriorityQueue();
        Set<Integer> inQueue = new HashSet<>();
        Map<Integer, Vertex> vertexMap = new HashMap<>();

        Vertex startVertex = new Vertex(start);
        startVertex.dist = 0;
        startVertex.f = 0;
        Vertex endVertex = new Vertex(end);
        queue.add(startVertex);
        vertexMap.put(startVertex.getPositionValue(), startVertex);
        inQueue.add(startVertex.getPositionValue());

        Vertex currentVertex = null;
        while (!queue.isEmpty()) {
            Vertex minVertex = queue.poll();
            List<Node> surroundingNodes = direction.getSurroundingNodes(mapData, mapData.getNodeByColAndRow(minVertex.col, minVertex.row));
            for (Node surroundingNode : surroundingNodes) {
                int pointValue = surroundingNode.calculatePositionValue();
                Vertex surroundingVertex = vertexMap.get(pointValue);
                if (surroundingVertex == null) {
                    surroundingVertex = new Vertex(surroundingNode);
                    vertexMap.put(pointValue, surroundingVertex);
                }

                float moveCost = Vertex.calculateMoveCost(minVertex, surroundingVertex);
                if (minVertex.dist + moveCost < surroundingVertex.dist) {
                    surroundingVertex.dist = minVertex.dist + moveCost;
                    surroundingVertex.f = surroundingVertex.dist + Vertex.hManhattan(surroundingVertex, endVertex);
                    surroundingVertex.parent = minVertex;
                    if (inQueue.contains(pointValue)) {
                        queue.update(surroundingVertex);
                    } else {
                        queue.add(surroundingVertex);
                        inQueue.add(pointValue);
                    }
                }

                if (Node.isSameNode(end, surroundingNode)) {
                    currentVertex = surroundingVertex;
                    queue.clear();
                    break;
                }
            }
        }

        return generatePath(mapData, currentVertex);
    }

    private static List<Node> generatePath(MapData mapData, Vertex end) {
        LinkedList<Node> list = new LinkedList<>();
        Vertex cursor = end;
        if (cursor != null) {
            list.add(mapData.getNodeByPositionValue(cursor.getPositionValue()));
            while (cursor.parent != null) {
                list.addFirst(mapData.getNodeByPositionValue(cursor.parent.getPositionValue()));
                cursor = cursor.parent;
            }
        }
        return list;
    }

    /**
     * 顶点
     */
    private static class Vertex {
        public float dist; // 起始顶点到这个顶点的距离，也就是 g(i)
        public float f; // f(i) = g(i) + h(i)
        public int col;
        public int row;
        public Vertex parent;

        public Vertex(Node point) {
            this(point.getCol(), point.getRow());
        }

        public Vertex(int col, int row) {
            this.col = col;
            this.row = row;
            this.f = Integer.MAX_VALUE;
            this.dist = Integer.MAX_VALUE;
        }

        public static float calculateMoveCost(Vertex v1, Vertex v2) {
            if (v1.col == v2.col || v1.row == v2.row) {
                return 1;
            }
            return 1.4f;
        }

        private static int hManhattan(Vertex v1, Vertex v2) {
            return Math.abs(v1.col - v2.col) + Math.abs(v1.row - v2.row);
        }

        public int getPositionValue() {
            return Node.calculatePositionValue(row, col);
        }
    }

    private static class PriorityQueue { // 根据 vertex.f 构建小顶堆
        private Vertex[] nodes;
        private int count;

        public PriorityQueue() {
            this(8);
        }

        public PriorityQueue(int v) {
            this.nodes = new Vertex[v + 1];
            this.count = 0;
        }

        public Vertex poll() {
            Vertex top = nodes[1];

            nodes[1] = nodes[count]; // 将堆尾数据放到堆顶
            this.count--;

            int i = 1; // 从堆顶节点开始，自上而下进行堆化
            while (true) {
                int minIndex = i;

                // 先和左子节点比较
                if (i * 2 <= count && nodes[minIndex].f > nodes[i * 2].f) {
                    minIndex = i * 2;
                }

                // 再和右子节点比较
                if (i * 2 + 1 <= count && nodes[minIndex].f > nodes[i * 2 + 1].f) {
                    minIndex = i * 2 + 1;
                }

                // 如果没有子节点比自己小，退出循环
                if (minIndex == i) {
                    break;
                }

                // 和比自己小的子节点交换
                this.swap(i, minIndex);

                i = minIndex; // 从比自己小的子节点开始继续判断
            }
            return top;
        }

        public void add(Vertex vertex) {
            // 扩容
            if (this.count + 1 >= this.nodes.length) {
                Vertex[] newNodes = new Vertex[(int) (this.nodes.length * 1.5)];
                System.arraycopy(this.nodes, 0, newNodes, 0, this.nodes.length);
                this.nodes = newNodes;
            }

            // 将数据插到堆尾
            this.count++;
            this.nodes[count] = vertex;

            // 自下向上进行堆化
            this.heapify(count);
        }

        // 更新结点的值，并且从下往上堆化，重新符合堆的定义。时间复杂度 O(logn)。
        public void update(Vertex vertex) {
            int index = 0;
            for (int i = 1; i <= count; i++) {
                Vertex node = nodes[i];
                if (node.row == vertex.row && node.col == vertex.col) {
                    node.dist = vertex.dist;
                    node.f = vertex.f;
                    index = i;
                    break;
                }
            }

            if (index > 0) {
                this.heapify(index);
            }
        }

        public boolean isEmpty() {
            return count < 1;
        }

        public void clear() {
            this.count = 0;
        }

        /**
         * 自下向上进行堆化
         */
        private void heapify(int index) {
            int i = index;
            while (i / 2 > 0 && nodes[i].f < nodes[i / 2].f) {
                this.swap(i, i / 2);
                i = i / 2;
            }
        }

        /**
         * 交换节点
         */
        private void swap(int index1, int index2) {
            Vertex tmp = nodes[index1];
            nodes[index1] = nodes[index2];
            nodes[index2] = tmp;
        }
    }


}
