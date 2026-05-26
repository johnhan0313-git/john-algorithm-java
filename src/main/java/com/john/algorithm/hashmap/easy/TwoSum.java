package com.john.algorithm.hashmap.easy;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 1. 两数之和
 *
 * <p>给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，
 * 并返回它们的数组下标。你可以假设每种输入只会对应一个答案，且同一个元素不能使用两次。
 *
 * <p>示例：nums = [2,7,11,15], target = 9，输出 [0,1]。
 */
public class TwoSum {

    /**
     * 一次遍历 + 哈希表存「值 → 下标」。
     *
     * <p>核心解法：遍历到 nums[i] 时，查表看 complement = target - nums[i] 是否已出现；
     * 有则返回两数下标，无则把当前值放入 map。
     *
     * <p>注意点：先查后存，避免同一元素被用两次；题目保证有唯一解，无需额外处理无解。
     *
     * <p>疑难点：为何只需一遍？每个 complement 只会与当前元素配对一次，
     * 且当前元素尚未入表，不会与自身配对。
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (indexMap.containsKey(complement)) {
                return new int[]{indexMap.get(complement), i};
            }
            indexMap.put(nums[i], i);
        }
        throw new IllegalArgumentException("no solution");
    }
}
