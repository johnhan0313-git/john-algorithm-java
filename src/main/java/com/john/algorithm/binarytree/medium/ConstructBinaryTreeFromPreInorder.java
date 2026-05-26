package com.john.algorithm.binarytree.medium;

import com.john.algorithm.common.TestHelper;

import com.john.algorithm.common.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 105. 从前序与中序遍历序列构造二叉树
 *
 * <p>给定两个整数数组 preorder 和 inorder，其中 preorder 是二叉树的先序遍历，inorder 是同一棵树的中序遍历，
 * 请构造二叉树并返回其根节点。
 *
 * <p>示例：preorder = [3,9,20,15,7], inorder = [9,3,15,20,7]。
 *
 * <p>面试考频：极高（树递归经典，Google/Meta/字节高频）
 * <p>常见公司：Google、Meta、字节跳动、微软、Amazon
 * <p>LeetCode 通过率：约 58.9%
 */
public class ConstructBinaryTreeFromPreInorder {

    private Map<Integer, Integer> inorderIndex;

    /**
     * 前序首元素为根，在中序中找到根划分左右子树，递归构建。
     *
     * <p>核心解法：HashMap 存 inorder 值→下标；build(preL,preR,inL,inR) 递归划分区间。
     *
     * <p>注意点：preorder 长度与 inorder 相同且无重复；左子树节点数 = rootIn - inL。
     *
     * <p>疑难点：区间边界 preLeft+leftSize 是左子树前序的右边界（开区间或闭区间需一致）。
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        inorderIndex = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderIndex.put(inorder[i], i);
        }
        return build(preorder, 0, preorder.length - 1, 0, inorder.length - 1);
    }

    private TreeNode build(int[] preorder, int preLeft, int preRight, int inLeft, int inRight) {
        if (preLeft > preRight) {
            return null;
        }
        int rootVal = preorder[preLeft];
        TreeNode root = new TreeNode(rootVal);
        int rootIn = inorderIndex.get(rootVal);
        int leftSize = rootIn - inLeft;
        root.left = build(preorder, preLeft + 1, preLeft + leftSize, inLeft, rootIn - 1);
        root.right = build(preorder, preLeft + leftSize + 1, preRight, rootIn + 1, inRight);
        return root;
    }

    public static void main(String[] args) {
        System.out.println("=== ConstructBinaryTreeFromPreInorder ===");
        ConstructBinaryTreeFromPreInorder s = new ConstructBinaryTreeFromPreInorder();
        com.john.algorithm.common.TreeNode r1 = s.buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        TestHelper.checkInt("case1 root", 3, r1.val);
        TestHelper.checkInt("case1 left", 9, r1.left.val);
        com.john.algorithm.common.TreeNode r2 = s.buildTree(new int[]{1}, new int[]{1});
        TestHelper.checkInt("case2 root", 1, r2.val);
        TestHelper.checkBool("case3 null", true, s.buildTree(new int[]{}, new int[]{}) == null);
    }
}
