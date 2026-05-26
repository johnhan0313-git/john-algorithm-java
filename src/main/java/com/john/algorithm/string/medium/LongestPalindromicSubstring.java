package com.john.algorithm.string.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 5. 最长回文子串
 *
 * <p>给你一个字符串 s，找到 s 中最长的回文子串。
 *
 * <p>示例：s = "babad"，输出 "bab" 或 "aba"。
 *
 * <p>面试考频：高（字符串 DP/中心扩展，Amazon/Google 常考）
 * <p>常见公司：Amazon、Google、Meta、微软、字节跳动
 * <p>LeetCode 通过率：约 35.8%
 */
public class LongestPalindromicSubstring {

    /**
     * 中心扩展：以每个位置（及相邻两位置）为中心向两侧扩展。
     *
     * <p>核心解法：对每个 i，分别以 (i,i) 和 (i,i+1) 为中心调用 expand，
     * 取较长回文并更新 start、maxLength。
     *
     * <p>注意点：回文起点 start = i - (len - 1) / 2；expand 返回的是扩展后的长度；
     * 奇偶长度回文需分别处理。
     *
     * <p>疑难点：expand 为何返回 right - left - 1？循环退出时 left、right 各多走了一步，
     * 实际回文区间为 [left+1, right-1]，长度为 right - left - 1。
     */
    public String longestPalindrome(String s) {
        if (s.length() < 2) {
            return s;
        }
        int start = 0;
        int maxLength = 1;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expand(s, i, i);
            int len2 = expand(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > maxLength) {
                maxLength = len;
                start = i - (len - 1) / 2;
            }
        }
        return s.substring(start, start + maxLength);
    }

    private int expand(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    public static void main(String[] args) {
        System.out.println("=== LongestPalindromicSubstring ===");
        LongestPalindromicSubstring s = new LongestPalindromicSubstring();
        TestHelper.checkString("case1", "bab", s.longestPalindrome("babad"));
        TestHelper.checkString("case2", "bb", s.longestPalindrome("cbbd"));
        TestHelper.checkString("case3", "a", s.longestPalindrome("a"));
    }
}
