package com.john.algorithm.binarysearch.easy;

/**
 * LeetCode 704. 二分查找
 *
 * <p>给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target，
 * 写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。
 *
 * <p>示例：nums = [-1,0,3,5,9,12], target = 9，输出 4。
 */
public class BinarySearch {

    /**
     * 闭区间 [left, right] 二分查找。
     *
     * <p>核心解法：mid = left + (right - left) / 2 防溢出；
     * nums[mid] == target 返回；小于则 left = mid + 1，大于则 right = mid - 1。
     *
     * <p>注意点：循环条件 left &lt;= right，区间缩小到空时结束；
     * mid 必须用 left + (right-left)/2，不能 (left+right)/2。
     *
     * <p>疑难点：为何 left = mid + 1 而非 mid？闭区间下 mid 已检查过，
     * 下次搜索应排除 mid；若用 left = mid 在 left == right 时可能死循环。
     */
    public int search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
}
