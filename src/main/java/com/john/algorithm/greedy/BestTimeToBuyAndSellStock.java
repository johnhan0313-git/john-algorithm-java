package com.john.algorithm.greedy;

/**
 * LeetCode 121. 买卖股票的最佳时机
 *
 * <p>给定一个数组 prices，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
 * 你只能选择某一天买入，并在未来某一天卖出。设计算法来计算你所能获取的最大利润。
 * 如果你不能获取任何利润，返回 0。
 *
 * <p>示例：prices = [7,1,5,3,6,4]，输出 5（第 2 天买入，第 5 天卖出）。
 */
public class BestTimeToBuyAndSellStock {

    public int maxProfit(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        for (int price : prices) {
            minPrice = Math.min(minPrice, price);
            maxProfit = Math.max(maxProfit, price - minPrice);
        }
        return maxProfit;
    }
}
