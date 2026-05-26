package com.john.algorithm.string.medium;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 394. 字符串解码
 *
 * <p>给定一个经过编码的字符串，返回它解码后的字符串。编码规则为 k[encoded_string]，
 * 表示 encoded_string 重复 k 次。
 *
 * <p>示例：s = "3[a]2[bc]"，输出 "aaabcbc"。
 *
 * <p>面试考频：高（栈处理嵌套结构，Google/字节常考）
 * <p>常见公司：Google、字节跳动、Amazon、微软
 * <p>LeetCode 通过率：约 58.7%
 */
public class DecodeString {

    /**
     * 栈存 (重复次数, 当前串前缀)，遇 '[' 入栈，遇 ']' 弹出拼接。
     *
     * <p>核心解法：数字累积 num；字母追加 current；'[' push(num,current) 并 reset；']' pop 后 current=prev+num*current。
     *
     * <p>注意点：数字可能多位；nested 括号靠栈保存外层状态。
     *
     * <p>疑难点：']' 时 current 是括号内串，pop 得到外层 prev 和 repeat，current = prev + repeat*current。
     */
    public String decodeString(String s) {
        Deque<Integer> numStack = new ArrayDeque<>();
        Deque<StringBuilder> strStack = new ArrayDeque<>();
        StringBuilder current = new StringBuilder();
        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            } else if (c == '[') {
                numStack.push(num);
                strStack.push(current);
                current = new StringBuilder();
                num = 0;
            } else if (c == ']') {
                StringBuilder prev = strStack.pop();
                int repeat = numStack.pop();
                for (int j = 0; j < repeat; j++) {
                    prev.append(current);
                }
                current = prev;
            } else {
                current.append(c);
            }
        }
        return current.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== DecodeString ===");
        DecodeString s = new DecodeString();
        TestHelper.checkString("case1", "aaabcbc", s.decodeString("3[a]2[bc]"));
        TestHelper.checkString("case2", "accaccacc", s.decodeString("3[a2[c]]"));
        TestHelper.checkString("case3", "abcabccd", s.decodeString("2[abc]3[cd]ef"));
    }
}
