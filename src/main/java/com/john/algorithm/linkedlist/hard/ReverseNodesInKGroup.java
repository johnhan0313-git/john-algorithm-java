package com.john.algorithm.linkedlist.hard;

import com.john.algorithm.common.TestHelper;

import com.john.algorithm.common.ListNode;

/**
 * LeetCode 25. K 个一组翻转链表
 *
 * <p>给你链表的头节点 head，每 k 个节点一组进行翻转，返回修改后的链表。k 为正值的整数。
 * 不足 k 个的末尾节点保持原样。
 *
 * <p>示例：head = [1,2,3,4,5], k = 2，输出 [2,1,4,3,5]。
 *
 * <p>面试考频：高（Hard 链表标杆，字节/Google 高频）
 * <p>常见公司：字节跳动、Google、Amazon、Facebook
 * <p>LeetCode 通过率：约 52.8%
 */
public class ReverseNodesInKGroup {

    /**
     * 分段反转：先探测 k 个节点是否存在，存在则反转并连接前后段。
     *
     * <p>核心解法：dummy 前置；每组 prev=段前节点，反转 k 个后 prev.next=新尾，prev=新尾继续。
     *
     * <p>注意点：先 count k 个再反转，不足 k 不动；反转后 tail 连向下组 head。
     *
     * <p>疑难点：指针连接顺序易错——反转后 groupPrev.next=新头，旧头(新尾).next=下一组头。
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummy = new ListNode(0, head);
        ListNode groupPrev = dummy;
        while (true) {
            ListNode kth = getKth(groupPrev, k);
            if (kth == null) {
                break;
            }
            ListNode groupNext = kth.next;
            ListNode prev = groupNext;
            ListNode current = groupPrev.next;
            while (current != groupNext) {
                ListNode next = current.next;
                current.next = prev;
                prev = current;
                current = next;
            }
            ListNode tmp = groupPrev.next;
            groupPrev.next = kth;
            groupPrev = tmp;
        }
        return dummy.next;
    }

    private ListNode getKth(ListNode current, int k) {
        while (current != null && k > 0) {
            current = current.next;
            k--;
        }
        return current;
    }

    public static void main(String[] args) {
        System.out.println("=== ReverseNodesInKGroup ===");
        ReverseNodesInKGroup s = new ReverseNodesInKGroup();
        TestHelper.checkArray("case1", new int[]{2, 1, 4, 3, 5}, TestHelper.listToArray(s.reverseKGroup(TestHelper.list(1, 2, 3, 4, 5), 2)));
        TestHelper.checkArray("case2", new int[]{1, 2, 3}, TestHelper.listToArray(s.reverseKGroup(TestHelper.list(1, 2, 3), 1)));
        TestHelper.checkArray("case3", new int[]{3, 2, 1}, TestHelper.listToArray(s.reverseKGroup(TestHelper.list(1, 2, 3), 3)));
    }
}
