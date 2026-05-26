package com.john.algorithm.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 46. 全排列
 *
 * <p>给定一个不含重复数字的数组 nums，返回其所有可能的全排列。你可以按任意顺序返回答案。
 *
 * <p>示例：nums = [1,2,3]，输出 [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]。
 */
public class Permutations {

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(nums, used, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> result) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            used[i] = true;
            path.add(nums[i]);
            backtrack(nums, used, path, result);
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }
}
