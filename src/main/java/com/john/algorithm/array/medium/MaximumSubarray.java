package com.john.algorithm.array.medium;

/**
 * LeetCode 53. 最大子数组和
 *
 * <p>给你一个整数数组 nums，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 *
 * <p>示例：nums = [-2,1,-3,4,-1,2,1,-5,4]，输出 6（子数组 [4,-1,2,1]）。
 */
public class MaximumSubarray {

    /**
     * Kadane 算法：遍历时维护「以当前元素结尾的最大子数组和」。
     *
     * <p>核心解法：对每个位置，要么单独成段（currentSum = nums[i]），要么接在前一段后面
     * （currentSum + nums[i]），取较大者。全局再维护 maxSum。
     *
     * <p>注意点：子数组至少包含一个元素，因此 currentSum 初始化为 nums[0] 而非 0；
     * 全负数数组时也能正确返回最大值。
     *
     * <p>疑难点：为什么 currentSum = max(nums[i], currentSum + nums[i]) 成立？
     * 若前一段和为负，接上只会拖累，不如从当前元素重新开始。
     */
    public int maxSubArray(int[] nums) {
        int maxSum = nums[0];
        int currentSum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            currentSum = Math.max(nums[i], currentSum + nums[i]);
            maxSum = Math.max(maxSum, currentSum);
        }
        return maxSum;
    }
}
