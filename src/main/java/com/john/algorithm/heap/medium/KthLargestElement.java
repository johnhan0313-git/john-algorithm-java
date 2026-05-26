package com.john.algorithm.heap.medium;

import java.util.PriorityQueue;

/**
 * LeetCode 215. 数组中的第 K 个最大元素
 *
 * <p>给定整数数组 nums 和整数 k，请找出数组中第 k 个最大的元素。
 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 *
 * <p>示例：nums = [3,2,1,5,6,4], k = 2，输出 5。
 */
public class KthLargestElement {

    /**
     * 大小为 k 的小顶堆，堆顶即为第 k 大元素。
     *
     * <p>核心解法：遍历 nums，元素入堆；堆大小超过 k 时 poll 最小值。
     * 最终堆中保留 k 个最大元素，peek 为第 k 大。
     *
     * <p>注意点：找第 k 大用小顶堆，找第 k 小用大顶堆；k 从 1 开始计数；
     * 也可用快速选择 O(n) 平均时间。
     *
     * <p>疑难点：为何小顶堆能维护 TopK？堆大小恒为 k，新元素若比堆顶大则入堆并弹出堆顶，
     * 堆中始终是「目前见过的最大的 k 个数」，堆顶是这 k 个中最小的，即第 k 大。
     */
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : nums) {
            minHeap.offer(num);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }
}
