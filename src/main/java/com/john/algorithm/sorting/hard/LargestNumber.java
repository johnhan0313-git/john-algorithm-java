package com.john.algorithm.sorting.hard;

import com.john.algorithm.common.TestHelper;

import java.util.Arrays;

/**
 * LeetCode 179. 最大数
 *
 * <p>给定一组非负整数 nums，重新排列每个数的顺序（每个数不可拆分）使之组成一个最大的整数。
 * 输出结果可能非常大，直接返回字符串形式。
 *
 * <p>示例：nums = [3,30,34,5,9]，输出 "9534330"。
 *
 * <p>面试考频：中（自定义排序 Hard，Google/字节 occasional）
 * <p>常见公司：Google、字节跳动、Amazon
 * <p>LeetCode 通过率：约 38.9%
 */
public class LargestNumber {

    /**
     * 自定义比较：a+b 与 b+a 字典序，大的排前。
     *
     * <p>核心解法：转字符串数组，排序 (a,b) -> (b+a).compareTo(a+b)；拼接，首字符 '0' 则返回 "0"。
     *
     * <p>注意点：全 0 特判返回 "0"；比较器用 Long 可能溢出，用字符串 compareTo。
     *
     * <p>疑难点：为何不是按长度排？30 vs 3 应排 3 前因 "330" &gt; "303"。
     */
    public String largestNumber(int[] nums) {
        String[] strs = new String[nums.length];
        for (int i = 0; i < nums.length; i++) {
            strs[i] = String.valueOf(nums[i]);
        }
        Arrays.sort(strs, (a, b) -> (b + a).compareTo(a + b));
        if (strs[0].equals("0")) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        for (String s : strs) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== LargestNumber ===");
        LargestNumber s = new LargestNumber();
        TestHelper.checkString("case1", "9534330", s.largestNumber(new int[]{3, 30, 34, 5, 9}));
        TestHelper.checkString("case2", "0", s.largestNumber(new int[]{0, 0}));
        TestHelper.checkString("case3", "210", s.largestNumber(new int[]{2, 10}));
    }
}
