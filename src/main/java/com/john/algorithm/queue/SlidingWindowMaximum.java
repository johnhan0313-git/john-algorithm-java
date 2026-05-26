package com.john.algorithm.queue;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 239. 滑动窗口最大值
 *
 * <p>给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字，滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
 *
 * <p>示例：nums = [1,3,-1,-3,5,3,6,7], k = 3，输出 [3,3,5,5,6,7]。
 */
public class SlidingWindowMaximum {

    public int[] maxSlidingWindow(int[] nums, int k) {
        Deque<Integer> deque = new ArrayDeque<>();
        int[] result = new int[nums.length - k + 1];
        for (int i = 0; i < nums.length; i++) {
            while (!deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        return result;
    }
}
