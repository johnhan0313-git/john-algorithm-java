package com.john.algorithm.hashmap;

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
