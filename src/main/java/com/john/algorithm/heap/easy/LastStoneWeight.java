package com.john.algorithm.heap.easy;

import com.john.algorithm.common.TestHelper;

import java.util.PriorityQueue;

/**
 * LeetCode 1046. 最后一块石头的重量
 *
 * <p>有一堆石头，每块石头的重量都是正整数。每一回合，选出两块最重的石头，然后一起粉碎。
 * 若重量相等则都消失；否则较轻的消失，较重的减去较轻的重量。求最后剩余石头重量，无则 0。
 *
 * <p>示例：stones = [2,7,4,1,8,1]，输出 1。
 *
 * <p>面试考频：中（堆入门题）
 * <p>常见公司：Amazon、Google
 * <p>LeetCode 通过率：约 65.4%
 */
public class LastStoneWeight {

    /**
     * 大顶堆每次取两个最大石头碰撞。
     *
     * <p>核心解法：全部入堆，while size&gt;1 弹出 y&gt;=x，若 y&gt;x 则 y-x 入堆。
     *
     * <p>注意点：Java PriorityQueue 默认小顶堆，需 reverseOrder 或取负。
     *
     * <p>疑难点：堆 size 为 1 时 peek 即答案；0 或 1 块石头直接返回对应值。
     */
    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        for (int stone : stones) {
            maxHeap.offer(stone);
        }
        while (maxHeap.size() > 1) {
            int y = maxHeap.poll();
            int x = maxHeap.poll();
            if (y > x) {
                maxHeap.offer(y - x);
            }
        }
        return maxHeap.isEmpty() ? 0 : maxHeap.poll();
    }

    public static void main(String[] args) {
        System.out.println("=== LastStoneWeight ===");
        LastStoneWeight s = new LastStoneWeight();
        TestHelper.checkInt("case1", 1, s.lastStoneWeight(new int[]{2, 7, 4, 1, 8, 1}));
        TestHelper.checkInt("case2", 0, s.lastStoneWeight(new int[]{1, 1}));
        TestHelper.checkInt("case3", 6, s.lastStoneWeight(new int[]{6}));
    }
}
