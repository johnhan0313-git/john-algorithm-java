package com.john.algorithm.binarysearch.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 33. 搜索旋转排序数组
 *
 * <p>整数数组 nums 按升序排列，数组中的值互不相同。在预先未知的某个下标 k 上进行了旋转。
 * 给你旋转后的数组 nums 和一个整数 target，如果 target 存在于数组中，返回其下标，否则返回 -1。
 *
 * <p>示例：nums = [4,5,6,7,0,1,2], target = 0，输出 4。
 *
 * <p>面试考频：极高（二分变形 Top 题，国内大厂几乎必考）
 * <p>常见公司：字节跳动、Google、Amazon、Meta、微软
 * <p>LeetCode 通过率：约 44.1%
 */
public class SearchRotatedSortedArray {

    /**
     * 二分：判断 mid 落在左有序段还是右有序段，再判 target 在哪段。
     *
     * <p>核心解法：nums[left] &lt;= nums[mid] 则左半有序，否则右半有序；缩小区间。
     *
     * <p>注意点：等号 nums[left] &lt;= nums[mid] 处理两元素情况；互不相同简化相等判断。
     *
     * <p>疑难点：左半有序时 target 在 [left,mid) 需 nums[left] &lt;= target &lt; nums[mid]。
     */
    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[left] <= nums[mid]) {
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("=== SearchRotatedSortedArray ===");
        SearchRotatedSortedArray s = new SearchRotatedSortedArray();
        TestHelper.checkInt("case1", 4, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
        TestHelper.checkInt("case2", -1, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
        TestHelper.checkInt("case3", 0, s.search(new int[]{1}, 1));
    }
}
