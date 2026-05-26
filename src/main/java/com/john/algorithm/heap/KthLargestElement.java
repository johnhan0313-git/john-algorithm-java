package com.john.algorithm.heap;

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
