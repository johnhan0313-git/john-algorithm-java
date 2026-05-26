package com.john.algorithm.stack.hard;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 84. 柱状图中最大的矩形
 *
 * <p>给定 n 个非负整数，表示柱状图中各个柱子的高度，每个柱子彼此相邻且宽度为 1。
 * 计算在该柱状图中，能够勾勒出来的矩形的最大面积。
 *
 * <p>示例：heights = [2,1,5,6,2,3]，输出 10。
 *
 * <p>面试考频：高（单调栈 Hard 代表，Google/字节常考）
 * <p>常见公司：Google、字节跳动、Amazon、微软
 * <p>LeetCode 通过率：约 47.6%
 */
public class LargestRectangleInHistogram {

    /**
     * 单调递增栈：每个柱子作为矩形「最矮边」向左右扩展。
     *
     * <p>核心解法：栈存下标，当前高度小于栈顶高度时弹出，计算以弹出柱为高的矩形宽度。
     *
     * <p>注意点：弹出时 width = i - stack.peek() - 1（栈空则 width = i）；末尾需 flush 栈。
     *
     * <p>疑难点：宽度是「左右第一个更矮柱之间」，不是相邻柱；哨兵或在末尾 push 0 简化边界。
     */
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int maxArea = 0;
        for (int i = 0; i <= heights.length; i++) {
            int h = i == heights.length ? 0 : heights[i];
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }
        return maxArea;
    }

    public static void main(String[] args) {
        System.out.println("=== LargestRectangleInHistogram ===");
        LargestRectangleInHistogram s = new LargestRectangleInHistogram();
        TestHelper.checkInt("case1", 10, s.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
        TestHelper.checkInt("case2", 4, s.largestRectangleArea(new int[]{2, 4}));
        TestHelper.checkInt("case3", 1, s.largestRectangleArea(new int[]{1, 1, 1}));
    }
}
