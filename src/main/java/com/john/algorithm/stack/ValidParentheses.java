package com.john.algorithm.stack;

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
