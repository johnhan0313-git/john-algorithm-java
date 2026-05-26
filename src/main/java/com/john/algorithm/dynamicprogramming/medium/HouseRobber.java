package com.john.algorithm.dynamicprogramming.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 198. 打家劫舍
 *
 * <p>你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，相邻的房屋装有相互连通的防盗系统，
 * 如果两相邻的房屋在同一晚上被小偷闯入，系统会自动报警。给定一个代表每个房屋存放金额的非负整数数组 nums，
 * 计算你不触动警报装置的情况下，一夜之内能够偷窃到的最高金额。
 *
 * <p>示例：nums = [1,2,3,1]，输出 4（偷 1 号 + 3 号）。
 *
 * <p>面试考频：极高（线性 DP 入门，Amazon/Google 高频）
 * <p>常见公司：Amazon、Google、LinkedIn、字节跳动
 * <p>LeetCode 通过率：约 55.1%
 */
public class HouseRobber {

    /**
     * dp[i] = max(dp[i-1], dp[i-2] + nums[i])，空间优化为两变量。
     *
     * <p>核心解法：prev2 表示 dp[i-2]，prev1 表示 dp[i-1]，滚动更新。
     *
     * <p>注意点：空数组返回 0；单元素返回 nums[0]。
     *
     * <p>疑难点：不能偷相邻——选 nums[i] 则不能选 nums[i-1]，故来自 dp[i-2]+nums[i]。
     */
    public int rob(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }
        int prev2 = nums[0];
        int prev1 = Math.max(nums[0], nums[1]);
        for (int i = 2; i < nums.length; i++) {
            int current = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = current;
        }
        return prev1;
    }

    public static void main(String[] args) {
        System.out.println("=== HouseRobber ===");
        HouseRobber s = new HouseRobber();
        TestHelper.checkInt("case1", 4, s.rob(new int[]{1, 2, 3, 1}));
        TestHelper.checkInt("case2", 12, s.rob(new int[]{2, 7, 9, 3, 1}));
        TestHelper.checkInt("case3", 0, s.rob(new int[]{}));
    }
}
