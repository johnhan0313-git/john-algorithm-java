package com.john.algorithm.array.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 238. 除自身以外数组的乘积
 *
 * <p>给你一个整数数组 nums，返回数组 answer，其中 answer[i] 等于 nums 中除 nums[i] 之外
 * 其余各元素的乘积。题目保证数组中任意元素的全部前缀元素和后缀元素的乘积都在 32 位整数范围内。
 * 要求 O(n) 时间且不使用除法。
 *
 * <p>示例：nums = [1,2,3,4]，输出 [24,12,8,6]。
 *
 * <p>面试考频：极高（Facebook/Meta 经典面经题，数组前缀积模板）
 * <p>常见公司：Meta、Google、Amazon、字节跳动、微软
 * <p>LeetCode 通过率：约 77.0%
 */
public class ProductExceptSelf {

    /**
     * 前缀积 + 后缀积，或一次遍历用输出数组存前缀再乘后缀。
     *
     * <p>核心解法：answer[i] 先存 i 左侧所有元素之积，再从右遍历乘上右侧之积。
     *
     * <p>注意点：不用除法意味着零需特殊处理，本解法天然支持零；O(1) 额外空间不含输出数组。
     *
     * <p>疑难点：为何不算两次完整前缀后缀数组？可用一个变量 suffix 滚动，空间更优。
     */
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] answer = new int[n];
        answer[0] = 1;
        for (int i = 1; i < n; i++) {
            answer[i] = answer[i - 1] * nums[i - 1];
        }
        int suffix = 1;
        for (int i = n - 1; i >= 0; i--) {
            answer[i] = answer[i] * suffix;
            suffix *= nums[i];
        }
        return answer;
    }

    public static void main(String[] args) {
        System.out.println("=== ProductExceptSelf ===");
        ProductExceptSelf s = new ProductExceptSelf();
        TestHelper.checkArray("case1", new int[]{24, 12, 8, 6}, s.productExceptSelf(new int[]{1, 2, 3, 4}));
        TestHelper.checkArray("case2", new int[]{0, 0}, s.productExceptSelf(new int[]{0, 0}));
        TestHelper.checkArray("case3", new int[]{0, 0, 9, 0}, s.productExceptSelf(new int[]{1, 2, 3, 4, 0}));
    }
}
