package com.john.algorithm.linkedlist.easy;

import com.john.algorithm.common.ListNode;

/**
 * LeetCode 206. 反转链表
 *
 * <p>给你单链表的头节点 head，请你反转链表，并返回反转后的链表。
 *
 * <p>示例：head = [1,2,3,4,5]，输出 [5,4,3,2,1]。
 */
public class ReverseLinkedList {

    /**
     * 迭代三指针：prev、current、next。
     *
     * <p>核心解法：遍历链表，将 current.next 指向 prev，然后三个指针同步前移。
     * 结束后 prev 即为新头节点。
     *
     * <p>注意点：必须先保存 next = current.next，再改 current.next，否则链表断裂；
     * 空链表直接返回 null（prev 初始为 null 时循环不执行，仍正确）。
     *
     * <p>疑难点：为何返回 prev 而非 current？循环结束时 current 已为 null，
     * prev 停在原链表最后一个节点，即反转后的头节点。
     */
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
