package com.john.algorithm.slidingwindow;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 3. 无重复字符的最长子串
 *
 * <p>给定一个字符串 s，请你找出其中不含有重复字符的最长子串的长度。
 *
 * <p>示例：s = "abcabcbb"，输出 3（子串 "abc"）。
 */
public class LongestSubstringWithoutRepeating {

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
}
