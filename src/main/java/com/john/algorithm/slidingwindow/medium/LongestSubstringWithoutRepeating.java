package com.john.algorithm.slidingwindow.medium;

import com.john.algorithm.common.TestHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 3. 无重复字符的最长子串
 *
 * <p>给定一个字符串 s，请你找出其中不含有重复字符的最长子串的长度。
 *
 * <p>示例：s = "abcabcbb"，输出 3（子串 "abc"）。
 *
 * <p>面试考频：极高（滑动窗口模板题，Amazon 最高频 Medium 之一）
 * <p>常见公司：Amazon、Google、Meta、字节跳动、微软
 * <p>LeetCode 通过率：约 41.3%
 */
public class LongestSubstringWithoutRepeating {

    /**
     * 可变窗口 + 哈希表记录字符最近出现位置。
     *
     * <p>核心解法：right 扩展窗口，遇到重复字符时将 left 跳到该字符上次位置 + 1；
     * 窗口 [left, right] 始终无重复，更新 maxLength。
     *
     * <p>注意点：left 只能右移不能回退，需用 Math.max(left, lastIndex + 1) 处理
     * 「重复字符出现在 left 左侧」的情况（如 "abba"）。
     *
     * <p>疑难点：为何不能简单 left++？重复字符可能在窗口外，直接 +1 会导致 left 回退，
     * 破坏滑动窗口单调性；用 lastIndex 可 O(1) 跳过整个无效区间。
     */
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> lastIndex = new HashMap<>();
        int left = 0;
        int maxLength = 0;
        for (int right = 0; right < s.length(); right++) {
            char c = s.charAt(right);
            if (lastIndex.containsKey(c)) {
                left = Math.max(left, lastIndex.get(c) + 1);
            }
            lastIndex.put(c, right);
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }

    public static void main(String[] args) {
        System.out.println("=== LongestSubstringWithoutRepeating ===");
        LongestSubstringWithoutRepeating s = new LongestSubstringWithoutRepeating();
        TestHelper.checkInt("case1", 3, s.lengthOfLongestSubstring("abcabcbb"));
        TestHelper.checkInt("case2", 1, s.lengthOfLongestSubstring("bbbbb"));
        TestHelper.checkInt("case3", 3, s.lengthOfLongestSubstring("pwwkew"));
    }
}
