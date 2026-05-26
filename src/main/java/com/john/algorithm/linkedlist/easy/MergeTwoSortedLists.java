package com.john.algorithm.linkedlist.easy;

import com.john.algorithm.common.TestHelper;

import com.john.algorithm.common.ListNode;

/**
 * LeetCode 21. 合并两个有序链表
 *
 * <p>将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 *
 * <p>示例：list1 = [1,2,4], list2 = [1,3,4]，输出 [1,1,2,3,4,4]。
 *
 * <p>面试考频：极高（链表入门必考，Amazon/Google 热身题）
 * <p>常见公司：Amazon、Google、微软、字节跳动、腾讯
 * <p>LeetCode 通过率：约 68.5%
 */
public class MergeTwoSortedLists {

    /**
     * 哑节点 + 逐节点比较拼接。
     *
     * <p>核心解法：dummy 头节点，current 指针，每次接较小节点，剩余链表直接接上。
     *
     * <p>注意点：其中一个为空时直接返回另一个；不要 new 新节点可原地改 next（本题通常新建或复用均可）。
     *
     * <p>疑难点：递归写法更简洁但 O(n) 栈空间；迭代 O(1) 空间更优。
     */
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        current.next = list1 != null ? list1 : list2;
        return dummy.next;
    }

    public static void main(String[] args) {
        System.out.println("=== MergeTwoSortedLists ===");
        MergeTwoSortedLists s = new MergeTwoSortedLists();
        TestHelper.checkArray("case1", new int[]{1, 1, 2, 3, 4, 4}, TestHelper.listToArray(s.mergeTwoLists(TestHelper.list(1, 2, 4), TestHelper.list(1, 3, 4))));
        TestHelper.checkArray("case2", new int[]{1}, TestHelper.listToArray(s.mergeTwoLists(null, TestHelper.list(1))));
        TestHelper.checkArray("case3", new int[]{}, TestHelper.listToArray(s.mergeTwoLists(null, null)));
    }
}
