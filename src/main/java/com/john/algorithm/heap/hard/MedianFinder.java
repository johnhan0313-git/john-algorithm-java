package com.john.algorithm.heap.hard;

import com.john.algorithm.common.TestHelper;

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * LeetCode 295. 数据流的中位数
 *
 * <p>中位数是有序整数列表中的中间值。实现 MedianFinder 类：addNum 从数据流添加整数，
 * findMedian 返回目前所有元素的中位数。
 *
 * <p>示例：addNum(1), addNum(2), findMedian() 1.5, addNum(3), findMedian() 2.0。
 *
 * <p>面试考频：高（双堆经典设计题，Google/Amazon 常考）
 * <p>常见公司：Google、Amazon、Meta、微软、字节跳动
 * <p>LeetCode 通过率：约 53.1%
 */
public class MedianFinder {

    private final PriorityQueue<Integer> maxHeap;
    private final PriorityQueue<Integer> minHeap;

    public MedianFinder() {
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        minHeap = new PriorityQueue<>();
    }

    /**
     * 大顶堆存较小一半，小顶堆存较大一半，保持大小平衡。
     *
     * <p>核心解法：先入 maxHeap，再 balance 到 minHeap，若 minHeap 更大则移回 maxHeap。
     *
     * <p>注意点：两堆大小差不超过 1；maxHeap.size() &gt;= minHeap.size()。
     *
     * <p>疑难点：中位数为偶数时取 maxHeap.peek() 与 minHeap.peek() 平均；奇数取 maxHeap.peek()。
     */
    public void addNum(int num) {
        maxHeap.offer(num);
        minHeap.offer(maxHeap.poll());
        if (maxHeap.size() < minHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }

    public static void main(String[] args) {
        System.out.println("=== MedianFinder ===");
        MedianFinder mf = new MedianFinder();
        mf.addNum(1); mf.addNum(2);
        TestHelper.checkDouble("case1", 1.5, mf.findMedian());
        mf.addNum(3);
        TestHelper.checkDouble("case2", 2.0, mf.findMedian());
        mf.addNum(4);
        TestHelper.checkDouble("case3", 2.5, mf.findMedian());
    }
}
