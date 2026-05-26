package com.john.algorithm.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 本地 main 测试辅助工具。
 */
public final class TestHelper {

    private TestHelper() {
    }

    public static ListNode list(int... vals) {
        if (vals.length == 0) {
            return null;
        }
        ListNode head = new ListNode(vals[0]);
        ListNode current = head;
        for (int i = 1; i < vals.length; i++) {
            current.next = new ListNode(vals[i]);
            current = current.next;
        }
        return head;
    }

    public static int[] listToArray(ListNode head) {
        List<Integer> values = new ArrayList<>();
        while (head != null) {
            values.add(head.val);
            head = head.next;
        }
        return values.stream().mapToInt(Integer::intValue).toArray();
    }

    public static TreeNode tree(Integer... vals) {
        if (vals.length == 0 || vals[0] == null) {
            return null;
        }
        TreeNode root = new TreeNode(vals[0]);
        List<TreeNode> queue = new ArrayList<>();
        queue.add(root);
        int index = 1;
        while (!queue.isEmpty() && index < vals.length) {
            TreeNode node = queue.remove(0);
            if (index < vals.length && vals[index] != null) {
                node.left = new TreeNode(vals[index]);
                queue.add(node.left);
            }
            index++;
            if (index < vals.length && vals[index] != null) {
                node.right = new TreeNode(vals[index]);
                queue.add(node.right);
            }
            index++;
        }
        return root;
    }

    public static void checkInt(String name, int expected, int actual) {
        System.out.println(name + ": expected=" + expected + ", actual=" + actual
                + (expected == actual ? " ✓" : " ✗"));
    }

    public static void checkLong(String name, long expected, long actual) {
        System.out.println(name + ": expected=" + expected + ", actual=" + actual
                + (expected == actual ? " ✓" : " ✗"));
    }

    public static void checkBool(String name, boolean expected, boolean actual) {
        System.out.println(name + ": expected=" + expected + ", actual=" + actual
                + (expected == actual ? " ✓" : " ✗"));
    }

    public static void checkString(String name, String expected, String actual) {
        System.out.println(name + ": expected=" + expected + ", actual=" + actual
                + (expected.equals(actual) ? " ✓" : " ✗"));
    }

    public static void checkArray(String name, int[] expected, int[] actual) {
        System.out.println(name + ": expected=" + Arrays.toString(expected)
                + ", actual=" + Arrays.toString(actual)
                + (Arrays.equals(expected, actual) ? " ✓" : " ✗"));
    }

    public static void checkList(String name, List<?> expected, List<?> actual) {
        System.out.println(name + ": expected=" + expected + ", actual=" + actual
                + (expected.equals(actual) ? " ✓" : " ✗"));
    }

    public static void checkDouble(String name, double expected, double actual) {
        System.out.println(name + ": expected=" + expected + ", actual=" + actual
                + (Math.abs(expected - actual) < 1e-9 ? " ✓" : " ✗"));
    }

    public static void printArray(String label, int[] arr) {
        System.out.println(label + ": " + Arrays.toString(arr));
    }

    public static void printChars(String label, char[][] grid) {
        System.out.println(label + ":");
        for (char[] row : grid) {
            System.out.println(Arrays.toString(row));
        }
    }
}
