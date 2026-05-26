package com.john.algorithm.backtracking.medium;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * LeetCode 46. 全排列
 *
 * <p>给定一个不含重复数字的数组 nums，返回其所有可能的全排列。你可以按任意顺序返回答案。
 *
 * <p>示例：nums = [1,2,3]，输出 [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]。
 *
 * <p>面试考频：极高（回溯算法入门标杆，Google/Meta 高频）
 * <p>常见公司：Google、Meta、Amazon、字节跳动、微软
 * <p>LeetCode 通过率：约 72.7%
 */
public class Permutations {

    /**
     * 回溯：用 used 数组标记已选元素，path 记录当前排列。
     *
     * <p>核心解法：path 长度等于 nums.length 时收集结果；否则遍历每个未使用的 nums[i]，
     * 做选择 → 递归 → 撤销选择。
     *
     * <p>注意点：收集结果时必须 new ArrayList&lt;&gt;(path)，否则后续修改 path 会影响已存结果；
     * 撤销时先 remove 再 used[i] = false，顺序与选择相反。
     *
     * <p>疑难点：回溯与 DFS 的区别？此处 DFS 是搜索框架，回溯强调「撤销状态」以复用 path，
     * 避免每层 new 列表带来的额外开销。
     */
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

    public static void main(String[] args) {
        System.out.println("=== Permutations ===");
        Permutations s = new Permutations();
        System.out.println("case1 size: " + s.permute(new int[]{1, 2, 3}).size());
        System.out.println("case2: " + s.permute(new int[]{0, 1}));
        System.out.println("case3: " + s.permute(new int[]{1}));
    }
}
