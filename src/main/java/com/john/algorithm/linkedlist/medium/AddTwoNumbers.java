package com.john.algorithm.linkedlist.medium;

import com.john.algorithm.common.TestHelper;

import com.john.algorithm.common.ListNode;

/**
 * LeetCode 2. 两数相加
 *
 * <p>给你两个非空的链表，表示两个非负的整数。数字按逆序存储，每个节点存储一个数字。
 * 将两个数相加并以相同形式返回。假设除了数字 0 之外，这两个数都不含前导零。
 *
 * <p>示例：l1 = [2,4,3], l2 = [5,6,4]，输出 [7,0,8]（342+465=807）。
 *
 * <p>面试考频：高（链表模拟加法，Meta/Amazon 常考）
 * <p>常见公司：Meta、Amazon、Google、阿里巴巴
 * <p>LeetCode 通过率：约 46.2%
 */
public class AddTwoNumbers {

    /**
     * 同步遍历两链表，维护进位 carry。
     *
     * <p>核心解法：sum = val1 + val2 + carry，新节点 sum % 10，carry = sum / 10；一链走完继续处理剩余链和 carry。
     *
     * <p>注意点：最后 carry==1 需再建节点；两链长度不同正常处理 null 为 0。
     *
     * <p>疑难点：while (l1!=null || l2!=null || carry!=0) 统一循环条件，避免遗漏末尾进位。
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        int carry = 0;
        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;
            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }
            current.next = new ListNode(sum % 10);
            current = current.next;
            carry = sum / 10;
        }
        return dummy.next;
    }

    public static void main(String[] args) {
        System.out.println("=== AddTwoNumbers ===");
        AddTwoNumbers s = new AddTwoNumbers();
        TestHelper.checkArray("case1", new int[]{7, 0, 8}, TestHelper.listToArray(s.addTwoNumbers(TestHelper.list(2, 4, 3), TestHelper.list(5, 6, 4))));
        TestHelper.checkArray("case2", new int[]{0}, TestHelper.listToArray(s.addTwoNumbers(TestHelper.list(0), TestHelper.list(0))));
        TestHelper.checkArray("case3", new int[]{8, 1}, TestHelper.listToArray(s.addTwoNumbers(TestHelper.list(9, 9), TestHelper.list(1))));
    }
}
