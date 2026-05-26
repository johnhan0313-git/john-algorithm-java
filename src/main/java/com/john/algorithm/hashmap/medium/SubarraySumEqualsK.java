package com.john.algorithm.hashmap.medium;

import com.john.algorithm.common.TestHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * LeetCode 560. 和为 K 的子数组
 *
 * <p>给你一个整数数组 nums 和一个整数 k，请你统计并返回该数组中和为 k 的连续子数组的个数。
 *
 * <p>示例：nums = [1,1,1], k = 2，输出 2。
 *
 * <p>面试考频：高（前缀和 + 哈希表经典，Facebook 常考）
 * <p>常见公司：Meta、Google、字节跳动、拼多多
 * <p>LeetCode 通过率：约 44.8%
 */
public class SubarraySumEqualsK {

    /**
     * 前缀和 + 哈希表统计 prefixSum - k 出现次数。
     *
     * <p>核心解法：map 存前缀和频次，初始 map.put(0,1)；遍历累加 sum，count += map.getOrDefault(sum-k,0)，再更新 sum。
     *
     * <p>注意点：含负数时滑动窗口失效，必须用前缀和；0 前缀初始化处理从头开始的子数组。
     *
     * <p>疑难点：子数组 [i,j] 和为 k 等价于 prefix[j] - prefix[i-1] = k，即找 prefix[i-1] = prefix[j] - k。
     */
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> prefixCount = new HashMap<>();
        prefixCount.put(0, 1);
        int sum = 0;
        int count = 0;
        for (int num : nums) {
            sum += num;
            count += prefixCount.getOrDefault(sum - k, 0);
            prefixCount.put(sum, prefixCount.getOrDefault(sum, 0) + 1);
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== SubarraySumEqualsK ===");
        SubarraySumEqualsK s = new SubarraySumEqualsK();
        TestHelper.checkInt("case1", 2, s.subarraySum(new int[]{1, 1, 1}, 2));
        TestHelper.checkInt("case2", 1, s.subarraySum(new int[]{1, 2, 3}, 3));
        TestHelper.checkInt("case3", 0, s.subarraySum(new int[]{1}, 0));
    }
}
