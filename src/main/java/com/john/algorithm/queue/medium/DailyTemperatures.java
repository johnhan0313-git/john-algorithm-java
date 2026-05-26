package com.john.algorithm.queue.medium;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 739. 每日温度
 *
 * <p>给定一个整数数组 temperatures，表示每天的温度，返回一个数组 answer，
 * 其中 answer[i] 是指在第 i 天之后，才会有更高的温度。如果之后都不会更高，则为 0。
 *
 * <p>示例：temperatures = [73,74,75,71,69,72,76,73]，输出 [1,1,4,2,1,1,0,0]。
 *
 * <p>面试考频：高（单调栈模板，Amazon/Google 常考）
 * <p>常见公司：Amazon、Google、字节跳动、微软
 * <p>LeetCode 通过率：约 68.9%
 */
public class DailyTemperatures {

    /**
     * 单调递减栈存下标，当前温度更高时弹出并计算天数差。
     *
     * <p>核心解法：栈存等待更高温度的下标；t[i] &gt; t[stack.peek] 时 answer[pop] = i - pop。
     *
     * <p>注意点：存下标而非温度，便于算天数；栈内温度单调递减。
     *
     * <p>疑难点：与单调队列区别——本题求「下一个更大元素」距离，栈更合适。
     */
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] answer = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                int prev = stack.pop();
                answer[prev] = i - prev;
            }
            stack.push(i);
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println("=== DailyTemperatures ===");
        DailyTemperatures s = new DailyTemperatures();
        TestHelper.checkArray("case1", new int[]{1, 1, 4, 2, 1, 1, 0, 0}, s.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
        TestHelper.checkArray("case2", new int[]{0, 0, 0}, s.dailyTemperatures(new int[]{30, 30, 30}));
        TestHelper.checkArray("case3", new int[]{1}, s.dailyTemperatures(new int[]{55, 56}));
    }
}
