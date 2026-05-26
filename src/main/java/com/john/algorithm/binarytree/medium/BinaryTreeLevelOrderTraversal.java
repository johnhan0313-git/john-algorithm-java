package com.john.algorithm.binarytree.medium;

import com.john.algorithm.common.TestHelper;

import com.john.algorithm.common.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * LeetCode 102. 二叉树的层序遍历
 *
 * <p>给你二叉树的根节点 root，返回其节点值的层序遍历（即逐层从左到右访问所有节点）。
 *
 * <p>示例：root = [3,9,20,null,null,15,7]，输出 [[3],[9,20],[15,7]]。
 *
 * <p>面试考频：极高（BFS 模板，几乎所有大厂必考）
 * <p>常见公司：字节跳动、腾讯、Google、Amazon、Meta
 * <p>LeetCode 通过率：约 68.2%
 */
public class BinaryTreeLevelOrderTraversal {

    /**
     * BFS 队列逐层处理，记录每层节点数。
     *
     * <p>核心解法：queue 入 root，while 非空，for 循环 size 次 poll 当前层节点并 push 子节点。
     *
     * <p>注意点：for 循环 bound 用 queue.size() 快照当前层大小；null root 返回空列表。
     *
     * <p>疑难点：层序与 DFS 前中后序不同——队列保证先进先出实现广度优先。
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            result.add(level);
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println("=== BinaryTreeLevelOrderTraversal ===");
        BinaryTreeLevelOrderTraversal s = new BinaryTreeLevelOrderTraversal();
        System.out.println("case1: " + s.levelOrder(TestHelper.tree(3, 9, 20, null, null, 15, 7)));
        System.out.println("case2: " + s.levelOrder(TestHelper.tree(1)));
        System.out.println("case3: " + s.levelOrder(null));
    }
}
