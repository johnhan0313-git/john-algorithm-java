package com.john.algorithm.backtracking.hard;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 51. N 皇后
 *
 * <p>按照国际象棋的规则，皇后可以攻击与之处在同一行、同一列或同一斜线上的棋子。
 * n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
 * 给你一个整数 n，返回所有不同的 n 皇后问题的解决方案。
 *
 * <p>示例：n = 4，输出 [[".Q..","...Q","Q...","..Q."] 等]。
 *
 * <p>面试考频：高（Hard 回溯标杆，Google/字节常考）
 * <p>常见公司：Google、字节跳动、Amazon、华为
 * <p>LeetCode 通过率：约 74.5%
 */
public class NQueens {

    /**
     * 逐行放皇后，用 col/diag1/diag2 集合判冲突。
     *
     * <p>核心解法：row 从 0 到 n-1，try col，无冲突则放置、递归下一行、撤销。
     *
     * <p>注意点：diag1 用 row-col，diag2 用 row+col 标识斜线；收集时用 char[] 构造字符串。
     *
     * <p>疑难点：斜线冲突判定——同主对角线 row-col 相同，副对角线 row+col 相同。
     */
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        boolean[] cols = new boolean[n];
        boolean[] diag1 = new boolean[2 * n];
        boolean[] diag2 = new boolean[2 * n];
        char[][] board = new char[n][n];
        for (char[] row : board) {
            Arrays.fill(row, '.');
        }
        backtrack(0, n, board, cols, diag1, diag2, result);
        return result;
    }

    private void backtrack(int row, int n, char[][] board, boolean[] cols,
                         boolean[] diag1, boolean[] diag2, List<List<String>> result) {
        if (row == n) {
            List<String> snapshot = new ArrayList<>();
            for (char[] r : board) {
                snapshot.add(new String(r));
            }
            result.add(snapshot);
            return;
        }
        for (int col = 0; col < n; col++) {
            int d1 = row - col + n;
            int d2 = row + col;
            if (cols[col] || diag1[d1] || diag2[d2]) {
                continue;
            }
            board[row][col] = 'Q';
            cols[col] = diag1[d1] = diag2[d2] = true;
            backtrack(row + 1, n, board, cols, diag1, diag2, result);
            board[row][col] = '.';
            cols[col] = diag1[d1] = diag2[d2] = false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== NQueens ===");
        NQueens s = new NQueens();
        System.out.println("case1 n=4 count: " + s.solveNQueens(4).size());
        System.out.println("case2 n=1: " + s.solveNQueens(1));
        System.out.println("case3 n=2 count: " + s.solveNQueens(2).size());
    }
}
