package com.john.algorithm.string.hard;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 10. 正则表达式匹配
 *
 * <p>给你一个字符串 s 和一个字符规律 p，请你实现支持 '.' 和 '*' 的正则表达式匹配。
 * '.' 匹配任意单个字符；'*' 匹配零个或多个前面的那一个元素。
 *
 * <p>示例：s = "aa", p = "a*"，输出 true。
 *
 * <p>面试考频：高（Hard DP 经典，Google/Meta 高频）
 * <p>常见公司：Google、Meta、Amazon、微软
 * <p>LeetCode 通过率：约 29.8%
 */
public class RegularExpressionMatching {

    /**
     * dp[i][j] 表示 s 前 i 字符与 p 前 j 字符是否匹配。
     *
     * <p>核心解法：p[j-1] 非 '*' 则字符匹配转移；是 '*' 则零次（dp[i][j-2]）或多次（字符匹配且 dp[i-1][j]）。
     *
     * <p>注意点：dp[0][0]=true；处理 a* 可消空串 p 前缀；.* 可匹配空 s。
     *
     * <p>疑难点：'*' 必须作用于前一个 pattern 字符；aa 与 a* 匹配因 a* 可表示 aa。
     */
    public boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int j = 2; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2];
            }
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char sc = s.charAt(i - 1);
                char pc = p.charAt(j - 1);
                if (pc != '*') {
                    if (match(sc, pc)) {
                        dp[i][j] = dp[i - 1][j - 1];
                    }
                } else {
                    char prev = p.charAt(j - 2);
                    dp[i][j] = dp[i][j - 2];
                    if (match(sc, prev)) {
                        dp[i][j] = dp[i][j] || dp[i - 1][j];
                    }
                }
            }
        }
        return dp[m][n];
    }

    private boolean match(char s, char p) {
        return p == '.' || s == p;
    }

    public static void main(String[] args) {
        System.out.println("=== RegularExpressionMatching ===");
        RegularExpressionMatching s = new RegularExpressionMatching();
        TestHelper.checkBool("case1", true, s.isMatch("aa", "a*"));
        TestHelper.checkBool("case2", false, s.isMatch("aa", "a"));
        TestHelper.checkBool("case3", true, s.isMatch("ab", ".*"));
    }
}
