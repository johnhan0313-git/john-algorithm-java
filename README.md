# john-algorithm-java

Java 8 + Maven 算法题整理项目。按**解法类别 → 难度 → 题目**组织，每道题对应一个 Java 解法类，类头 Javadoc 含题目描述、面试考频、常见公司与 LeetCode 通过率，主方法含核心解法说明。

## 技术栈

- Java 8 + Maven 3.x（解法源码）
- FastAPI + PostgreSQL + Redis（Web 服务，可选 MinIO 扩展）
- Vite + React（学习面板，**强制邮箱验证码登录**）

## Web 服务（john-server 部署）

```bash
# 一次性：john-server 建库 + 测试库建表 + 同步 68 题
chmod +x scripts/*.sh run.sh
./scripts/bootstrap-test-env.sh

# 日常开发（连接 john-algorithm-test，需 Tailscale）
./run.sh start
./run.sh sync          # 新增/修改 Java 题后同步

# 访问
# 前端 http://localhost:3004  （未登录跳转 /login）
# API  http://localhost:8004/docs
```

- 本地开发 **默认使用** `john-server` 上的 **`john-algorithm-test`** 测试库（非 SQLite）
- 登录：邮箱 + 6 位验证码（无滑动拼图），JWT 有效期 **12 小时**
- Portainer 生产部署见 [docs/PORTAINER_DEPLOY.md](docs/PORTAINER_DEPLOY.md)
- 环境说明见 [docs/TEST_ENV.md](docs/TEST_ENV.md)

> 原静态面板 [ui/](ui/) 已迁移至 [frontend/](frontend/)，仍可用 `scripts/generate-ui-data.py` 生成本地 data.js。

## 项目结构

```
src/main/java/com/john/algorithm/   # Java 解法（真相源）
backend/                            # FastAPI API
frontend/                           # React 学习面板
scripts/sync-problems.py            # Javadoc → PostgreSQL
```

## 快速开始

```bash
# 编译
mvn compile

# 运行单题测试（类内 main 含 2~3 组用例）
java -cp target/classes com.john.algorithm.hashmap.easy.TwoSum
java -cp target/classes com.john.algorithm.twopointer.medium.ThreeSum
```

## IntelliJ IDEA 如何打开题目链接

IDEA 内置 Markdown **预览（Preview）** 常把 `src/...` 误当成网址（`http://src/...`），**无法可靠打开本地 Java 文件**。请用下面任一方式：

| 方式 | 操作 |
| --- | --- |
| **推荐：源码区点击** | 在左侧 **编辑区**（不要点 Preview 里的链接），**Ctrl + 单击**（Mac：**⌘ + 单击**）题目链接 |
| **按类名跳转** | **Ctrl + N**（Mac：**⌘ + O**），粘贴表格中的全限定类名，如 `com.john.algorithm.hashmap.easy.TwoSum` |
| **按路径打开** | **Ctrl + Shift + N**（Mac：**⌘ + Shift + O**），输入 `TwoSum.java` |

本 README 中链接使用 **以 `/` 开头的项目根路径**（如 `/src/main/java/.../TwoSum.java`），这是 IDEA 官方推荐的工程内路径写法。

## 题目一览（共 68 题）

> 在 IDEA **源码编辑区** Ctrl/⌘ + 单击题目名打开解法类；每题附全限定类名便于 Ctrl+N 跳转。大厂与通过率仅供参考。

