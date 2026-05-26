package com.john.algorithm.queue.easy;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 232. 用栈实现队列
 *
 * <p>请你仅使用两个栈实现先入先出队列。队列应当支持 push、pop、peek、empty 操作。
 *
 * <p>示例：push(1), push(2), peek() 1, pop() 1, empty() false。
 *
 * <p>面试考频：中（数据结构基础，考察栈队列转换）
 * <p>常见公司：Amazon、微软、Adobe
 * <p>LeetCode 通过率：约 62.1%
 */
public class MyQueue {

    private final Deque<Integer> inStack;
    private final Deque<Integer> outStack;

    public MyQueue() {
        inStack = new ArrayDeque<>();
        outStack = new ArrayDeque<>();
    }

    /**
     * 双栈：入队 push 到 inStack，出队时若 outStack 空则将 inStack 全部倒入 outStack。
     *
     * <p>核心解法：摊还 O(1)——每个元素最多入栈出栈各两次。
     *
     * <p>注意点：pop/peek 前需 pour；empty 判两栈皆空。
     *
     * <p>疑难点：一次性倒入保证 FIFO 顺序——inStack 栈顶变 outStack 栈底，先进入的先出。
     */
    public void push(int x) {
        inStack.push(x);
    }

    public int pop() {
        pour();
        return outStack.pop();
    }

    public int peek() {
        pour();
        return outStack.peek();
    }

    public boolean empty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }

    private void pour() {
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== MyQueue ===");
        MyQueue q = new MyQueue();
        q.push(1); q.push(2);
        TestHelper.checkInt("peek", 1, q.peek());
        TestHelper.checkInt("pop", 1, q.pop());
        TestHelper.checkBool("empty false", false, q.empty());
        TestHelper.checkInt("pop", 2, q.pop());
        TestHelper.checkBool("empty true", true, q.empty());
    }
}
