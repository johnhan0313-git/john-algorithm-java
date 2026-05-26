package com.john.algorithm.binarytree.easy;

import com.john.algorithm.common.TreeNode;

/**
 * LeetCode 104. 二叉树的最大深度
 *
 * <p>给定一个二叉树，找出其最大深度。二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 *
 * <p>示例：root = [3,9,20,null,null,15,7]，输出 3。
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
}
