package com.john.algorithm.heap.medium;

import com.john.algorithm.common.TestHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * LeetCode 347. 前 K 个高频元素
 *
 * <p>给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
 *
 * <p>示例：nums = [1,1,1,2,2,3], k = 2，输出 [1,2]。
 *
 * <p>面试考频：极高（TopK 模板，Amazon/字节/Google 高频）
 * <p>常见公司：Amazon、字节跳动、Google、Meta、Uber
 * <p>LeetCode 通过率：约 64.3%
 */
public class TopKFrequentElements {

    /**
     * 哈希计数 + 大小为 k 的小顶堆（按频率）。
     *
     * <p>核心解法：map 统计频次，堆存 (freq, num)，堆超 k 则 pop 最小频率。
     *
     * <p>注意点：堆元素是频率；也可用桶排序 O(n) 当频率范围有限。
     *
     * <p>疑难点：与 LC 215 区别——215 按元素值 TopK，347 按频率 TopK。
     */
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int num : nums) {
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            minHeap.offer(new int[]{entry.getValue(), entry.getKey()});
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = minHeap.poll()[1];
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== TopKFrequentElements ===");
        TopKFrequentElements s = new TopKFrequentElements();
        System.out.println("case1: " + java.util.Arrays.toString(s.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2)));
        TestHelper.checkArray("case2", new int[]{1}, s.topKFrequent(new int[]{1}, 1));
        System.out.println("case3: " + java.util.Arrays.toString(s.topKFrequent(new int[]{4, 1, -1, 2, -1, 2, 3}, 2)));
    }
}
