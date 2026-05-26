package com.john.algorithm.math.medium;

import com.john.algorithm.common.TestHelper;

/**
 * LeetCode 7. 整数反转
 *
 * <p>给你一个 32 位有符号整数 x，返回将 x 中数字部分反转后的结果。如果反转后整数溢出，返回 0。
 *
 * <p>示例：x = 123，输出 321；x = -123，输出 -321。
 *
 * <p>面试考频：高（数学/溢出处理，Apple/Amazon 常考）
 * <p>常见公司：Apple、Amazon、Google、微软
 * <p>LeetCode 通过率：约 35.6%
 */
public class ReverseInteger {

    /**
     * 逐位取模构建 reversed，每次检查是否溢出。
     *
     * <p>核心解法：rev = rev * 10 + x % 10; x /= 10；若 rev &gt; Integer.MAX_VALUE/10 或边界相等且个位过大则返回 0。
     *
     * <p>注意点：负数同样处理；溢出判断在乘 10 前进行。
     *
     * <p>疑难点：INT_MAX=2147483647，末位最大 7；INT_MIN 反转亦溢出返回 0。
     */
    public int reverse(int x) {
        int reversed = 0;
        while (x != 0) {
            int pop = x % 10;
            x /= 10;
            if (reversed > Integer.MAX_VALUE / 10
                    || (reversed == Integer.MAX_VALUE / 10 && pop > 7)) {
                return 0;
            }
            reversed = reversed * 10 + pop;
        }
        return reversed;
    }

    public static void main(String[] args) {
        System.out.println("=== ReverseInteger ===");
        ReverseInteger s = new ReverseInteger();
        TestHelper.checkInt("case1", 321, s.reverse(123));
        TestHelper.checkInt("case2", -321, s.reverse(-123));
        TestHelper.checkInt("case3", 0, s.reverse(1534236469));
    }
}
