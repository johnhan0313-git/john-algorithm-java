package com.john.algorithm.array.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 283. 移动零
 *
 * <p>给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 * 必须在不复制数组的情况下原地操作。
 *
 * <p>示例：nums = [0,1,0,3,12]，操作后 [1,3,12,0,0]。
 *
 * <p>面试考频：高（数组原地操作基础题）
 * <p>常见公司：字节跳动、美团、阿里巴巴、快手
 * <p>LeetCode 通过率：约 63.8%
 */
public class MoveZeroes {

    /**
     * 双指针：slow 指向下一个非零元素应放置的位置。
     *
     * <p>核心解法：fast 扫描，非零则与 slow 交换并 slow++；零自然被挤到后面。
     *
     * <p>注意点：先 swap 再 slow++，保证相对顺序；全零数组也正确。
     *
     * <p>疑难点：为何不直接赋值再补零？交换写法一次遍历，赋值法需两次遍历但常数更小。
     */
    public void moveZeroes(int[] nums) {
        int slow = 0;
        for (int fast = 0; fast < nums.length; fast++) {
            if (nums[fast] != 0) {
                int temp = nums[slow];
                nums[slow] = nums[fast];
                nums[fast] = temp;
                slow++;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== MoveZeroes ===");
        MoveZeroes s = new MoveZeroes();
        int[] c1 = {0, 1, 0, 3, 12}; s.moveZeroes(c1);
        TestHelper.checkArray("case1", new int[]{1, 3, 12, 0, 0}, c1);
        int[] c2 = {0, 0, 1}; s.moveZeroes(c2);
        TestHelper.checkArray("case2", new int[]{1, 0, 0}, c2);
        int[] c3 = {1, 2, 3}; s.moveZeroes(c3);
        TestHelper.checkArray("case3", new int[]{1, 2, 3}, c3);
    }
}
