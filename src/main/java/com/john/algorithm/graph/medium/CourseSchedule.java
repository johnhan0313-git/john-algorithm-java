package com.john.algorithm.graph.medium;

import com.john.algorithm.common.TestHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * LeetCode 207. 课程表
 *
 * <p>你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1。
 * 先修课程按 prerequisites[i] = [a, b] 表示要选修 a 必须先修 b。
 * 判断是否可能完成所有课程（即是否为有向无环图）。
 *
 * <p>示例：numCourses = 2, prerequisites = [[1,0]]，输出 true。
 *
 * <p>面试考频：极高（拓扑排序模板，Google/字节必考）
 * <p>常见公司：Google、字节跳动、Amazon、Meta、微软
 * <p>LeetCode 通过率：约 55.3%
 */
public class CourseSchedule {

    /**
     * Kahn 拓扑排序：BFS 入度为 0 的节点，统计已修课程数。
     *
     * <p>核心解法：建图和 indegree，队列入度 0 节点，弹出时邻居 indegree--，入度变 0 再入队。
     *
     * <p>注意点：processed == numCourses 则无环；也可用 DFS 三色标记判环。
     *
     * <p>疑难点：有环则 eventually 队列无法处理全部节点，processed &lt; numCourses。
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        int[] indegree = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : prerequisites) {
            graph.get(edge[1]).add(edge[0]);
            indegree[edge[0]]++;
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        int processed = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            processed++;
            for (int next : graph.get(course)) {
                indegree[next]--;
                if (indegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        return processed == numCourses;
    }

    public static void main(String[] args) {
        System.out.println("=== CourseSchedule ===");
        CourseSchedule s = new CourseSchedule();
        TestHelper.checkBool("case1", true, s.canFinish(2, new int[][]{{1, 0}}));
        TestHelper.checkBool("case2", false, s.canFinish(2, new int[][]{{1, 0}, {0, 1}}));
        TestHelper.checkBool("case3", true, s.canFinish(1, new int[][]{}));
    }
}
