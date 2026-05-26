package com.john.algorithm.slidingwindow.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 209. 长度最小的子数组
 *
 * <p>给定一个含有 n 个正整数的数组和一个正整数 target，找出该数组中满足其和 ≥ target 的长度最小的
 * 连续子数组 [numsl, numsl+1, ..., numsr-1, numsr]，返回其长度。不存在则返回 0。
 *
 * <p>示例：target = 7, nums = [2,3,1,2,4,3]，输出 2（子数组 [4,3]）。
 *
 * <p>面试考频：高（滑动窗口模板题，字节/美团常考）
 * <p>常见公司：字节跳动、美团、Google、Facebook
 * <p>LeetCode 通过率：约 46.3%
 */
public class MinimumSizeSubarraySum {

    /**
     * 可变窗口：right 扩展累加 sum，sum &gt;= target 时收缩 left 取最小长度。
     *
     * <p>核心解法：双指针维护窗口和，满足条件时 while 循环 left++ 缩小窗口。
     *
     * <p>注意点：正整数保证 sum 单调，left 只增不减；无解返回 0 而非 -1。
     *
     * <p>疑难点：while 而非 if 收缩——同一 right 下可能多种 left 均满足，需取最短。
     */
    public int minSubArrayLen(int target, int[] nums) {
        int left = 0;
        int sum = 0;
        int minLen = Integer.MAX_VALUE;
        for (int right = 0; right < nums.length; right++) {
            sum += nums[right];
            while (sum >= target) {
                minLen = Math.min(minLen, right - left + 1);
                sum -= nums[left];
                left++;
            }
        }
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }

    public static void main(String[] args) {
        System.out.println("=== MinimumSizeSubarraySum ===");
        MinimumSizeSubarraySum s = new MinimumSizeSubarraySum();
        TestHelper.checkInt("case1", 2, s.minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}));
        TestHelper.checkInt("case2", 1, s.minSubArrayLen(4, new int[]{1, 4, 4}));
        TestHelper.checkInt("case3", 0, s.minSubArrayLen(11, new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
    }
}
