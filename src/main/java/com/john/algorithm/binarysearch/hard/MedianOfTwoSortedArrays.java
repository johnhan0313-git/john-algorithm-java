package com.john.algorithm.binarysearch.hard;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 4. 寻找两个正序数组的中位数
 *
 * <p>给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。
 * 请你找出并返回这两个正序数组的中位数。算法时间复杂度应为 O(log (m+n))。
 *
 * <p>示例：nums1 = [1,3], nums2 = [2]，输出 2.0。
 *
 * <p>面试考频：极高（Hard 二分天花板，Google 超高频）
 * <p>常见公司：Google、Meta、Amazon、微软、字节跳动
 * <p>LeetCode 通过率：约 42.3%
 */
public class MedianOfTwoSortedArrays {

    /**
     * 在较短数组上二分 partition，使左右两部分元素个数平衡。
     *
     * <p>核心解法：在 nums1 上切分 i，nums2 上 j=(m+n+1)/2-i，使 leftMax &lt;= rightMin。
     *
     * <p>注意点：保证在较短数组上二分；奇偶长度中位数公式不同。
     *
     * <p>疑难点：partition 含义是 nums1[0..i-1] 和 nums2[0..j-1] 归入左半，需满足 maxLeft &lt;= minRight。
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }
        int m = nums1.length;
        int n = nums2.length;
        int left = 0;
        int right = m;
        while (left <= right) {
            int i = (left + right) / 2;
            int j = (m + n + 1) / 2 - i;
            int maxLeft1 = i == 0 ? Integer.MIN_VALUE : nums1[i - 1];
            int minRight1 = i == m ? Integer.MAX_VALUE : nums1[i];
            int maxLeft2 = j == 0 ? Integer.MIN_VALUE : nums2[j - 1];
            int minRight2 = j == n ? Integer.MAX_VALUE : nums2[j];
            if (maxLeft1 <= minRight2 && maxLeft2 <= minRight1) {
                if ((m + n) % 2 == 1) {
                    return Math.max(maxLeft1, maxLeft2);
                }
                return (Math.max(maxLeft1, maxLeft2) + Math.min(minRight1, minRight2)) / 2.0;
            }
            if (maxLeft1 > minRight2) {
                right = i - 1;
            } else {
                left = i + 1;
            }
        }
        throw new IllegalArgumentException("invalid input");
    }

    public static void main(String[] args) {
        System.out.println("=== MedianOfTwoSortedArrays ===");
        MedianOfTwoSortedArrays s = new MedianOfTwoSortedArrays();
        TestHelper.checkDouble("case1", 2.0, s.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));
        TestHelper.checkDouble("case2", 2.5, s.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}));
        TestHelper.checkDouble("case3", 1.0, s.findMedianSortedArrays(new int[]{1}, new int[]{}));
    }
}
