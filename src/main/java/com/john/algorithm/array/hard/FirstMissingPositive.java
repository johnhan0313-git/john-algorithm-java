package com.john.algorithm.array.hard;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 41. 缺失的第一个正数
 *
 * <p>给你一个未排序的整数数组 nums，请你找出其中没有出现的最小的正整数。
 * 要求 O(n) 时间、O(1) 额外空间。
 *
 * <p>示例：nums = [3,4,-1,1]，输出 2。
 *
 * <p>面试考频：高（Hard 中考察原地哈希的代表题）
 * <p>常见公司：Google、Amazon、字节跳动、华为
 * <p>LeetCode 通过率：约 47.5%
 */
public class FirstMissingPositive {

    /**
     * 原地哈希：将 nums[i] 放到下标 nums[i]-1 的位置。
     *
     * <p>核心解法：遍历交换，使值 v 落在 index v-1；再扫描找第一个 nums[i] != i+1。
     *
     * <p>注意点：只交换 1..n 范围内的值；交换后 i 不递增（新值还需处理）；避免死循环判 v==v-1 位置。
     *
     * <p>疑难点：答案最大为 n+1（如 [1,2,3] 缺失 4），扫描完返回 n+1。
     */
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int temp = nums[nums[i] - 1];
                nums[nums[i] - 1] = nums[i];
                nums[i] = temp;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return n + 1;
    }

    public static void main(String[] args) {
        System.out.println("=== FirstMissingPositive ===");
        FirstMissingPositive s = new FirstMissingPositive();
        TestHelper.checkInt("case1", 3, s.firstMissingPositive(new int[]{1, 2, 0}));
        TestHelper.checkInt("case2", 2, s.firstMissingPositive(new int[]{3, 4, -1, 1}));
        TestHelper.checkInt("case3", 1, s.firstMissingPositive(new int[]{7, 8, 9, 11, 12}));
    }
}
