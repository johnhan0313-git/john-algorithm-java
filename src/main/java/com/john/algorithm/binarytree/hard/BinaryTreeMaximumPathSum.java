package com.john.algorithm.binarytree.hard;

import com.john.algorithm.common.TestHelper;

import com.john.algorithm.common.TreeNode;

/**
 * LeetCode 124. 二叉树中的最大路径和
 *
 * <p>二叉树中的路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。
 * 同一个节点在一条路径序列中至多出现一次。路径和是路径上各节点值之和。
 * 给你一个二叉树的根节点 root，返回其最大路径和。
 *
 * <p>示例：root = [-10,9,20,null,null,15,7]，输出 42（路径 15→20→7）。
 *
 * <p>面试考频：极高（Google Hard 经典，树形 DP 代表）
 * <p>常见公司：Google、Meta、字节跳动、微软
 * <p>LeetCode 通过率：约 47.8%
 */
public class BinaryTreeMaximumPathSum {

    private int maxSum;

    /**
     * 后序 DFS：返回以 node 为端点的单边最大贡献，全局更新过 node 的「拱桥路径」。
     *
     * <p>核心解法：gain = max(0, leftGain) + node.val + max(0, rightGain) 更新 maxSum；
     * 返回 node.val + max(0, leftGain, rightGain) 供父节点接单边。
     *
     * <p>注意点：节点值可能为负，max(0, gain) 表示不选负贡献子树；maxSum 初始 Integer.MIN_VALUE。
     *
     * <p>疑难点：拱桥路径不能同时走左右子树到父的上一层——返回给父的只能选一边。
     */
    public int maxPathSum(TreeNode root) {
        maxSum = Integer.MIN_VALUE;
        maxGain(root);
        return maxSum;
    }

    private int maxGain(TreeNode node) {
        if (node == null) {
            return 0;
        }
        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);
        maxSum = Math.max(maxSum, node.val + leftGain + rightGain);
        return node.val + Math.max(leftGain, rightGain);
    }

    public static void main(String[] args) {
        System.out.println("=== BinaryTreeMaximumPathSum ===");
        BinaryTreeMaximumPathSum s = new BinaryTreeMaximumPathSum();
        TestHelper.checkInt("case1", 42, s.maxPathSum(TestHelper.tree(-10, 9, 20, null, null, 15, 7)));
        TestHelper.checkInt("case2", 2, s.maxPathSum(TestHelper.tree(2, -1)));
        TestHelper.checkInt("case3", 3, s.maxPathSum(TestHelper.tree(1, 2, 3)));
    }
}
