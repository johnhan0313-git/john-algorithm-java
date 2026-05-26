package com.john.algorithm.dynamicprogramming;

/**
 * LeetCode 70. 爬楼梯
 *
 * <p>假设你正在爬楼梯。需要 n 阶你才能到达楼顶。每次你可以爬 1 或 2 个台阶。
 * 你有多少种不同的方法可以爬到楼顶？
 *
 * <p>示例：n = 3，输出 3（1+1+1, 1+2, 2+1）。
 */
public class ClimbingStairs {

    public int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }
        int prev2 = 1;
        int prev1 = 2;
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev2 = prev1;
            prev1 = current;
        }
        return prev1;
    }
}
