package com.john.algorithm.slidingwindow.hard;

import com.john.algorithm.common.TestHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 76. 最小覆盖子串
 *
 * <p>给你一个字符串 s 和一个字符串 t，返回 s 中涵盖 t 所有字符的最小子串。如果不存在则返回空字符串 ""。
 *
 * <p>示例：s = "ADOBECODEBANC", t = "ABC"，输出 "BANC"。
 *
 * <p>面试考频：极高（Hard 滑动窗口标杆，Google/Meta 高频）
 * <p>常见公司：Google、Meta、Amazon、字节跳动、微软
 * <p>LeetCode 通过率：约 47.2%
 */
public class MinimumWindowSubstring {

    /**
     * 滑动窗口 + 哈希表统计字符需求与窗口内计数。
     *
     * <p>核心解法：need 记录 t 各字符需求量，window 记录窗口内计数；right 扩展直到 formed == required，
     * 然后 left 收缩取最小，更新答案。
     *
     * <p>注意点：用 formed 计数「已满足种类的数量」避免每次全量比较；收缩时 decrement 并更新 formed。
     *
     * <p>疑难点：window.get(c) 与 need.get(c) 相等时 formed++；收缩导致不等时 formed--。
     */
    public String minWindow(String s, String t) {
        if (t.length() > s.length()) {
            return "";
        }
        Map<Character, Integer> need = new HashMap<>();
        for (int i = 0; i < t.length(); i++) {
            char c = t.charAt(i);
            need.put(c, need.getOrDefault(c, 0) + 1);
        }
        Map<Character, Integer> window = new HashMap<>();
        int required = need.size();
        int formed = 0;
        int left = 0;
        int minLen = Integer.MAX_VALUE;
        int minStart = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            window.put(c, window.getOrDefault(c, 0) + 1);
            if (need.containsKey(c) && window.get(c).intValue() == need.get(c).intValue()) {
                formed++;
            }
            while (formed == required) {
                if (right - left + 1 < minLen) {
                    minLen = right - left + 1;
                    minStart = left;
                }
                char leftChar = s.charAt(left);
                window.put(leftChar, window.get(leftChar) - 1);
                if (need.containsKey(leftChar) && window.get(leftChar) < need.get(leftChar)) {
                    formed--;
                }
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? "" : s.substring(minStart, minStart + minLen);
    }

    public static void main(String[] args) {
        System.out.println("=== MinimumWindowSubstring ===");
        MinimumWindowSubstring s = new MinimumWindowSubstring();
        TestHelper.checkString("case1", "BANC", s.minWindow("ADOBECODEBANC", "ABC"));
        TestHelper.checkString("case2", "a", s.minWindow("a", "a"));
        TestHelper.checkString("case3", "", s.minWindow("a", "aa"));
    }
}
