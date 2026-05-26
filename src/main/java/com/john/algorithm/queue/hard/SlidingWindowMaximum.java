package com.john.algorithm.queue.hard;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 239. 滑动窗口最大值
 *
 * <p>给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字，滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
 *
 * <p>示例：nums = [1,3,-1,-3,5,3,6,7], k = 3，输出 [3,3,5,5,6,7]。
 *
 * <p>面试考频：极高（单调队列 Hard 代表，Google/字节高频）
 * <p>常见公司：Google、Amazon、字节跳动、微软、Meta
 * <p>LeetCode 通过率：约 50.3%
 */
public class SlidingWindowMaximum {

    /**
     * 单调递减双端队列，队头始终是当前窗口最大值的下标。
     *
     * <p>核心解法：deque 存下标，从队尾弹出所有 nums[deque.peekLast()] &lt;= nums[i] 的元素，
     * 保证 deque 对应值单调递减；队头超出窗口范围（&lt;= i - k）则弹出。
     *
     * <p>注意点：存下标而非值，便于判断元素是否滑出窗口；i &gt;= k - 1 时才写入结果；
     * 每个下标最多入队出队各一次，均摊 O(n)。
     *
     * <p>疑难点：为何弹出较小值？它们不可能成为后续窗口最大值（有更大且更新的元素），
     * 保留只会阻塞队头；这是「单调队列」维护窗口极值的经典套路。
     */
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

    public static void main(String[] args) {
        System.out.println("=== SlidingWindowMaximum ===");
        SlidingWindowMaximum s = new SlidingWindowMaximum();
        TestHelper.checkArray("case1", new int[]{3, 3, 5, 5, 6, 7}, s.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
        TestHelper.checkArray("case2", new int[]{1}, s.maxSlidingWindow(new int[]{1}, 1));
        TestHelper.checkArray("case3", new int[]{3, 3}, s.maxSlidingWindow(new int[]{1, 3, 1, 2}, 2));
    }
}
