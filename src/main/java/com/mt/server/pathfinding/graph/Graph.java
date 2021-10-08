package com.mt.server.pathfinding.graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 无向图
 *
 * @author pangjiawei
 * @created 2021-08-06 10:32:46
 */
public class Graph {
    private int v; //顶点个数
    private LinkedList<Integer> adj[]; //邻接表

    public Graph(int v) {
        this.v = v;
        this.adj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    public void addEdge(int s, int t) {
        adj[s].add(t);
        adj[t].add(s);
    }

    private void print(int[] prev, int s, int t) { // 递归打印s->t的路径
        if (prev[t] != -1 && t != s) {
            print(prev, s, prev[t]);
        }
        System.out.print(t + " ");
    }

    /**
     * 广度优先搜索
     */
    public void bfs(int s, int t) {
        if (s == t) return;
        boolean[] visited = new boolean[v]; // 记录所有顶点是否被访问过
        visited[s] = true;
        Queue<Integer> queue = new LinkedList<>(); // 记录已经被访问、但相连的顶点还没有被访问的顶点
        queue.add(s);
        int[] prev = new int[v]; // 记录搜索路径，即访问过程中每个顶点的前置顶点，从起点出发，每个顶点的前置顶点只会有一个
        for (int i = 0; i < v; ++i) {
            prev[i] = -1;
        }
        while (queue.size() != 0) {
            int w = queue.poll(); // 取出下一个待处理顶点
            for (int i = 0; i < adj[w].size(); ++i) { // 遍历该顶点的所有相连的顶点
                int q = adj[w].get(i);
                if (!visited[q]) { // 如果该相连顶点没有被访问过，继续判断
                    prev[q] = w; // 设置访问路径
                    if (q == t) {
                        print(prev, s, t);
                        return;
                    }
                    visited[q] = true; //记录已访问过
                    queue.add(q); // 放入待处理队列
                }
            }
        }
    }

    boolean dfsFound = false; // 深度优先搜索是否完成

    /**
     * 深度优先搜索
     */
    public void dfs(int s, int t) {
        dfsFound = false;
        boolean[] visited = new boolean[v]; // 记录所有顶点是否被访问过
        int[] prev = new int[v]; // 记录搜索路径
        for (int i = 0; i < v; ++i) {
            prev[i] = -1;
        }
        recurDfs(s, t, visited, prev);
        print(prev, s, t);
    }

    private void recurDfs(int w, int t, boolean[] visited, int[] prev) {
        if (dfsFound) return;
        visited[w] = true;
        if (w == t) {
            dfsFound = true;
            return;
        }
        for (int i = 0; i < adj[w].size(); ++i) {
            int q = adj[w].get(i);
            if (!visited[q]) {
                prev[q] = w;
                recurDfs(q, t, visited, prev);
            }
        }
    }
}
