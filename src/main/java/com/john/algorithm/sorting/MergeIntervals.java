package com.john.algorithm.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * LeetCode 56. 合并区间
 *
 * <p>以数组 intervals 表示若干个区间的集合，其中单个区间为 intervals[i] = [start_i, end_i]。
 * 请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。
 *
 * <p>示例：intervals = [[1,3],[2,6],[8,10],[15,18]]，输出 [[1,6],[8,10],[15,18]]。
 */
public class MergeIntervals {

    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> merged = new ArrayList<>();
        for (int[] interval : intervals) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(interval);
            } else {
                int[] last = merged.get(merged.size() - 1);
                last[1] = Math.max(last[1], interval[1]);
            }
        }
        return merged.toArray(new int[merged.size()][]);
    }
}
