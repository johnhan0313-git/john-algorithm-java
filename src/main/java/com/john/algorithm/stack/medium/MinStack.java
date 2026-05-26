package com.john.algorithm.stack.medium;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 155. 最小栈
 *
 * <p>设计一个支持 push、pop、top 操作，并能在常数时间内检索到最小元素的栈。
 *
 * <p>示例：MinStack minStack = new MinStack(); minStack.push(-2); minStack.getMin() 返回 -2。
 *
 * <p>面试考频：高（设计题经典，Amazon/微软常考）
 * <p>常见公司：Amazon、微软、Google、Bloomberg
 * <p>LeetCode 通过率：约 55.6%
 */
public class MinStack {

    private final Deque<Integer> stack;
    private final Deque<Integer> minStack;

    public MinStack() {
        stack = new ArrayDeque<>();
        minStack = new ArrayDeque<>();
    }

    /**
     * 辅助栈同步压入当前最小值。
     *
     * <p>核心解法：minStack 栈顶始终是主栈当前最小值；push 时 minStack.push(min(val, minStack.peek))。
     *
     * <p>注意点：pop 时两栈同步 pop；minStack 可存重复最小值。
     *
     * <p>疑难点：也可用一个栈存差值编码两个栈信息，但双栈最直观。
     */
    public void push(int val) {
        stack.push(val);
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }

    public void pop() {
        if (stack.pop().equals(minStack.peek())) {
            minStack.pop();
        }
    }

    public int top() {
        return stack.peek();
    }

    public int getMin() {
        return minStack.peek();
    }

    public static void main(String[] args) {
        System.out.println("=== MinStack ===");
        MinStack st = new MinStack();
        st.push(-2); st.push(0); st.push(-3);
        TestHelper.checkInt("getMin", -3, st.getMin());
        st.pop();
        TestHelper.checkInt("top", 0, st.top());
        TestHelper.checkInt("getMin after pop", -2, st.getMin());
    }
}
