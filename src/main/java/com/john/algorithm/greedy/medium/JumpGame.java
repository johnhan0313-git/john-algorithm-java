package com.john.algorithm.greedy.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 55. 跳跃游戏
 *
 * <p>给定一个非负整数数组 nums，你最初位于数组的第一个下标。数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 判断你是否能够到达最后一个下标。
 *
 * <p>示例：nums = [2,3,1,1,4]，输出 true。
 *
 * <p>面试考频：高（贪心经典，Google/Amazon 常考）
 * <p>常见公司：Google、Amazon、字节跳动、微软
 * <p>LeetCode 通过率：约 43.5%
 */
public class JumpGame {

    /**
     * 维护能到达的最远位置 farthest。
     *
     * <p>核心解法：遍历 i，若 i &gt; farthest 则不可达；否则 farthest = max(farthest, i + nums[i])。
     *
     * <p>注意点：空数组/单元素返回 true；nums[i] 可为 0 但 farthest 可能仍够。
     *
     * <p>疑难点：与 LC 45 最少步数区别——本题只需判可达，不需最小化步数。
     */
    public boolean canJump(int[] nums) {
        int farthest = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > farthest) {
                return false;
            }
            farthest = Math.max(farthest, i + nums[i]);
            if (farthest >= nums.length - 1) {
                return true;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== JumpGame ===");
        JumpGame s = new JumpGame();
        TestHelper.checkBool("case1", true, s.canJump(new int[]{2, 3, 1, 1, 4}));
        TestHelper.checkBool("case2", false, s.canJump(new int[]{3, 2, 1, 0, 4}));
        TestHelper.checkBool("case3", true, s.canJump(new int[]{0}));
    }
}
