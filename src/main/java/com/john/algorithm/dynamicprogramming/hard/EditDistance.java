package com.john.algorithm.dynamicprogramming.hard;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 72. 编辑距离
 *
 * <p>给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作数。
 * 可进行的操作：插入、删除、替换一个字符。
 *
 * <p>示例：word1 = "horse", word2 = "ros"，输出 3。
 *
 * <p>面试考频：极高（Hard DP 经典，Google/Meta 几乎必问）
 * <p>常见公司：Google、Meta、Amazon、微软、字节跳动
 * <p>LeetCode 通过率：约 62.4%
 */
public class EditDistance {

    /**
     * 二维 DP：dp[i][j] 为 word1 前 i 字符变 word2 前 j 字符的最少操作。
     *
     * <p>核心解法：字符相同则 dp[i][j]=dp[i-1][j-1]；否则 1+min(插入,删除,替换)。
     *
     * <p>注意点：dp[i][0]=i, dp[0][j]=j 初始化；可滚动数组优化空间。
     *
     * <p>疑难点：插入/删除/替换的 DP 转移对应 dp[i][j-1]+1, dp[i-1][j]+1, dp[i-1][j-1]+1。
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        System.out.println("=== EditDistance ===");
        EditDistance s = new EditDistance();
        TestHelper.checkInt("case1", 3, s.minDistance("horse", "ros"));
        TestHelper.checkInt("case2", 5, s.minDistance("intention", "execution"));
        TestHelper.checkInt("case3", 0, s.minDistance("", ""));
    }
}
