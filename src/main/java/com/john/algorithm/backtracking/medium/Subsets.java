package com.john.algorithm.backtracking.medium;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 78. 子集
 *
 * <p>给你一个整数数组 nums，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。
 *
 * <p>示例：nums = [1,2,3]，输出 [[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]。
 *
 * <p>面试考频：高（回溯入门，Facebook/字节常考）
 * <p>常见公司：Meta、字节跳动、Google、Amazon
 * <p>LeetCode 通过率：约 82.3%
 */
public class Subsets {

    /**
     * 回溯：每个元素选或不选，或 start 递增避免重复组合。
     *
     * <p>核心解法：从 start 遍历，path.add(nums[i]) → 递归(i+1) → 撤销；每层都收集 path。
     *
     * <p>注意点：收集时机在进入递归前或每层循环后均可；copy path 再存入 result。
     *
     * <p>疑难点：与排列区别——子集用 start 保证 [1,2] 与 [2,1] 不重复，只往后选。
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(nums, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, int start, List<Integer> path, List<List<Integer>> result) {
        result.add(new ArrayList<>(path));
        for (int i = start; i < nums.length; i++) {
            path.add(nums[i]);
            backtrack(nums, i + 1, path, result);
            path.remove(path.size() - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Subsets ===");
        Subsets s = new Subsets();
        System.out.println("case1: " + s.subsets(new int[]{1, 2, 3}));
        System.out.println("case2: " + s.subsets(new int[]{0}));
        System.out.println("case3: " + s.subsets(new int[]{}));
    }
}
