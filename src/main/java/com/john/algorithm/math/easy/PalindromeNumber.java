package com.john.algorithm.math.easy;

/**
 * LeetCode 9. 回文数
 *
 * <p>给你一个整数 x，如果 x 是一个回文整数，返回 true；否则，返回 false。
 * 回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 *
 * <p>示例：x = 121，输出 true；x = -121，输出 false。
 */
public class PalindromeNumber {

    /**
     * 反转整数后半部分，与前半部分比较。
     *
     * <p>核心解法：循环 reversed = reversed * 10 + x % 10; x /= 10，
     * 直到 x &lt;= reversed。x == reversed（偶数位）或 x == reversed/10（奇数位）即为回文。
     *
     * <p>注意点：负数不是回文；末尾为 0 的非零数（如 10）不是回文，提前排除；
     * 只反转一半避免溢出。
     *
     * <p>疑难点：为何 x &lt;= reversed 时停止？此时 reversed 已包含原数后半（及中间位），
     * x 为前半；位数相等或相差 1 时上述两种等式覆盖所有回文情况。
     */
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
