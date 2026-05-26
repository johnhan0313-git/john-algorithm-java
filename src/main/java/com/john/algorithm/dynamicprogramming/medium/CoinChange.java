package com.john.algorithm.dynamicprogramming.medium;

import com.john.algorithm.common.TestHelper;

import java.util.Arrays;

/**
 * LeetCode 322. 零钱兑换
 *
 * <p>给你一个整数数组 coins 表示不同面额的硬币，以及一个整数 amount 表示总金额。
 * 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1。
 *
 * <p>示例：coins = [1,2,5], amount = 11，输出 3（11 = 5+5+1）。
 *
 * <p>面试考频：极高（完全背包 DP，大厂必考）
 * <p>常见公司：Google、Amazon、字节跳动、腾讯、Airbnb
 * <p>LeetCode 通过率：约 48.6%
 */
public class CoinChange {

    /**
     * 完全背包：dp[i] 表示凑 amount i 的最少硬币数。
     *
     * <p>核心解法：dp[0]=0，其余初始化为 amount+1；对每个 coin 正序遍历 amount，dp[a]=min(dp[a], dp[a-coin]+1)。
     *
     * <p>注意点：不可达时 dp[amount] &gt; amount，返回 -1；硬币顺序无关。
     *
     * <p>疑难点：外层 coin 内层 amount 求最少个数；若外层 amount 则是排列问题（377 题）。
     */
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        for (int coin : coins) {
            for (int a = coin; a <= amount; a++) {
                dp[a] = Math.min(dp[a], dp[a - coin] + 1);
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        System.out.println("=== CoinChange ===");
        CoinChange s = new CoinChange();
        TestHelper.checkInt("case1", 3, s.coinChange(new int[]{1, 2, 5}, 11));
        TestHelper.checkInt("case2", -1, s.coinChange(new int[]{2}, 3));
        TestHelper.checkInt("case3", 0, s.coinChange(new int[]{1}, 0));
    }
}
