package com.john.algorithm.sorting.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 977. 有序数组的平方
 *
 * <p>给你一个按非递减顺序排序的整数数组 nums，返回每个数字的平方组成的新数组，也按非递减顺序排序。
 *
 * <p>示例：nums = [-4,-1,0,3,10]，输出 [0,1,9,16,100]。
 *
 * <p>面试考频：中（双指针 + 排序入门）
 * <p>常见公司：Facebook、Amazon、微软
 * <p>LeetCode 通过率：约 72.5%
 */
public class SquaresOfSortedArray {

    /**
     * 双指针从两端比较平方，填入结果数组从后往前。
     *
     * <p>核心解法：负数平方可能很大，左右端比较较大者放入 result[p]。
     *
     * <p>注意点：结果数组从 index n-1 向前填；O(n) 时间 O(n) 空间。
     *
     * <p>疑难点：为何不从左填？最小平方可能在中间（含负数时），必须从最大平方开始填。
     */
    public int[] sortedSquares(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        int[] result = new int[nums.length];
        for (int p = nums.length - 1; p >= 0; p--) {
            int leftSq = nums[left] * nums[left];
            int rightSq = nums[right] * nums[right];
            if (leftSq > rightSq) {
                result[p] = leftSq;
                left++;
            } else {
                result[p] = rightSq;
                right--;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== SquaresOfSortedArray ===");
        SquaresOfSortedArray s = new SquaresOfSortedArray();
        TestHelper.checkArray("case1", new int[]{0, 1, 9, 16, 100}, s.sortedSquares(new int[]{-4, -1, 0, 3, 10}));
        TestHelper.checkArray("case2", new int[]{0, 0, 1, 1}, s.sortedSquares(new int[]{-1, 0, 1, 2}));
        TestHelper.checkArray("case3", new int[]{4}, s.sortedSquares(new int[]{-2}));
    }
}
