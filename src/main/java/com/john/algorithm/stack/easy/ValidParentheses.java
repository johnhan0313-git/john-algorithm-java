package com.john.algorithm.stack.easy;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * LeetCode 20. 有效的括号
 *
 * <p>给定一个只包括 '('、')'、'{'、'}'、'['、']' 的字符串 s，判断字符串是否有效。
 * 有效字符串需满足：左括号必须用相同类型的右括号闭合，且必须以正确的顺序闭合。
 *
 * <p>示例：s = "()[]{}"，输出 true。
 */
public class ValidParentheses {

    /**
     * 栈匹配：遇左括号入栈，遇右括号与栈顶配对。
     *
     * <p>核心解法：左括号 push；右括号 pop 栈顶，检查类型是否匹配。
     * 最终栈空则有效。
     *
     * <p>注意点：右括号出现时栈空直接 false；遍历结束后必须栈空（处理多余左括号）；
     * 可用 Map 存配对关系简化 if-else。
     *
     * <p>疑难点：为何栈能处理嵌套？后入栈的左括号必须先闭合，符合 LIFO 特性，
     * 天然保证「正确顺序闭合」。
     */
    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
                continue;
            }
            if (stack.isEmpty()) {
                return false;
            }
            char top = stack.pop();
            if (c == ')' && top != '(') {
                return false;
            }
            if (c == '}' && top != '{') {
                return false;
            }
            if (c == ']' && top != '[') {
                return false;
            }
        }
        return stack.isEmpty();
    }
}
