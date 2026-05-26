package com.john.algorithm.twopointer.hard;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 42. 接雨水
 *
 * <p>给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 *
 * <p>示例：height = [0,1,0,2,1,0,1,3,2,1,2,1]，输出 6。
 *
 * <p>面试考频：极高（Google/Amazon 高频 Hard，双指针经典）
 * <p>常见公司：Google、Amazon、字节跳动、微软、Meta
 * <p>LeetCode 通过率：约 58.4%
 */
public class TrappingRainWater {

    /**
     * 双指针：维护 leftMax、rightMax，较小侧决定当前位置接水量。
     *
     * <p>核心解法：left/right 向中间移动，height[left] &lt; height[right] 时，
     * 若 height[left] &gt;= leftMax 更新 leftMax，否则累加 leftMax - height[left]。
     *
     * <p>注意点：比较的是 height[left] 与 height[right] 决定处理哪侧，不是比较 max。
     *
     * <p>疑难点：为何较小侧可以安全计算？较小侧水位由另一侧较高柱保证，与对侧 max 无关。
     */
    public int trap(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int water = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    water += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    water += rightMax - height[right];
                }
                right--;
            }
        }
        return water;
    }

    public static void main(String[] args) {
        System.out.println("=== TrappingRainWater ===");
        TrappingRainWater s = new TrappingRainWater();
        TestHelper.checkInt("case1", 6, s.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
        TestHelper.checkInt("case2", 0, s.trap(new int[]{0, 0, 0}));
        TestHelper.checkInt("case3", 9, s.trap(new int[]{4, 2, 0, 3, 2, 5}));
    }
}
