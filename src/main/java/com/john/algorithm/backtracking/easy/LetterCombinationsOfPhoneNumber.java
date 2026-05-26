package com.john.algorithm.backtracking.easy;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 17. 电话号码的字母组合
 *
 * <p>给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。数字到字母映射与电话按键相同。
 *
 * <p>示例：digits = "23"，输出 ["ad","ae","af","bd","be","bf","cd","ce","cf"]。
 *
 * <p>面试考频：高（回溯入门，Google/Amazon 常作第一题）
 * <p>常见公司：Google、Amazon、字节跳动、微软
 * <p>LeetCode 通过率：约 58.6%
 */
public class LetterCombinationsOfPhoneNumber {

    private static final String[] MAPPING = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    /**
     * 回溯：按 digit 顺序逐层扩展 path。
     *
     * <p>核心解法：index 到达 digits.length 时收集 path；否则取当前 digit 对应字母逐个递归。
     *
     * <p>注意点：digits 为空返回空列表；path 用 StringBuilder 需 deleteCharAt 撤销。
     *
     * <p>疑难点：与全排列区别——每步选项来自不同 digit 的字母表，无 used 数组。
     */
    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits.isEmpty()) {
            return result;
        }
        backtrack(digits, 0, new StringBuilder(), result);
        return result;
    }

    private void backtrack(String digits, int index, StringBuilder path, List<String> result) {
        if (index == digits.length()) {
            result.add(path.toString());
            return;
        }
        String letters = MAPPING[digits.charAt(index) - '0'];
        for (int i = 0; i < letters.length(); i++) {
            path.append(letters.charAt(i));
            backtrack(digits, index + 1, path, result);
            path.deleteCharAt(path.length() - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== LetterCombinationsOfPhoneNumber ===");
        LetterCombinationsOfPhoneNumber s = new LetterCombinationsOfPhoneNumber();
        System.out.println("case1: " + s.letterCombinations("23"));
        System.out.println("case2: " + s.letterCombinations(""));
        System.out.println("case3: " + s.letterCombinations("2"));
    }
}
