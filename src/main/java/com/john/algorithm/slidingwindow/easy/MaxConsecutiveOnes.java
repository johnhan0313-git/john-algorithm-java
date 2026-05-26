package com.john.algorithm.slidingwindow.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 485. 最大连续 1 的个数
 *
 * <p>给定一个二进制数组 nums，计算其中最大连续 1 的个数。
 *
 * <p>示例：nums = [1,1,0,1,1,1]，输出 3。
 *
 * <p>面试考频：中（滑动窗口入门题）
 * <p>常见公司：Amazon、微软、LinkedIn
 * <p>LeetCode 通过率：约 63.2%
 */
public class MaxConsecutiveOnes {

    /**
     * 单次遍历计数，遇 0 重置计数。
     *
     * <p>核心解法：count 记录当前连续 1 长度，遇 1 则 count++ 并更新 max；遇 0 则 count=0。
     *
     * <p>注意点：全 1 数组需正确返回 n；空数组返回 0。
     *
     * <p>疑难点：本题窗口固定扩展无需收缩；进阶 LC 1004 允许翻转 k 个 0 才需真滑动窗口。
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        int maxCount = 0;
        int count = 0;
        for (int num : nums) {
            if (num == 1) {
                count++;
                maxCount = Math.max(maxCount, count);
            } else {
                count = 0;
            }
        }
        return maxCount;
    }

    public static void main(String[] args) {
        System.out.println("=== MaxConsecutiveOnes ===");
        MaxConsecutiveOnes s = new MaxConsecutiveOnes();
        TestHelper.checkInt("case1", 3, s.findMaxConsecutiveOnes(new int[]{1, 1, 0, 1, 1, 1}));
        TestHelper.checkInt("case2", 2, s.findMaxConsecutiveOnes(new int[]{1, 0, 1, 1, 0, 1}));
        TestHelper.checkInt("case3", 0, s.findMaxConsecutiveOnes(new int[]{0, 0, 0}));
    }
}
