package com.john.algorithm.unionfind.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 684. 冗余连接
 *
 * <p>树可以看成无环连通图。给定往树中添加一条边后形成的图 edges，返回一条可以删去的边，
 * 使得结果仍是一棵树。若有多个答案，返回 edges 中最后出现的边。
 *
 * <p>示例：edges = [[1,2],[1,3],[2,3]]，输出 [2,3]。
 *
 * <p>面试考频：高（并查集入门，Amazon/Google 常考）
 * <p>常见公司：Amazon、Google、字节跳动
 * <p>LeetCode 通过率：约 66.7%
 */
public class RedundantConnection {

    /**
     * 并查集：首次发现 u、v 已在同一集合的边即为冗余边。
     *
     * <p>核心解法：顺序 union，find(u)==find(v) 时返回该边。
     *
     * <p>注意点：无向边 union(u,v)；路径压缩优化 find。
     *
     * <p>疑难点：为何返回最后出现的？题目要求；按序处理最后成环边即答案。
     */
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length;
        int[] parent = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            if (find(parent, u) == find(parent, v)) {
                return edge;
            }
            union(parent, u, v);
        }
        throw new IllegalArgumentException("no redundant edge");
    }

    private int find(int[] parent, int x) {
        if (parent[x] != x) {
            parent[x] = find(parent, parent[x]);
        }
        return parent[x];
    }

    private void union(int[] parent, int u, int v) {
        parent[find(parent, u)] = find(parent, v);
    }

    public static void main(String[] args) {
        System.out.println("=== RedundantConnection ===");
        RedundantConnection s = new RedundantConnection();
        TestHelper.checkArray("case1", new int[]{2, 3}, s.findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}}));
        TestHelper.checkArray("case2", new int[]{1, 4}, s.findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}, {3, 4}, {1, 4}}));
        TestHelper.checkArray("case3", new int[]{2, 3}, s.findRedundantConnection(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}}));
    }
}
