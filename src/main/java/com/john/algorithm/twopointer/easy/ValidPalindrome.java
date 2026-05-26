package com.john.algorithm.twopointer.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 125. 验证回文串
 *
 * <p>如果在将所有大写字符转换为小写字符后，并且只保留字母数字字符之后，
 * 给定字符串 s 是一个回文串，则返回 true，否则返回 false。
 *
 * <p>示例：s = "A man, a plan, a canal: Panama"，输出 true。
 *
 * <p>面试考频：中（双指针热身题，Facebook 早期常考）
 * <p>常见公司：Meta、微软、Apple
 * <p>LeetCode 通过率：约 48.5%
 */
public class ValidPalindrome {

    /**
     * 左右指针跳过非字母数字，比较忽略大小写。
     *
     * <p>核心解法：left/right 向中间移动，跳过非法字符，Character.toLowerCase 比较。
     *
     * <p>注意点：空串视为回文；只需比较字母数字，标点空格跳过。
     *
     * <p>疑难点：isLetterOrDigit 比正则更快；也可先清洗字符串再比较，但额外空间 O(n)。
     */
    public boolean isPalindrome(String s) {
        int left = 0;
        int right = s.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }
            if (Character.toLowerCase(s.charAt(left)) != Character.toLowerCase(s.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== ValidPalindrome ===");
        ValidPalindrome s = new ValidPalindrome();
        TestHelper.checkBool("case1", true, s.isPalindrome("A man, a plan, a canal: Panama"));
        TestHelper.checkBool("case2", false, s.isPalindrome("race a car"));
        TestHelper.checkBool("case3", true, s.isPalindrome(" "));
    }
}
