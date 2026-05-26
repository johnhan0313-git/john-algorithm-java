package com.john.algorithm.greedy.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 121. 买卖股票的最佳时机
 *
 * <p>给定一个数组 prices，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * 你只能选择某一天买入，并在未来某一天卖出。设计算法来计算你所能获取的最大利润。
 * 如果你不能获取任何利润，返回 0。
 *
 * <p>示例：prices = [7,1,5,3,6,4]，输出 5（第 2 天买入，第 5 天卖出）。
 *
 * <p>面试考频：极高（贪心入门，Amazon/Meta 股票系列第一题）
 * <p>常见公司：Amazon、Meta、Google、字节跳动、微软
 * <p>LeetCode 通过率：约 55.4%
 */
public class BestTimeToBuyAndSellStock {

    /**
     * 一次遍历，维护历史最低买入价与当前最大利润。
     *
     * <p>核心解法：遍历时更新 minPrice = min(minPrice, price)，
     * 并以 price - minPrice 更新 maxProfit。等价于「在每个卖出日选之前最低买入价」。
     *
     * <p>注意点：minPrice 初始为 Integer.MAX_VALUE；maxProfit 初始为 0（无利润时返回 0）；
     * 必须先更新 minPrice 再算 profit，同一天买卖利润为 0。
     *
     * <p>疑难点：为何贪心正确？对固定卖出日，最优买入一定是该日之前最低价；
     * 遍历所有卖出日时取 max 即全局最优，无需显式枚举买入日。
     */
    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int price : prices) {
            minPrice = Math.min(minPrice, price);
            maxProfit = Math.max(maxProfit, price - minPrice);
        }
        return maxProfit;
    }

    public static void main(String[] args) {
        System.out.println("=== BestTimeToBuyAndSellStock ===");
        BestTimeToBuyAndSellStock s = new BestTimeToBuyAndSellStock();
        TestHelper.checkInt("case1", 5, s.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
        TestHelper.checkInt("case2", 0, s.maxProfit(new int[]{7, 6, 4, 3, 1}));
        TestHelper.checkInt("case3", 1, s.maxProfit(new int[]{1, 2}));
    }
}
