package com.john.algorithm.hashmap.medium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LeetCode 49. 字母异位词分组
 *
 * <p>给你一个字符串数组，请你将字母异位词组合在一起。字母异位词指字母相同但排列不同的字符串。
 *
 * <p>示例：strs = ["eat","tea","tan","ate","nat","bat"]，
 * 输出 [["bat"],["nat","tan"],["ate","eat","tea"]]。
 */
public class GroupAnagrams {

    /**
     * 排序后的字符串作为哈希 key，将异位词归到同一组。
     *
     * <p>核心解法：对每个 str 排序得到 canonical key，用 HashMap&lt;key, List&gt; 分组收集。
     *
     * <p>注意点：key 必须能区分不同字母集合，排序是最直观的 canonical 形式；
     * 也可用 26 位字母计数数组拼接成 key（更快，适合长串）。
     *
     * <p>疑难点：为何排序后相同即异位词？异位词字母 multiset 相同，排序后字符串必然一致，
     * 这是 O(k log k) 的简单判等方式（k 为单词长度）。
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> groups = new HashMap<>();
        for (String str : strs) {
            char[] chars = str.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars);
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
        }
        return new ArrayList<>(groups.values());
    }
}
