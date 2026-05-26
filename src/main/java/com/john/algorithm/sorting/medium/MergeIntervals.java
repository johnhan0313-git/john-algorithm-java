package com.john.algorithm.sorting.medium;

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

    /**
     * 按起点排序后线性扫描合并。
     *
     * <p>核心解法：排序后遍历，若当前区间与 merged 最后一个不重叠则直接加入；
     * 否则扩展最后一个区间的 end = max(end, current.end)。
     *
     * <p>注意点：重叠判定用 last.end &gt;= current.start（排序后只需比 end）；
     * 本实现用 last.end &lt; current.start 判断不重叠，等价；合并时 end 取 max 防止 [1,4] 与 [2,3] 情况。
     *
     * <p>疑难点：为何要排序？无序时间无法 O(n) 判断相邻区间关系；
     * 按 start 排序后，重叠区间在序列中必然相邻或可被一次扫描合并。
     */
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
