package com.john.algorithm.graph.medium;

/**
 * LeetCode 200. 岛屿数量
 *
 * <p>给你一个 m x n 的二维网格 grid，其中 '1' 表示陆地，'0' 表示水。
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 返回网格中岛屿的数量。
 *
 * <p>示例：grid = [
 *   ["1","1","1","1","0"],
 *   ["1","1","0","1","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","0","0","0"]
 * ]，输出 1。
 */
public class NumberOfIslands {

    /**
     * 遍历网格，遇 '1' 则 DFS 淹没整座岛屿并计数 +1。
     *
     * <p>核心解法：双重 for 扫描，grid[i][j] == '1' 时启动 dfs 将连通 '1' 全部置 '0'，
     * 避免重复访问。每启动一次 dfs 代表发现一座新岛屿。
     *
     * <p>注意点：dfs 中先判界再判是否为 '1'；原地改 grid 省去 visited 数组；
     * 四方向扩展即可，对角线不算连通。
     *
     * <p>疑难点：为何置 '0' 而非 visited？效果相同，'0' 表示已访问或本就是水，
     * 后续扫描会自动跳过；也可用并查集/BFS 求解。
     */
    public int numIslands(char[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    dfs(grid, i, j);
                    count++;
                }
            }
        }
        return count;
    }

    private void dfs(char[][] grid, int row, int col) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length || grid[row][col] == '0') {
            return;
        }
        grid[row][col] = '0';
        dfs(grid, row - 1, col);
        dfs(grid, row + 1, col);
        dfs(grid, row, col - 1);
        dfs(grid, row, col + 1);
    }
}
