package com.john.algorithm.linkedlist;

import com.john.algorithm.common.ListNode;

/**
 * LeetCode 206. 反转链表
 *
 * <p>给你单链表的头节点 head，请你反转链表，并返回反转后的链表。
 *
 * <p>示例：head = [1,2,3,4,5]，输出 [5,4,3,2,1]。
 */
public class ReverseLinkedList {

    public ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return prev;
    }
}
