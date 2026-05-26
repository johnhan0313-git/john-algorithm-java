package com.john.algorithm.twopointer.medium;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LeetCode 15. 三数之和
 *
 * <p>给你一个整数数组 nums，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k，
 * 同时还满足 nums[i] + nums[j] + nums[k] == 0。请你返回所有和为 0 且不重复的三元组。
 *
 * <p>示例：nums = [-1,0,1,2,-1,-4]，输出 [[-1,-1,2],[-1,0,1]]。
 *
 * <p>面试考频：极高（国内大厂算法岗必考 Top 5）
 * <p>常见公司：字节跳动、腾讯、阿里巴巴、Google、Amazon、Meta
 * <p>LeetCode 通过率：约 37.8%
 */
public class ThreeSum {

    /**
     * 排序 + 固定一端 + 双指针找两数之和。
     *
     * <p>核心解法：排序后 for i，left=i+1, right=n-1，sum==0 收集并跳过重复；sum&lt;0 则 left++，否则 right--。
     *
     * <p>注意点：i 层和 left 层都要 skip duplicate；sum 用 long 防溢出（本题 int 够用）。
     *
     * <p>疑难点：去重是难点——相同 nums[i] 不重复固定；找到答案后 while 跳过相同 left/right。
     */
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== ThreeSum ===");
        ThreeSum s = new ThreeSum();
        System.out.println("case1: " + s.threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
        System.out.println("case2: " + s.threeSum(new int[]{0, 0, 0}));
        System.out.println("case3: " + s.threeSum(new int[]{1, 2, -2, -1}));
    }
}
