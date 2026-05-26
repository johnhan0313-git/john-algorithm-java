package com.john.algorithm.graph.hard;

import com.john.algorithm.common.TestHelper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * LeetCode 127. 单词接龙
 *
 * <p>字典 wordList 中有一个词 beginWord 和 endWord，
 * 找到从 beginWord 到 endWord 的最短转换序列中的单词数目。
 * 每次只能改变一个字母，且中间词必须在 wordList 中。
 *
 * <p>示例：beginWord = "hit", endWord = "cog",
 * wordList = ["hot","dot","dog","lot","log","cog"]，输出 5。
 *
 * <p>面试考频：高（BFS  shortest path，Google/Amazon Hard 经典）
 * <p>常见公司：Google、Amazon、Meta、LinkedIn
 * <p>LeetCode 通过率：约 48.7%
 */
public class WordLadder {

    /**
     * BFS 层序遍历，每层 wordLength+1，首次到达 endWord 即最短。
     *
     * <p>核心解法：队列存当前词，Set 存 wordList 便于 O(1) 查找和删除已访问词。
     *
     * <p>注意点：endWord 不在 wordList 则返回 0；每替换一位字母 26 种尝试。
     *
     * <p>疑难点：必须用 BFS 而非 DFS 求最短；visited 可从 wordSet remove 避免重复入队。
     */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if (!wordSet.contains(endWord)) {
            return 0;
        }
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        int level = 1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                char[] chars = queue.poll().toCharArray();
                for (int j = 0; j < chars.length; j++) {
                    char original = chars[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        chars[j] = c;
                        String next = new String(chars);
                        if (next.equals(endWord)) {
                            return level + 1;
                        }
                        if (wordSet.contains(next)) {
                            queue.offer(next);
                            wordSet.remove(next);
                        }
                    }
                    chars[j] = original;
                }
            }
            level++;
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println("=== WordLadder ===");
        WordLadder s = new WordLadder();
        TestHelper.checkInt("case1", 5, s.ladderLength("hit", "cog", java.util.Arrays.asList("hot","dot","dog","lot","log","cog")));
        TestHelper.checkInt("case2", 0, s.ladderLength("hit", "cog", java.util.Arrays.asList("hot","dot","dog","lot","log")));
        TestHelper.checkInt("case3", 2, s.ladderLength("a", "c", java.util.Arrays.asList("a","b","c")));
    }
}
