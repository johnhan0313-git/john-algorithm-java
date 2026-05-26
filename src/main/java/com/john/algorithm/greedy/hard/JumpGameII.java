package com.john.algorithm.greedy.hard;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 45. 跳跃游戏 II
 *
 * <p>给定一个长度为 n 的 0 索引整数数组 nums。初始位置为 nums[0]。
 * 每个元素 nums[i] 表示从索引 i 向前最多跳 nums[i] 步。返回到达 nums[n - 1] 的最小跳跃次数。
 * 题目保证可以到达。
 *
 * <p>示例：nums = [2,3,1,1,4]，输出 2（2 步到 index 4）。
 *
 * <p>面试考频：高（贪心进阶，与 LC 55 常成对出现）
 * <p>常见公司：Google、Amazon、字节跳动
 * <p>LeetCode 通过率：约 44.2%
 */
public class JumpGameII {

    /**
     * 当前步数边界 end，下一跳最远 nextEnd，遍历时扩展 nextEnd。
     *
     * <p>核心解法：i 到达 end 时 jumps++，end = nextEnd；nextEnd = max(nextEnd, i+nums[i])。
     *
     * <p>注意点：循环到 n-2 即可（最后一格不必跳）；题目保证可达故无需判 false。
     *
     * <p>疑难点：在 [currentEnd, nextEnd] 范围内选最优下一跳——贪心扩展 nextEnd 即 BFS 层序思想。
     */
    public int jump(int[] nums) {
        int jumps = 0;
        int currentEnd = 0;
        int nextEnd = 0;
        for (int i = 0; i < nums.length - 1; i++) {
            nextEnd = Math.max(nextEnd, i + nums[i]);
            if (i == currentEnd) {
                jumps++;
                currentEnd = nextEnd;
            }
        }
        return jumps;
    }

    public static void main(String[] args) {
        System.out.println("=== JumpGameII ===");
        JumpGameII s = new JumpGameII();
        TestHelper.checkInt("case1", 2, s.jump(new int[]{2, 3, 1, 1, 4}));
        TestHelper.checkInt("case2", 1, s.jump(new int[]{1, 1, 1, 1}));
        TestHelper.checkInt("case3", 1, s.jump(new int[]{1}));
    }
}
