package com.john.algorithm.hashmap.medium;

import com.john.algorithm.common.TestHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * LeetCode 128. 最长连续序列
 *
 * <p>给定一个未排序的整数数组 nums，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 要求 O(n) 时间复杂度。
 *
 * <p>示例：nums = [100,4,200,1,3,2]，输出 4（连续序列 [1,2,3,4]）。
 *
 * <p>面试考频：极高（Google 面经 Top 题，考察哈希集合思维）
 * <p>常见公司：Google、字节跳动、LinkedIn、Amazon
 * <p>LeetCode 通过率：约 49.3%
 */
public class LongestConsecutiveSequence {

    /**
     * HashSet 存所有数，只从序列起点（num-1 不存在）开始向后数。
     *
     * <p>核心解法：对每个 num，若 num-1 不在 set 中，则 while num+k in set 扩展长度。
     *
     * <p>注意点：每个数最多被访问两次，均摊 O(n)；排序解法 O(n log n) 不符合要求。
     *
     * <p>疑难点：为何只从起点开始？避免 O(n²) 重复计数，每个连续段只遍历一次。
     */
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int maxLen = 0;
        for (int num : set) {
            if (set.contains(num - 1)) {
                continue;
            }
            int current = num;
            int len = 1;
            while (set.contains(current + 1)) {
                current++;
                len++;
            }
            maxLen = Math.max(maxLen, len);
        }
        return maxLen;
    }

    public static void main(String[] args) {
        System.out.println("=== LongestConsecutiveSequence ===");
        LongestConsecutiveSequence s = new LongestConsecutiveSequence();
        TestHelper.checkInt("case1", 4, s.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
        TestHelper.checkInt("case2", 1, s.longestConsecutive(new int[]{0, -1}));
        TestHelper.checkInt("case3", 0, s.longestConsecutive(new int[]{}));
    }
}
