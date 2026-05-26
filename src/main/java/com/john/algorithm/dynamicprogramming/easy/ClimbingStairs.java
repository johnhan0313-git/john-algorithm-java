package com.john.algorithm.dynamicprogramming.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 70. 爬楼梯
 *
 * <p>假设你正在爬楼梯。需要 n 阶你才能到达楼顶。每次你可以爬 1 或 2 个台阶。
 * 你有多少种不同的方法可以爬到楼顶？
 *
 * <p>示例：n = 3，输出 3（1+1+1, 1+2, 2+1）。
 *
 * <p>面试考频：极高（DP 入门第一题，Amazon/Google 必考）
 * <p>常见公司：Amazon、Google、字节跳动、LinkedIn、微软
 * <p>LeetCode 通过率：约 55.3%
 */
public class ClimbingStairs {

    /**
     * 斐波那契递推，空间优化为两个变量。
     *
     * <p>核心解法：到达第 i 阶的方法数 = dp[i-1] + dp[i-2]（最后一步走 1 或 2 阶）。
     * 用 prev1、prev2 滚动代替数组。
     *
     * <p>注意点：n &lt;= 2 时直接返回 n；循环从 i = 3 开始，初始 prev2=1, prev1=2。
     *
     * <p>疑难点：为何是加法而非乘法？每步只能走 1 或 2 阶，到 i 的路径数等于
     * 「从 i-1 走一步」与「从 i-2 走两步」的路径数之和，与斐波那契本质相同。
     */
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

    public static void main(String[] args) {
        System.out.println("=== ClimbingStairs ===");
        ClimbingStairs s = new ClimbingStairs();
        TestHelper.checkInt("case1", 3, s.climbStairs(3));
        TestHelper.checkInt("case2", 2, s.climbStairs(2));
        TestHelper.checkInt("case3", 89, s.climbStairs(10));
    }
}
