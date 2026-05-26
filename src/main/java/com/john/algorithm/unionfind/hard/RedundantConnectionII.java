package com.john.algorithm.unionfind.hard;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 685. 冗余连接 II
 *
 * <p>在本问题中，有 n 个节点（节点编号从 1 到 n）的树加入了由一条有向边构成的新边。
 * 输入 edges 表示边，返回一条可以删除的边，使得删除后 graph 是有 n 个节点的有根树。
 * 若有多个答案，返回 edges 中最后出现的边。
 *
 * <p>示例：edges = [[1,2],[1,3],[2,3]]，输出 [2,3]。
 *
 * <p>面试考频：中（并查集 Hard，考察有向图判环 + 双父节点）
 * <p>常见公司：Google、Meta、华为
 * <p>LeetCode 通过率：约 35.2%
 */
public class RedundantConnectionII {

    /**
     * 先找入度为 2 的节点（双父边 candidate1/candidate2），再用并查集判环。
     *
     * <p>核心解法：无双父则纯环，返回首次成环边；有双父则跳过 candidate1 判环，
     * 仍有环删 candidate1，否则删 candidate2。
     *
     * <p>注意点：有向边 u→v 做 union(u,v)；parent[] 另存「节点第一个父边下标」。
     *
     * <p>疑难点：三种冗余类型——纯环、双父+无环、双父+有环，需分支处理。
     */
    public int[] findRedundantDirectedEdge(int[][] edges) {
        int n = edges.length;
        int[] firstParentEdge = new int[n + 1];
        int cand1 = -1;
        int cand2 = -1;
        for (int i = 0; i < n; i++) {
            int child = edges[i][1];
            if (firstParentEdge[child] != 0) {
                cand1 = firstParentEdge[child] - 1;
                cand2 = i;
            } else {
                firstParentEdge[child] = i + 1;
            }
        }
        int[] uf = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            uf[i] = i;
        }
        for (int i = 0; i < n; i++) {
            if (i == cand1) {
                continue;
            }
            int u = edges[i][0];
            int v = edges[i][1];
            if (find(uf, u) == find(uf, v)) {
                return cand2 == -1 ? edges[i] : edges[cand1];
            }
            union(uf, u, v);
        }
        return edges[cand2];
    }

    private int find(int[] uf, int x) {
        if (uf[x] != x) {
            uf[x] = find(uf, uf[x]);
        }
        return uf[x];
    }

    private void union(int[] uf, int u, int v) {
        uf[find(uf, u)] = find(uf, v);
    }

    public static void main(String[] args) {
        System.out.println("=== RedundantConnectionII ===");
        RedundantConnectionII s = new RedundantConnectionII();
        TestHelper.checkArray("case1", new int[]{2, 3}, s.findRedundantDirectedEdge(new int[][]{{1, 2}, {1, 3}, {2, 3}}));
        TestHelper.checkArray("case2", new int[]{3, 4}, s.findRedundantDirectedEdge(new int[][]{{1, 2}, {2, 3}, {3, 4}, {4, 1}, {1, 5}}));
        TestHelper.checkArray("case3", new int[]{2, 5}, s.findRedundantDirectedEdge(new int[][]{{2, 1}, {3, 1}, {4, 1}, {1, 5}}));
    }
}
