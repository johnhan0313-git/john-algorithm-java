package com.john.algorithm.hashmap;

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
