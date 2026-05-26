package com.john.algorithm.unionfind.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 547. 省份数量
 *
 * <p>有 n 个城市，其中一些彼此相连，另一些没有相连。如果城市 a 与城市 b 直接相连，
 * 且城市 b 与城市 c 直接相连，那么城市 a 与城市 c 间接相连。省份是一组直接或间接相连的城市。
 * 给你一个 n x n 的矩阵 isConnected，其中 isConnected[i][j] = 1 表示第 i 个城市和第 j 个城市直接相连，
 * 返回省份的数量。
 *
 * <p>示例：isConnected = [[1,1,0],[1,1,0],[0,0,1]]，输出 2。
 *
 * <p>面试考频：高（并查集入门，Amazon/Google 常考）
 * <p>常见公司：Amazon、Google、字节跳动、Meta、LinkedIn
 * <p>LeetCode 通过率：约 65.4%
 */
public class NumberOfProvinces {

    /**
     * 并查集：遍历矩阵上三角，相连则 union，最终 count 即为连通分量数。
     *
     * <p>核心解法：初始化 n 个独立集合；isConnected[i][j] == 1 时 union(i, j)；
     * union 时若根不同则合并并 count--。find 带路径压缩。
     *
     * <p>注意点：矩阵对称，只遍历 j &gt; i 避免重复 union；对角线 isConnected[i][i] 恒为 1 可跳过；
     * 路径压缩在 find 中递归实现。
     *
     * <p>疑难点：为何 union 时 count--？初始每个节点独立为一个集合，count = n；
     * 每次成功合并两个不同集合，连通分量减 1，最终 count 即省份数。
     */
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        UnionFind unionFind = new UnionFind(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    unionFind.union(i, j);
                }
            }
        }
        return unionFind.count;
    }

    private static class UnionFind {
        private final int[] parent;
        private int count;

        UnionFind(int n) {
            parent = new int[n];
            count = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) {
                return;
            }
            parent[rootX] = rootY;
            count--;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== NumberOfProvinces ===");
        NumberOfProvinces s = new NumberOfProvinces();
        TestHelper.checkInt("case1", 2, s.findCircleNum(new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}));
        TestHelper.checkInt("case2", 3, s.findCircleNum(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        TestHelper.checkInt("case3", 1, s.findCircleNum(new int[][]{{1, 1}, {1, 1}}));
    }
}