| 类别 | Easy | Medium | Hard |
| --- | --- | --- | --- |
| **数组** | [88 合并两个有序数组](/src/main/java/com/john/algorithm/array/easy/MergeSortedArray.java)<br>类：`com.john.algorithm.array.easy.MergeSortedArray`<br>大厂：Google、微软、字节跳动、腾讯<br>通过率：55.4%<br><br>[283 移动零](/src/main/java/com/john/algorithm/array/easy/MoveZeroes.java)<br>类：`com.john.algorithm.array.easy.MoveZeroes`<br>大厂：字节跳动、美团、阿里巴巴、快手<br>通过率：63.8% | [53 最大子数组和](/src/main/java/com/john/algorithm/array/medium/MaximumSubarray.java)<br>类：`com.john.algorithm.array.medium.MaximumSubarray`<br>大厂：Google、Amazon、字节跳动、LinkedIn、微软<br>通过率：51.3%<br><br>[238 除自身以外数组的乘积](/src/main/java/com/john/algorithm/array/medium/ProductExceptSelf.java)<br>类：`com.john.algorithm.array.medium.ProductExceptSelf`<br>大厂：Meta、Google、Amazon、字节跳动、微软<br>通过率：77.0% | [41 缺失的第一个正数](/src/main/java/com/john/algorithm/array/hard/FirstMissingPositive.java)<br>类：`com.john.algorithm.array.hard.FirstMissingPositive`<br>大厂：Google、Amazon、字节跳动、华为<br>通过率：47.5% |
| **双指针** | [125 验证回文串](/src/main/java/com/john/algorithm/twopointer/easy/ValidPalindrome.java)<br>类：`com.john.algorithm.twopointer.easy.ValidPalindrome`<br>大厂：Meta、微软、Apple<br>通过率：48.5% | [11 盛最多水的容器](/src/main/java/com/john/algorithm/twopointer/medium/ContainerWithMostWater.java)<br>类：`com.john.algorithm.twopointer.medium.ContainerWithMostWater`<br>大厂：Google、Meta、Amazon、字节跳动、微软<br>通过率：57.6%<br><br>[15 三数之和](/src/main/java/com/john/algorithm/twopointer/medium/ThreeSum.java)<br>类：`com.john.algorithm.twopointer.medium.ThreeSum`<br>大厂：字节跳动、腾讯、阿里巴巴、Google、Amazon、Meta<br>通过率：37.8% | [42 接雨水](/src/main/java/com/john/algorithm/twopointer/hard/TrappingRainWater.java)<br>类：`com.john.algorithm.twopointer.hard.TrappingRainWater`<br>大厂：Google、Amazon、字节跳动、微软、Meta<br>通过率：58.4% |
| **滑动窗口** | [485 最大连续 1 的个数](/src/main/java/com/john/algorithm/slidingwindow/easy/MaxConsecutiveOnes.java)<br>类：`com.john.algorithm.slidingwindow.easy.MaxConsecutiveOnes`<br>大厂：Amazon、微软、LinkedIn<br>通过率：63.2% | [3 无重复字符的最长子串](/src/main/java/com/john/algorithm/slidingwindow/medium/LongestSubstringWithoutRepeating.java)<br>类：`com.john.algorithm.slidingwindow.medium.LongestSubstringWithoutRepeating`<br>大厂：Amazon、Google、Meta、字节跳动、微软<br>通过率：41.3%<br><br>[209 长度最小的子数组](/src/main/java/com/john/algorithm/slidingwindow/medium/MinimumSizeSubarraySum.java)<br>类：`com.john.algorithm.slidingwindow.medium.MinimumSizeSubarraySum`<br>大厂：字节跳动、美团、Google、Facebook<br>通过率：46.3% | [76 最小覆盖子串](/src/main/java/com/john/algorithm/slidingwindow/hard/MinimumWindowSubstring.java)<br>类：`com.john.algorithm.slidingwindow.hard.MinimumWindowSubstring`<br>大厂：Google、Meta、Amazon、字节跳动、微软<br>通过率：47.2% |
| **哈希表** | [1 两数之和](/src/main/java/com/john/algorithm/hashmap/easy/TwoSum.java)<br>类：`com.john.algorithm.hashmap.easy.TwoSum`<br>大厂：字节跳动、Google、Meta、Amazon、微软、腾讯、阿里巴巴<br>通过率：54.4% | [49 字母异位词分组](/src/main/java/com/john/algorithm/hashmap/medium/GroupAnagrams.java)<br>类：`com.john.algorithm.hashmap.medium.GroupAnagrams`<br>大厂：Meta、Amazon、Google、Uber、字节跳动<br>通过率：67.9%<br><br>[128 最长连续序列](/src/main/java/com/john/algorithm/hashmap/medium/LongestConsecutiveSequence.java)<br>类：`com.john.algorithm.hashmap.medium.LongestConsecutiveSequence`<br>大厂：Google、字节跳动、LinkedIn、Amazon<br>通过率：49.3%<br><br>[560 和为 K 的子数组](/src/main/java/com/john/algorithm/hashmap/medium/SubarraySumEqualsK.java)<br>类：`com.john.algorithm.hashmap.medium.SubarraySumEqualsK`<br>大厂：Meta、Google、字节跳动、拼多多<br>通过率：44.8% | [380 O(1) 时间插入、删除和获取随机元素](/src/main/java/com/john/algorithm/hashmap/hard/RandomizedSet.java)<br>类：`com.john.algorithm.hashmap.hard.RandomizedSet`<br>大厂：Google、Meta、LinkedIn、字节跳动<br>通过率：51.2% |
| **链表** | [141 环形链表](/src/main/java/com/john/algorithm/linkedlist/easy/LinkedListCycle.java)<br>类：`com.john.algorithm.linkedlist.easy.LinkedListCycle`<br>大厂：Amazon、Google、字节跳动、微软、Apple<br>通过率：54.5%<br><br>[21 合并两个有序链表](/src/main/java/com/john/algorithm/linkedlist/easy/MergeTwoSortedLists.java)<br>类：`com.john.algorithm.linkedlist.easy.MergeTwoSortedLists`<br>大厂：Amazon、Google、微软、字节跳动、腾讯<br>通过率：68.5%<br><br>[206 反转链表](/src/main/java/com/john/algorithm/linkedlist/easy/ReverseLinkedList.java)<br>类：`com.john.algorithm.linkedlist.easy.ReverseLinkedList`<br>大厂：Amazon、Google、Meta、微软、字节跳动<br>通过率：56.1% | [2 两数相加](/src/main/java/com/john/algorithm/linkedlist/medium/AddTwoNumbers.java)<br>类：`com.john.algorithm.linkedlist.medium.AddTwoNumbers`<br>大厂：Meta、Amazon、Google、阿里巴巴<br>通过率：46.2% | [25 K 个一组翻转链表](/src/main/java/com/john/algorithm/linkedlist/hard/ReverseNodesInKGroup.java)<br>类：`com.john.algorithm.linkedlist.hard.ReverseNodesInKGroup`<br>大厂：字节跳动、Google、Amazon、Facebook<br>通过率：52.8% |
| **栈** | [20 有效的括号](/src/main/java/com/john/algorithm/stack/easy/ValidParentheses.java)<br>类：`com.john.algorithm.stack.easy.ValidParentheses`<br>大厂：Amazon、Google、Meta、字节跳动、腾讯、微软<br>通过率：44.9% | [155 最小栈](/src/main/java/com/john/algorithm/stack/medium/MinStack.java)<br>类：`com.john.algorithm.stack.medium.MinStack`<br>大厂：Amazon、微软、Google、Bloomberg<br>通过率：55.6% | [84 柱状图中最大的矩形](/src/main/java/com/john/algorithm/stack/hard/LargestRectangleInHistogram.java)<br>类：`com.john.algorithm.stack.hard.LargestRectangleInHistogram`<br>大厂：Google、字节跳动、Amazon、微软<br>通过率：47.6% |
| **队列** | [232 用栈实现队列](/src/main/java/com/john/algorithm/queue/easy/MyQueue.java)<br>类：`com.john.algorithm.queue.easy.MyQueue`<br>大厂：Amazon、微软、Adobe<br>通过率：62.1% | [739 每日温度](/src/main/java/com/john/algorithm/queue/medium/DailyTemperatures.java)<br>类：`com.john.algorithm.queue.medium.DailyTemperatures`<br>大厂：Amazon、Google、字节跳动、微软<br>通过率：68.9% | [239 滑动窗口最大值](/src/main/java/com/john/algorithm/queue/hard/SlidingWindowMaximum.java)<br>类：`com.john.algorithm.queue.hard.SlidingWindowMaximum`<br>大厂：Google、Amazon、字节跳动、微软、Meta<br>通过率：50.3% |
| **二叉树** | [104 二叉树的最大深度](/src/main/java/com/john/algorithm/binarytree/easy/MaximumDepthOfBinaryTree.java)<br>类：`com.john.algorithm.binarytree.easy.MaximumDepthOfBinaryTree`<br>大厂：Amazon、Google、字节跳动、Meta、微软<br>通过率：77.5% | [102 二叉树的层序遍历](/src/main/java/com/john/algorithm/binarytree/medium/BinaryTreeLevelOrderTraversal.java)<br>类：`com.john.algorithm.binarytree.medium.BinaryTreeLevelOrderTraversal`<br>大厂：字节跳动、腾讯、Google、Amazon、Meta<br>通过率：68.2%<br><br>[105 从前序与中序遍历序列构造二叉树](/src/main/java/com/john/algorithm/binarytree/medium/ConstructBinaryTreeFromPreInorder.java)<br>类：`com.john.algorithm.binarytree.medium.ConstructBinaryTreeFromPreInorder`<br>大厂：Google、Meta、字节跳动、微软、Amazon<br>通过率：58.9% | [124 二叉树中的最大路径和](/src/main/java/com/john/algorithm/binarytree/hard/BinaryTreeMaximumPathSum.java)<br>类：`com.john.algorithm.binarytree.hard.BinaryTreeMaximumPathSum`<br>大厂：Google、Meta、字节跳动、微软<br>通过率：47.8% |
| **回溯** | [17 电话号码的字母组合](/src/main/java/com/john/algorithm/backtracking/easy/LetterCombinationsOfPhoneNumber.java)<br>类：`com.john.algorithm.backtracking.easy.LetterCombinationsOfPhoneNumber`<br>大厂：Google、Amazon、字节跳动、微软<br>通过率：58.6% | [46 全排列](/src/main/java/com/john/algorithm/backtracking/medium/Permutations.java)<br>类：`com.john.algorithm.backtracking.medium.Permutations`<br>大厂：Google、Meta、Amazon、字节跳动、微软<br>通过率：72.7%<br><br>[78 子集](/src/main/java/com/john/algorithm/backtracking/medium/Subsets.java)<br>类：`com.john.algorithm.backtracking.medium.Subsets`<br>大厂：Meta、字节跳动、Google、Amazon<br>通过率：82.3% | [51 N 皇后](/src/main/java/com/john/algorithm/backtracking/hard/NQueens.java)<br>类：`com.john.algorithm.backtracking.hard.NQueens`<br>大厂：Google、字节跳动、Amazon、华为<br>通过率：74.5% |
| **动态规划** | [70 爬楼梯](/src/main/java/com/john/algorithm/dynamicprogramming/easy/ClimbingStairs.java)<br>类：`com.john.algorithm.dynamicprogramming.easy.ClimbingStairs`<br>大厂：Amazon、Google、字节跳动、LinkedIn、微软<br>通过率：55.3% | [322 零钱兑换](/src/main/java/com/john/algorithm/dynamicprogramming/medium/CoinChange.java)<br>类：`com.john.algorithm.dynamicprogramming.medium.CoinChange`<br>大厂：Google、Amazon、字节跳动、腾讯、Airbnb<br>通过率：48.6%<br><br>[198 打家劫舍](/src/main/java/com/john/algorithm/dynamicprogramming/medium/HouseRobber.java)<br>类：`com.john.algorithm.dynamicprogramming.medium.HouseRobber`<br>大厂：Amazon、Google、LinkedIn、字节跳动<br>通过率：55.1% | [72 编辑距离](/src/main/java/com/john/algorithm/dynamicprogramming/hard/EditDistance.java)<br>类：`com.john.algorithm.dynamicprogramming.hard.EditDistance`<br>大厂：Google、Meta、Amazon、微软、字节跳动<br>通过率：62.4% |
| **贪心** | [121 买卖股票的最佳时机](/src/main/java/com/john/algorithm/greedy/easy/BestTimeToBuyAndSellStock.java)<br>类：`com.john.algorithm.greedy.easy.BestTimeToBuyAndSellStock`<br>大厂：Amazon、Meta、Google、字节跳动、微软<br>通过率：55.4% | [55 跳跃游戏](/src/main/java/com/john/algorithm/greedy/medium/JumpGame.java)<br>类：`com.john.algorithm.greedy.medium.JumpGame`<br>大厂：Google、Amazon、字节跳动、微软<br>通过率：43.5% | [45 跳跃游戏 II](/src/main/java/com/john/algorithm/greedy/hard/JumpGameII.java)<br>类：`com.john.algorithm.greedy.hard.JumpGameII`<br>大厂：Google、Amazon、字节跳动<br>通过率：44.2% |
| **二分查找** | [704 二分查找](/src/main/java/com/john/algorithm/binarysearch/easy/BinarySearch.java)<br>类：`com.john.algorithm.binarysearch.easy.BinarySearch`<br>大厂：Google、Amazon、字节跳动、Meta、微软、腾讯<br>通过率：55.2% | [33 搜索旋转排序数组](/src/main/java/com/john/algorithm/binarysearch/medium/SearchRotatedSortedArray.java)<br>类：`com.john.algorithm.binarysearch.medium.SearchRotatedSortedArray`<br>大厂：字节跳动、Google、Amazon、Meta、微软<br>通过率：44.1% | [4 寻找两个正序数组的中位数](/src/main/java/com/john/algorithm/binarysearch/hard/MedianOfTwoSortedArrays.java)<br>类：`com.john.algorithm.binarysearch.hard.MedianOfTwoSortedArrays`<br>大厂：Google、Meta、Amazon、微软、字节跳动<br>通过率：42.3% |
| **图** | [733 图像渲染](/src/main/java/com/john/algorithm/graph/easy/FloodFill.java)<br>类：`com.john.algorithm.graph.easy.FloodFill`<br>大厂：Google、Amazon、微软<br>通过率：58.3% | [207 课程表](/src/main/java/com/john/algorithm/graph/medium/CourseSchedule.java)<br>类：`com.john.algorithm.graph.medium.CourseSchedule`<br>大厂：Google、字节跳动、Amazon、Meta、微软<br>通过率：55.3%<br><br>[200 岛屿数量](/src/main/java/com/john/algorithm/graph/medium/NumberOfIslands.java)<br>类：`com.john.algorithm.graph.medium.NumberOfIslands`<br>大厂：Google、Amazon、微软、字节跳动、Meta<br>通过率：58.8% | [127 单词接龙](/src/main/java/com/john/algorithm/graph/hard/WordLadder.java)<br>类：`com.john.algorithm.graph.hard.WordLadder`<br>大厂：Google、Amazon、Meta、LinkedIn<br>通过率：48.7% |
| **堆** | [1046 最后一块石头的重量](/src/main/java/com/john/algorithm/heap/easy/LastStoneWeight.java)<br>类：`com.john.algorithm.heap.easy.LastStoneWeight`<br>大厂：Amazon、Google<br>通过率：65.4% | [215 数组中的第 K 个最大元素](/src/main/java/com/john/algorithm/heap/medium/KthLargestElement.java)<br>类：`com.john.algorithm.heap.medium.KthLargestElement`<br>大厂：Amazon、Google、字节跳动、Meta、LinkedIn<br>通过率：62.3%<br><br>[347 前 K 个高频元素](/src/main/java/com/john/algorithm/heap/medium/TopKFrequentElements.java)<br>类：`com.john.algorithm.heap.medium.TopKFrequentElements`<br>大厂：Amazon、字节跳动、Google、Meta、Uber<br>通过率：64.3% | [295 数据流的中位数](/src/main/java/com/john/algorithm/heap/hard/MedianFinder.java)<br>类：`com.john.algorithm.heap.hard.MedianFinder`<br>大厂：Google、Amazon、Meta、微软、字节跳动<br>通过率：53.1% |
| **并查集** | [684 冗余连接](/src/main/java/com/john/algorithm/unionfind/easy/RedundantConnection.java)<br>类：`com.john.algorithm.unionfind.easy.RedundantConnection`<br>大厂：Amazon、Google、字节跳动<br>通过率：66.7% | [547 省份数量](/src/main/java/com/john/algorithm/unionfind/medium/NumberOfProvinces.java)<br>类：`com.john.algorithm.unionfind.medium.NumberOfProvinces`<br>大厂：Amazon、Google、字节跳动、Meta、LinkedIn<br>通过率：65.4% | [685 冗余连接 II](/src/main/java/com/john/algorithm/unionfind/hard/RedundantConnectionII.java)<br>类：`com.john.algorithm.unionfind.hard.RedundantConnectionII`<br>大厂：Google、Meta、华为<br>通过率：35.2% |
| **字符串** | [242 有效的字母异位词](/src/main/java/com/john/algorithm/string/easy/ValidAnagram.java)<br>类：`com.john.algorithm.string.easy.ValidAnagram`<br>大厂：Amazon、Google、微软、字节跳动<br>通过率：66.8% | [394 字符串解码](/src/main/java/com/john/algorithm/string/medium/DecodeString.java)<br>类：`com.john.algorithm.string.medium.DecodeString`<br>大厂：Google、字节跳动、Amazon、微软<br>通过率：58.7%<br><br>[5 最长回文子串](/src/main/java/com/john/algorithm/string/medium/LongestPalindromicSubstring.java)<br>类：`com.john.algorithm.string.medium.LongestPalindromicSubstring`<br>大厂：Amazon、Google、Meta、微软、字节跳动<br>通过率：35.8% | [10 正则表达式匹配](/src/main/java/com/john/algorithm/string/hard/RegularExpressionMatching.java)<br>类：`com.john.algorithm.string.hard.RegularExpressionMatching`<br>大厂：Google、Meta、Amazon、微软<br>通过率：29.8% |
| **数学** | [9 回文数](/src/main/java/com/john/algorithm/math/easy/PalindromeNumber.java)<br>类：`com.john.algorithm.math.easy.PalindromeNumber`<br>大厂：Amazon、Apple、微软、Google<br>通过率：55.7% | [7 整数反转](/src/main/java/com/john/algorithm/math/medium/ReverseInteger.java)<br>类：`com.john.algorithm.math.medium.ReverseInteger`<br>大厂：Apple、Amazon、Google、微软<br>通过率：35.6% | — |
| **排序** | [977 有序数组的平方](/src/main/java/com/john/algorithm/sorting/easy/SquaresOfSortedArray.java)<br>类：`com.john.algorithm.sorting.easy.SquaresOfSortedArray`<br>大厂：Facebook、Amazon、微软<br>通过率：72.5% | [56 合并区间](/src/main/java/com/john/algorithm/sorting/medium/MergeIntervals.java)<br>类：`com.john.algorithm.sorting.medium.MergeIntervals`<br>大厂：Google、Meta、字节跳动、Amazon、微软<br>通过率：49.8%<br><br>[75 颜色分类](/src/main/java/com/john/algorithm/sorting/medium/SortColors.java)<br>类：`com.john.algorithm.sorting.medium.SortColors`<br>大厂：Google、字节跳动、Amazon、Meta、微软<br>通过率：59.8% | [179 最大数](/src/main/java/com/john/algorithm/sorting/hard/LargestNumber.java)<br>类：`com.john.algorithm.sorting.hard.LargestNumber`<br>大厂：Google、字节跳动、Amazon<br>通过率：38.9% |

