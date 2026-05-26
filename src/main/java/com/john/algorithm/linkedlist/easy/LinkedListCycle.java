package com.john.algorithm.linkedlist.easy;

import com.john.algorithm.common.TestHelper;

import com.john.algorithm.common.ListNode;

/**
 * LeetCode 141. 环形链表
 *
 * <p>给定一个链表 head，判断链表中是否有环。如果链表中存在环，则返回 true，否则返回 false。
 *
 * <p>示例：head = [3,2,0,-4], pos = 1（尾连到 index 1），输出 true。
 *
 * <p>面试考频：极高（Floyd 判环，大厂链表题基础）
 * <p>常见公司：Amazon、Google、字节跳动、微软、Apple
 * <p>LeetCode 通过率：约 54.5%
 */
public class LinkedListCycle {

    /**
     * Floyd 快慢指针：快指针走两步，慢指针走一步，相遇则有环。
     *
     * <p>核心解法：slow=head, fast=head，while fast!=null && fast.next!=null，同步移动，相等则 true。
     *
     * <p>注意点：fast 判 null 和 fast.next 判 null；无环时 fast 先到达 null。
     *
     * <p>疑难点：为何相遇必在环内？相对速度为 1，环内必追及；找环入口见 LC 142。
     */
    public boolean hasCycle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("=== LinkedListCycle ===");
        LinkedListCycle s = new LinkedListCycle();
        TestHelper.checkBool("case1 no cycle", false, s.hasCycle(TestHelper.list(1, 2, 3)));
        com.john.algorithm.common.ListNode cycle = TestHelper.list(3, 2, 0, -4);
        cycle.next.next.next.next = cycle.next;
        TestHelper.checkBool("case2 has cycle", true, s.hasCycle(cycle));
        TestHelper.checkBool("case3 single", false, s.hasCycle(TestHelper.list(1)));
    }
}
