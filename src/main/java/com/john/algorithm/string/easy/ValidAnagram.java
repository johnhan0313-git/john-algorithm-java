package com.john.algorithm.string.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 242. 有效的字母异位词
 *
 * <p>给定两个字符串 s 和 t，编写一个函数来判断 t 是否是 s 的字母异位词。
 *
 * <p>示例：s = "anagram", t = "nagaram"，输出 true。
 *
 * <p>面试考频：高（字符串热身 + 哈希计数，Amazon/Google 常考）
 * <p>常见公司：Amazon、Google、微软、字节跳动
 * <p>LeetCode 通过率：约 66.8%
 */
public class ValidAnagram {

    /**
     * 长度不等直接 false；26 字母计数数组比较。
     *
     * <p>核心解法：s 字符 count++，t 字符 count--，任一 count 非零则 false。
     *
     * <p>注意点：Unicode 扩展题需 HashMap；本题仅小写字母用 int[26] 更快。
     *
     * <p>疑难点：与 LC 49 分组异位词区别——本题判两串是否互为异位词，O(n) 计数即可。
     */
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }
        int[] count = new int[26];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i) - 'a']++;
            count[t.charAt(i) - 'a']--;
        }
        for (int c : count) {
            if (c != 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== ValidAnagram ===");
        ValidAnagram s = new ValidAnagram();
        TestHelper.checkBool("case1", true, s.isAnagram("anagram", "nagaram"));
        TestHelper.checkBool("case2", false, s.isAnagram("rat", "car"));
        TestHelper.checkBool("case3", true, s.isAnagram("a", "a"));
    }
}
