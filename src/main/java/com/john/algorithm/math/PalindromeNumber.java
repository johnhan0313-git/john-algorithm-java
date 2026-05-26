package com.john.algorithm.math;

/**
 * LeetCode 9. 回文数
 *
 * <p>给你一个整数 x，如果 x 是一个回文整数，返回 true；否则，返回 false。
 * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 *
 * <p>示例：x = 121，输出 true；x = -121，输出 false。
 */
public class PalindromeNumber {

    public boolean isPalindrome(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }
        int reversed = 0;
        while (x > reversed) {
            reversed = reversed * 10 + x % 10;
            x /= 10;
        }
        return x == reversed || x == reversed / 10;
    }
}
