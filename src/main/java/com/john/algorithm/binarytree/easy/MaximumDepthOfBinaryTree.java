package com.john.algorithm.binarytree.easy;

import com.john.algorithm.common.TestHelper;

import com.john.algorithm.common.TreeNode;

/**
 * LeetCode 104. 二叉树的最大深度
 *
 * <p>给定一个二叉树，找出其最大深度。二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 *
 * <p>示例：root = [3,9,20,null,null,15,7]，输出 3。
 *
 * <p>面试考频：极高（二叉树递归入门，Amazon/Google 必考）
 * <p>常见公司：Amazon、Google、字节跳动、Meta、微软
 * <p>LeetCode 通过率：约 77.5%
 */
public class MaximumDepthOfBinaryTree {

    /**
     * 递归：树的最大深度 = 1 + max(左子树深度, 右子树深度)。
     *
     * <p>核心解法：后序思想，先递归左右子树，再在当前层 +1。
     * 空节点深度为 0，作为递归基。
     *
     * <p>注意点：深度定义是节点数而非边数；单节点树深度为 1；
     * 递归深度等于树高，极端情况下可能栈溢出，可改 BFS/DFS 迭代。
     *
     * <p>疑难点：为何取 max 而非 sum？深度是「最长路径」，左右只需取较大分支，
     * 不是左右路径长度相加。
     */
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    public static void main(String[] args) {
        System.out.println("=== MaximumDepthOfBinaryTree ===");
        MaximumDepthOfBinaryTree s = new MaximumDepthOfBinaryTree();
        TestHelper.checkInt("case1", 3, s.maxDepth(TestHelper.tree(3, 9, 20, null, null, 15, 7)));
        TestHelper.checkInt("case2", 0, s.maxDepth(null));
        TestHelper.checkInt("case3", 2, s.maxDepth(TestHelper.tree(1, null, 2)));
    }
}