## 约定说明

| 项目 | 说明 |
| --- | --- |
| 目录层级 | `类别 / easy|medium|hard / 解法类` |
| 类头注释 | LeetCode 题号、题目描述、示例、面试考频、常见公司、通过率 |
| 方法注释 | 核心解法、注意点、疑难点 |
| 本地测试 | 每个解法类提供 `main` 方法，调用 `TestHelper` 断言 |

## 新增题目

1. 在对应类别与难度包下新建 Java 类
2. 类头 Javadoc 补全题目描述与面试信息
3. 主方法 Javadoc 补全解法说明
4. 添加 `main` 方法编写测试用例
5. 运行 `python3 scripts/generate-readme.py` 刷新本表格
6. 运行 `python3 scripts/generate-ui-data.py` 刷新可视化面板数据

## 可视化学习面板

项目提供本地 Web 界面，支持按类别/难度/考频筛选、查看解法思路、复制类名与运行命令、标记学习进度。

```bash
# 生成数据并启动（浏览器打开 http://localhost:8765）
./scripts/serve-ui.sh

# 或分步执行
python3 scripts/generate-ui-data.py
cd ui && python3 -m http.server 8765
```

IDEA 中也可直接右键 `ui/index.html` → Open in Browser（需先执行 `generate-ui-data.py`）。
