package com.john.algorithm.graph.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 733. 图像渲染
 *
 * <p>有一幅以 m x n 二维数组表示的图像，其中 image[i][j] 表示图像第 i 行第 j 列的像素值。
 * 另给定三个整数 sr、sc 和 newColor，从 image[sr][sc] 开始，将同颜色连通区域替换为 newColor。
 *
 * <p>示例：image = [[1,1,1],[1,1,0],[1,0,1]], sr = 1, sc = 1, newColor = 2。
 *
 * <p>面试考频：中（DFS/BFS 入门，Google/Amazon 热身）
 * <p>常见公司：Google、Amazon、微软
 * <p>LeetCode 通过率：约 58.3%
 */
public class FloodFill {

    /**
     * DFS 四方向扩散，仅替换与原色相同且未访问的像素。
     *
     * <p>核心解法：原色 oldColor = image[sr][sc]，若等于 newColor 直接返回；否则 dfs 替换。
     *
     * <p>注意点：同色替换直接返回避免死循环；边界检查 row/col。
     *
     * <p>疑难点：与 LC 200 岛屿区别——本题从单点出发 flood fill，非计数连通块。
     */
    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int oldColor = image[sr][sc];
        if (oldColor == newColor) {
            return image;
        }
        dfs(image, sr, sc, oldColor, newColor);
        return image;
    }

    private void dfs(int[][] image, int row, int col, int oldColor, int newColor) {
        if (row < 0 || col < 0 || row >= image.length || col >= image[0].length || image[row][col] != oldColor) {
            return;
        }
        image[row][col] = newColor;
        dfs(image, row - 1, col, oldColor, newColor);
        dfs(image, row + 1, col, oldColor, newColor);
        dfs(image, row, col - 1, oldColor, newColor);
        dfs(image, row, col + 1, oldColor, newColor);
    }

    public static void main(String[] args) {
        System.out.println("=== FloodFill ===");
        FloodFill s = new FloodFill();
        int[][] c1 = {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        TestHelper.printArray("case1 row0", s.floodFill(c1, 1, 1, 2)[0]);
        int[][] c2 = {{0, 0, 0}, {0, 0, 0}};
        TestHelper.checkInt("case2", 2, s.floodFill(c2, 0, 0, 2)[0][0]);
        int[][] c3 = {{1}};
        TestHelper.checkInt("case3", 0, s.floodFill(c3, 0, 0, 0)[0][0]);
    }
}
