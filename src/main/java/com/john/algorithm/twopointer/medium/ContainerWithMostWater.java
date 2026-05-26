package com.john.algorithm.twopointer.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 11. 盛最多水的容器
 *
 * <p>给定一个长度为 n 的整数数组 height，有 n 条垂线，第 i 条线的两个端点是 (i, 0) 和 (i, height[i])。
 * 找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 *
 * <p>示例：height = [1,8,6,2,5,4,8,3,7]，输出 49。
 *
 * <p>面试考频：高（双指针经典题，Google/Meta 面经高频）
 * <p>常见公司：Google、Meta、Amazon、字节跳动、微软
 * <p>LeetCode 通过率：约 57.6%
 */
public class ContainerWithMostWater {

    /**
     * 左右双指针从两端向中间收缩，每次移动较短的那一侧。
     *
     * <p>核心解法：面积 = min(height[left], height[right]) × (right - left)。
     * 宽度随收缩单调减小，只有移动较短边才有可能找到更大面积。
     *
     * <p>注意点：移动较长边宽度必减且高度不会更高，面积一定变小，可以安全跳过；
     * 相等时移动哪一侧均可。
     *
     * <p>疑难点：为什么移动短边不会漏解？任何更优解必须包含当前较短的边，
     * 固定短边后只有扩大宽度（移动长边）才可能提升高度，但宽度已在减小，
     * 因此当前状态下移动短边是唯一能探索更优解的方向。
     */
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int area = Math.min(height[left], height[right]) * (right - left);
            maxArea = Math.max(maxArea, area);
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return maxArea;
    }

    public static void main(String[] args) {
        System.out.println("=== ContainerWithMostWater ===");
        ContainerWithMostWater s = new ContainerWithMostWater();
        TestHelper.checkInt("case1", 49, s.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
        TestHelper.checkInt("case2", 1, s.maxArea(new int[]{1, 1}));
        TestHelper.checkInt("case3", 16, s.maxArea(new int[]{4, 3, 2, 1, 4}));
    }
}
