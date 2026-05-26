package com.john.algorithm.hashmap.hard;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * LeetCode 380. O(1) 时间插入、删除和获取随机元素
 *
 * <p>实现 RandomizedSet 类：insert、remove、getRandom 均在平均 O(1) 时间内完成。
 *
 * <p>示例：insert(1) true, remove(2) false, insert(2) true, getRandom() 1或2, remove(1) true, insert(2) false。
 *
 * <p>面试考频：高（Google 设计类常考，考察哈希表 + 动态数组）
 * <p>常见公司：Google、Meta、LinkedIn、字节跳动
 * <p>LeetCode 通过率：约 51.2%
 */
public class RandomizedSet {

    private final List<Integer> values;
    private final Map<Integer, Integer> indexMap;
    private final Random random;

    public RandomizedSet() {
        values = new ArrayList<>();
        indexMap = new HashMap<>();
        random = new Random();
    }

    /**
     * 哈希表存值→下标，数组存值；删除时用末尾元素填补被删位置。
     *
     * <p>核心解法：remove 时 swap 被删元素与末尾，pop 末尾，更新 indexMap。
     *
     * <p>注意点：删除不存在的值返回 false；getRandom 用 random.nextInt(values.size())。
     *
     * <p>疑难点：swap-with-last 保证数组连续 O(1) 删除；indexMap 需同步更新被交换元素的下标。
     */
    public boolean insert(int val) {
        if (indexMap.containsKey(val)) {
            return false;
        }
        indexMap.put(val, values.size());
        values.add(val);
        return true;
    }

    public boolean remove(int val) {
        if (!indexMap.containsKey(val)) {
            return false;
        }
        int index = indexMap.get(val);
        int lastVal = values.get(values.size() - 1);
        values.set(index, lastVal);
        indexMap.put(lastVal, index);
        values.remove(values.size() - 1);
        indexMap.remove(val);
        return true;
    }

    public int getRandom() {
        return values.get(random.nextInt(values.size()));
    }

    public static void main(String[] args) {
        System.out.println("=== RandomizedSet ===");
        RandomizedSet set = new RandomizedSet();
        TestHelper.checkBool("insert(1)", true, set.insert(1));
        TestHelper.checkBool("insert(1) dup", false, set.insert(1));
        TestHelper.checkBool("insert(2)", true, set.insert(2));
        System.out.println("getRandom in {1,2}: " + set.getRandom());
        TestHelper.checkBool("remove(1)", true, set.remove(1));
        TestHelper.checkInt("getRandom", 2, set.getRandom());
    }
}
