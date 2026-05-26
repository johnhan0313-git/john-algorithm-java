package com.john.algorithm.sorting.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 75. 颜色分类
 *
 * <p>给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，原地排序使得相同颜色相邻，顺序为红、白、蓝。
 * 用 0、1、2 表示红、白、蓝。不使用库函数，O(n) 时间、O(1) 空间。
 *
 * <p>示例：nums = [2,0,2,1,1,0]，输出 [0,0,1,1,2,2]。
 *
 * <p>面试考频：极高（荷兰国旗问题，Google/字节/Amazon 高频）
 * <p>常见公司：Google、字节跳动、Amazon、Meta、微软
 * <p>LeetCode 通过率：约 59.8%
 */
public class SortColors {

    /**
     * 三指针 Dutch National Flag：p0 指向下一个 0 位置，p2 指向下一个 2 位置。
     *
     * <p>核心解法：i 扫描，0 与 p0 交换 p0++，2 与 p2 交换 p2--（i 不增，因换入未处理），1 则 i++。
     *
     * <p>注意点：换 2 后 i 不能 ++，换入元素需再判；p0 也跟踪下一个 1 边界。
     *
     * <p>疑难点：一次遍历不变式——[0,p0) 全 0，[p0,i) 全 1，(p2,n] 全 2。
     */
    public void sortColors(int[] nums) {
        int p0 = 0;
        int p2 = nums.length - 1;
        int i = 0;
        while (i <= p2) {
            if (nums[i] == 0) {
                swap(nums, i, p0);
                p0++;
                i++;
            } else if (nums[i] == 2) {
                swap(nums, i, p2);
                p2--;
            } else {
                i++;
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    public static void main(String[] args) {
        System.out.println("=== SortColors ===");
        SortColors s = new SortColors();
        int[] c1 = {2, 0, 2, 1, 1, 0}; s.sortColors(c1);
        TestHelper.checkArray("case1", new int[]{0, 0, 1, 1, 2, 2}, c1);
        int[] c2 = {2, 0, 1}; s.sortColors(c2);
        TestHelper.checkArray("case2", new int[]{0, 1, 2}, c2);
        int[] c3 = {0}; s.sortColors(c3);
        TestHelper.checkArray("case3", new int[]{0}, c3);
    }
}
