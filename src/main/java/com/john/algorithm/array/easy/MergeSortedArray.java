package com.john.algorithm.array.easy;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 88. 合并两个有序数组
 *
 * <p>给你两个按非递减顺序排列的整数数组 nums1 和 nums2，另有两个整数 m 和 n，
 * 分别表示 nums1 和 nums2 中的元素数目。合并 nums2 到 nums1 中，使 nums1 成为一个有序数组。
 *
 * <p>示例：nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3，合并后 [1,2,2,3,5,6]。
 *
 * <p>面试考频：高（归并思想入门，常作热身题）
 * <p>常见公司：Google、微软、字节跳动、腾讯
 * <p>LeetCode 通过率：约 55.4%
 */
public class MergeSortedArray {

    /**
     * 从尾部双指针合并，避免覆盖 nums1 未处理元素。
     *
     * <p>核心解法：p1 = m-1, p2 = n-1, p = m+n-1，较大者放入 nums1[p] 并指针前移。
     *
     * <p>注意点：nums2 剩余元素需继续写入；nums1 剩余已在正确位置无需移动。
     *
     * <p>疑难点：为何不从头部合并？头部合并需额外 O(m) 空间或逐元素后移 O(m×n)。
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int p1 = m - 1;
        int p2 = n - 1;
        int p = m + n - 1;
        while (p1 >= 0 && p2 >= 0) {
            if (nums1[p1] > nums2[p2]) {
                nums1[p] = nums1[p1];
                p1--;
            } else {
                nums1[p] = nums2[p2];
                p2--;
            }
            p--;
        }
        while (p2 >= 0) {
            nums1[p] = nums2[p2];
            p2--;
            p--;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== MergeSortedArray ===");
        MergeSortedArray s = new MergeSortedArray();
        int[] c1 = {1, 2, 3, 0, 0, 0}; s.merge(c1, 3, new int[]{2, 5, 6}, 3);
        TestHelper.checkArray("case1", new int[]{1, 2, 2, 3, 5, 6}, c1);
        int[] c2 = {1, 0, 0}; s.merge(c2, 1, new int[]{2}, 1);
        TestHelper.checkArray("case2", new int[]{1, 2}, c2);
        int[] c3 = {0}; s.merge(c3, 0, new int[]{1}, 1);
        TestHelper.checkArray("case3", new int[]{1}, c3);
    }
}
