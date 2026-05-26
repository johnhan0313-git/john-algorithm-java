#!/usr/bin/env python3
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent / "src/main/java/com/john/algorithm"
IMPORT = "import com.john.algorithm.common.TestHelper;\n"

MAINS = {
"MoveZeroes": '''    public static void main(String[] args) {
        System.out.println("=== MoveZeroes ===");
        MoveZeroes s = new MoveZeroes();
        int[] c1 = {0, 1, 0, 3, 12}; s.moveZeroes(c1);
        TestHelper.checkArray("case1", new int[]{1, 3, 12, 0, 0}, c1);
        int[] c2 = {0, 0, 1}; s.moveZeroes(c2);
        TestHelper.checkArray("case2", new int[]{1, 0, 0}, c2);
        int[] c3 = {1, 2, 3}; s.moveZeroes(c3);
        TestHelper.checkArray("case3", new int[]{1, 2, 3}, c3);
    }''',
"MergeSortedArray": '''    public static void main(String[] args) {
        System.out.println("=== MergeSortedArray ===");
        MergeSortedArray s = new MergeSortedArray();
        int[] c1 = {1, 2, 3, 0, 0, 0}; s.merge(c1, 3, new int[]{2, 5, 6}, 3);
        TestHelper.checkArray("case1", new int[]{1, 2, 2, 3, 5, 6}, c1);
        int[] c2 = {1, 0, 0}; s.merge(c2, 1, new int[]{2}, 1);
        TestHelper.checkArray("case2", new int[]{1, 2}, c2);
        int[] c3 = {0}; s.merge(c3, 0, new int[]{1}, 1);
        TestHelper.checkArray("case3", new int[]{1}, c3);
    }''',
"MaximumSubarray": '''    public static void main(String[] args) {
        System.out.println("=== MaximumSubarray ===");
        MaximumSubarray s = new MaximumSubarray();
        TestHelper.checkInt("case1", 6, s.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        TestHelper.checkInt("case2", 1, s.maxSubArray(new int[]{1}));
        TestHelper.checkInt("case3", 23, s.maxSubArray(new int[]{5, 4, -1, 7, 8}));
    }''',
"ProductExceptSelf": '''    public static void main(String[] args) {
        System.out.println("=== ProductExceptSelf ===");
        ProductExceptSelf s = new ProductExceptSelf();
        TestHelper.checkArray("case1", new int[]{24, 12, 8, 6}, s.productExceptSelf(new int[]{1, 2, 3, 4}));
        TestHelper.checkArray("case2", new int[]{0, 0}, s.productExceptSelf(new int[]{0, 0}));
        TestHelper.checkArray("case3", new int[]{0, 0, 9, 0}, s.productExceptSelf(new int[]{1, 2, 3, 4, 0}));
    }''',
"FirstMissingPositive": '''    public static void main(String[] args) {
        System.out.println("=== FirstMissingPositive ===");
        FirstMissingPositive s = new FirstMissingPositive();
        TestHelper.checkInt("case1", 3, s.firstMissingPositive(new int[]{1, 2, 0}));
        TestHelper.checkInt("case2", 2, s.firstMissingPositive(new int[]{3, 4, -1, 1}));
        TestHelper.checkInt("case3", 1, s.firstMissingPositive(new int[]{7, 8, 9, 11, 12}));
    }''',
"ValidPalindrome": '''    public static void main(String[] args) {
        System.out.println("=== ValidPalindrome ===");
        ValidPalindrome s = new ValidPalindrome();
        TestHelper.checkBool("case1", true, s.isPalindrome("A man, a plan, a canal: Panama"));
        TestHelper.checkBool("case2", false, s.isPalindrome("race a car"));
        TestHelper.checkBool("case3", true, s.isPalindrome(" "));
    }''',
"ContainerWithMostWater": '''    public static void main(String[] args) {
        System.out.println("=== ContainerWithMostWater ===");
        ContainerWithMostWater s = new ContainerWithMostWater();
        TestHelper.checkInt("case1", 49, s.maxArea(new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7}));
        TestHelper.checkInt("case2", 1, s.maxArea(new int[]{1, 1}));
        TestHelper.checkInt("case3", 16, s.maxArea(new int[]{4, 3, 2, 1, 4}));
    }''',
"ThreeSum": '''    public static void main(String[] args) {
        System.out.println("=== ThreeSum ===");
        ThreeSum s = new ThreeSum();
        System.out.println("case1: " + s.threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
        System.out.println("case2: " + s.threeSum(new int[]{0, 0, 0}));
        System.out.println("case3: " + s.threeSum(new int[]{1, 2, -2, -1}));
    }''',
"TrappingRainWater": '''    public static void main(String[] args) {
        System.out.println("=== TrappingRainWater ===");
        TrappingRainWater s = new TrappingRainWater();
        TestHelper.checkInt("case1", 6, s.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
        TestHelper.checkInt("case2", 0, s.trap(new int[]{0, 0, 0}));
        TestHelper.checkInt("case3", 9, s.trap(new int[]{4, 2, 0, 3, 2, 5}));
    }''',
"MaxConsecutiveOnes": '''    public static void main(String[] args) {
        System.out.println("=== MaxConsecutiveOnes ===");
        MaxConsecutiveOnes s = new MaxConsecutiveOnes();
        TestHelper.checkInt("case1", 3, s.findMaxConsecutiveOnes(new int[]{1, 1, 0, 1, 1, 1}));
        TestHelper.checkInt("case2", 2, s.findMaxConsecutiveOnes(new int[]{1, 0, 1, 1, 0, 1}));
        TestHelper.checkInt("case3", 0, s.findMaxConsecutiveOnes(new int[]{0, 0, 0}));
    }''',
"LongestSubstringWithoutRepeating": '''    public static void main(String[] args) {
        System.out.println("=== LongestSubstringWithoutRepeating ===");
        LongestSubstringWithoutRepeating s = new LongestSubstringWithoutRepeating();
        TestHelper.checkInt("case1", 3, s.lengthOfLongestSubstring("abcabcbb"));
        TestHelper.checkInt("case2", 1, s.lengthOfLongestSubstring("bbbbb"));
        TestHelper.checkInt("case3", 3, s.lengthOfLongestSubstring("pwwkew"));
    }''',
"MinimumSizeSubarraySum": '''    public static void main(String[] args) {
        System.out.println("=== MinimumSizeSubarraySum ===");
        MinimumSizeSubarraySum s = new MinimumSizeSubarraySum();
        TestHelper.checkInt("case1", 2, s.minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}));
        TestHelper.checkInt("case2", 1, s.minSubArrayLen(4, new int[]{1, 4, 4}));
        TestHelper.checkInt("case3", 0, s.minSubArrayLen(11, new int[]{1, 1, 1, 1, 1, 1, 1, 1}));
    }''',
"MinimumWindowSubstring": '''    public static void main(String[] args) {
        System.out.println("=== MinimumWindowSubstring ===");
        MinimumWindowSubstring s = new MinimumWindowSubstring();
        TestHelper.checkString("case1", "BANC", s.minWindow("ADOBECODEBANC", "ABC"));
        TestHelper.checkString("case2", "a", s.minWindow("a", "a"));
        TestHelper.checkString("case3", "", s.minWindow("a", "aa"));
    }''',
"TwoSum": '''    public static void main(String[] args) {
        System.out.println("=== TwoSum ===");
        TwoSum s = new TwoSum();
        TestHelper.checkArray("case1", new int[]{0, 1}, s.twoSum(new int[]{2, 7, 11, 15}, 9));
        TestHelper.checkArray("case2", new int[]{1, 2}, s.twoSum(new int[]{3, 2, 4}, 6));
        TestHelper.checkArray("case3", new int[]{0, 1}, s.twoSum(new int[]{3, 3}, 6));
    }''',
"GroupAnagrams": '''    public static void main(String[] args) {
        System.out.println("=== GroupAnagrams ===");
        GroupAnagrams s = new GroupAnagrams();
        System.out.println("case1: " + s.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
        System.out.println("case2: " + s.groupAnagrams(new String[]{""}));
        System.out.println("case3: " + s.groupAnagrams(new String[]{"a"}));
    }''',
"LongestConsecutiveSequence": '''    public static void main(String[] args) {
        System.out.println("=== LongestConsecutiveSequence ===");
        LongestConsecutiveSequence s = new LongestConsecutiveSequence();
        TestHelper.checkInt("case1", 4, s.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
        TestHelper.checkInt("case2", 1, s.longestConsecutive(new int[]{0, -1}));
        TestHelper.checkInt("case3", 0, s.longestConsecutive(new int[]{}));
    }''',
"SubarraySumEqualsK": '''    public static void main(String[] args) {
        System.out.println("=== SubarraySumEqualsK ===");
        SubarraySumEqualsK s = new SubarraySumEqualsK();
        TestHelper.checkInt("case1", 2, s.subarraySum(new int[]{1, 1, 1}, 2));
        TestHelper.checkInt("case2", 1, s.subarraySum(new int[]{1, 2, 3}, 3));
        TestHelper.checkInt("case3", 0, s.subarraySum(new int[]{1}, 0));
    }''',
"RandomizedSet": '''    public static void main(String[] args) {
        System.out.println("=== RandomizedSet ===");
        RandomizedSet set = new RandomizedSet();
        TestHelper.checkBool("insert(1)", true, set.insert(1));
        TestHelper.checkBool("insert(1) dup", false, set.insert(1));
        TestHelper.checkBool("insert(2)", true, set.insert(2));
        System.out.println("getRandom in {1,2}: " + set.getRandom());
        TestHelper.checkBool("remove(1)", true, set.remove(1));
        TestHelper.checkInt("getRandom", 2, set.getRandom());
    }''',
"ReverseLinkedList": '''    public static void main(String[] args) {
        System.out.println("=== ReverseLinkedList ===");
        ReverseLinkedList s = new ReverseLinkedList();
        TestHelper.checkArray("case1", new int[]{5, 4, 3, 2, 1}, TestHelper.listToArray(s.reverseList(TestHelper.list(1, 2, 3, 4, 5))));
        TestHelper.checkArray("case2", new int[]{1}, TestHelper.listToArray(s.reverseList(TestHelper.list(1))));
        TestHelper.checkArray("case3", new int[]{}, TestHelper.listToArray(s.reverseList(null)));
    }''',
"MergeTwoSortedLists": '''    public static void main(String[] args) {
        System.out.println("=== MergeTwoSortedLists ===");
        MergeTwoSortedLists s = new MergeTwoSortedLists();
        TestHelper.checkArray("case1", new int[]{1, 1, 2, 3, 4, 4}, TestHelper.listToArray(s.mergeTwoLists(TestHelper.list(1, 2, 4), TestHelper.list(1, 3, 4))));
        TestHelper.checkArray("case2", new int[]{1}, TestHelper.listToArray(s.mergeTwoLists(null, TestHelper.list(1))));
        TestHelper.checkArray("case3", new int[]{}, TestHelper.listToArray(s.mergeTwoLists(null, null)));
    }''',
"LinkedListCycle": '''    public static void main(String[] args) {
        System.out.println("=== LinkedListCycle ===");
        LinkedListCycle s = new LinkedListCycle();
        TestHelper.checkBool("case1 no cycle", false, s.hasCycle(TestHelper.list(1, 2, 3)));
        com.john.algorithm.common.ListNode cycle = TestHelper.list(3, 2, 0, -4);
        cycle.next.next.next.next = cycle.next;
        TestHelper.checkBool("case2 has cycle", true, s.hasCycle(cycle));
        TestHelper.checkBool("case3 single", false, s.hasCycle(TestHelper.list(1)));
    }''',
"AddTwoNumbers": '''    public static void main(String[] args) {
        System.out.println("=== AddTwoNumbers ===");
        AddTwoNumbers s = new AddTwoNumbers();
        TestHelper.checkArray("case1", new int[]{7, 0, 8}, TestHelper.listToArray(s.addTwoNumbers(TestHelper.list(2, 4, 3), TestHelper.list(5, 6, 4))));
        TestHelper.checkArray("case2", new int[]{0}, TestHelper.listToArray(s.addTwoNumbers(TestHelper.list(0), TestHelper.list(0))));
        TestHelper.checkArray("case3", new int[]{8, 1}, TestHelper.listToArray(s.addTwoNumbers(TestHelper.list(9, 9), TestHelper.list(1))));
    }''',
"ReverseNodesInKGroup": '''    public static void main(String[] args) {
        System.out.println("=== ReverseNodesInKGroup ===");
        ReverseNodesInKGroup s = new ReverseNodesInKGroup();
        TestHelper.checkArray("case1", new int[]{2, 1, 4, 3, 5}, TestHelper.listToArray(s.reverseKGroup(TestHelper.list(1, 2, 3, 4, 5), 2)));
        TestHelper.checkArray("case2", new int[]{1, 2, 3}, TestHelper.listToArray(s.reverseKGroup(TestHelper.list(1, 2, 3), 1)));
        TestHelper.checkArray("case3", new int[]{3, 2, 1}, TestHelper.listToArray(s.reverseKGroup(TestHelper.list(1, 2, 3), 3)));
    }''',
"ValidParentheses": '''    public static void main(String[] args) {
        System.out.println("=== ValidParentheses ===");
        ValidParentheses s = new ValidParentheses();
        TestHelper.checkBool("case1", true, s.isValid("()[]{}"));
        TestHelper.checkBool("case2", false, s.isValid("(]"));
        TestHelper.checkBool("case3", false, s.isValid("([)]"));
    }''',
"MinStack": '''    public static void main(String[] args) {
        System.out.println("=== MinStack ===");
        MinStack st = new MinStack();
        st.push(-2); st.push(0); st.push(-3);
        TestHelper.checkInt("getMin", -3, st.getMin());
        st.pop();
        TestHelper.checkInt("top", 0, st.top());
        TestHelper.checkInt("getMin after pop", -2, st.getMin());
    }''',
"MyQueue": '''    public static void main(String[] args) {
        System.out.println("=== MyQueue ===");
        MyQueue q = new MyQueue();
        q.push(1); q.push(2);
        TestHelper.checkInt("peek", 1, q.peek());
        TestHelper.checkInt("pop", 1, q.pop());
        TestHelper.checkBool("empty false", false, q.empty());
        TestHelper.checkInt("pop", 2, q.pop());
        TestHelper.checkBool("empty true", true, q.empty());
    }''',
"DailyTemperatures": '''    public static void main(String[] args) {
        System.out.println("=== DailyTemperatures ===");
        DailyTemperatures s = new DailyTemperatures();
        TestHelper.checkArray("case1", new int[]{1, 1, 4, 2, 1, 1, 0, 0}, s.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
        TestHelper.checkArray("case2", new int[]{0, 0, 0}, s.dailyTemperatures(new int[]{30, 30, 30}));
        TestHelper.checkArray("case3", new int[]{1}, s.dailyTemperatures(new int[]{55, 56}));
    }''',
"SlidingWindowMaximum": '''    public static void main(String[] args) {
        System.out.println("=== SlidingWindowMaximum ===");
        SlidingWindowMaximum s = new SlidingWindowMaximum();
        TestHelper.checkArray("case1", new int[]{3, 3, 5, 5, 6, 7}, s.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
        TestHelper.checkArray("case2", new int[]{1}, s.maxSlidingWindow(new int[]{1}, 1));
        TestHelper.checkArray("case3", new int[]{3, 3}, s.maxSlidingWindow(new int[]{1, 3, 1, 2}, 2));
    }''',
"MaximumDepthOfBinaryTree": '''    public static void main(String[] args) {
        System.out.println("=== MaximumDepthOfBinaryTree ===");
        MaximumDepthOfBinaryTree s = new MaximumDepthOfBinaryTree();
        TestHelper.checkInt("case1", 3, s.maxDepth(TestHelper.tree(3, 9, 20, null, null, 15, 7)));
        TestHelper.checkInt("case2", 0, s.maxDepth(null));
        TestHelper.checkInt("case3", 2, s.maxDepth(TestHelper.tree(1, null, 2)));
    }''',
"BinaryTreeLevelOrderTraversal": '''    public static void main(String[] args) {
        System.out.println("=== BinaryTreeLevelOrderTraversal ===");
        BinaryTreeLevelOrderTraversal s = new BinaryTreeLevelOrderTraversal();
        System.out.println("case1: " + s.levelOrder(TestHelper.tree(3, 9, 20, null, null, 15, 7)));
        System.out.println("case2: " + s.levelOrder(TestHelper.tree(1)));
        System.out.println("case3: " + s.levelOrder(null));
    }''',
"ConstructBinaryTreeFromPreInorder": '''    public static void main(String[] args) {
        System.out.println("=== ConstructBinaryTreeFromPreInorder ===");
        ConstructBinaryTreeFromPreInorder s = new ConstructBinaryTreeFromPreInorder();
        com.john.algorithm.common.TreeNode r1 = s.buildTree(new int[]{3, 9, 20, 15, 7}, new int[]{9, 3, 15, 20, 7});
        TestHelper.checkInt("case1 root", 3, r1.val);
        TestHelper.checkInt("case1 left", 9, r1.left.val);
        com.john.algorithm.common.TreeNode r2 = s.buildTree(new int[]{1}, new int[]{1});
        TestHelper.checkInt("case2 root", 1, r2.val);
        TestHelper.checkBool("case3 null", true, s.buildTree(new int[]{}, new int[]{}) == null);
    }''',
"BinaryTreeMaximumPathSum": '''    public static void main(String[] args) {
        System.out.println("=== BinaryTreeMaximumPathSum ===");
        BinaryTreeMaximumPathSum s = new BinaryTreeMaximumPathSum();
        TestHelper.checkInt("case1", 42, s.maxPathSum(TestHelper.tree(-10, 9, 20, null, null, 15, 7)));
        TestHelper.checkInt("case2", 2, s.maxPathSum(TestHelper.tree(2, -1)));
        TestHelper.checkInt("case3", 3, s.maxPathSum(TestHelper.tree(1, 2, 3)));
    }''',
}

# part 2 appended below in same file - fix typo Largest noInHistogram
MAINS["LargestRectangleInHistogram"] = '''    public static void main(String[] args) {
        System.out.println("=== LargestRectangleInHistogram ===");
        LargestRectangleInHistogram s = new LargestRectangleInHistogram();
        TestHelper.checkInt("case1", 10, s.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
        TestHelper.checkInt("case2", 4, s.largestRectangleArea(new int[]{2, 4}));
        TestHelper.checkInt("case3", 1, s.largestRectangleArea(new int[]{1, 1, 1}));
    }'''

MAINS.update({
"LetterCombinationsOfPhoneNumber": '''    public static void main(String[] args) {
        System.out.println("=== LetterCombinationsOfPhoneNumber ===");
        LetterCombinationsOfPhoneNumber s = new LetterCombinationsOfPhoneNumber();
        System.out.println("case1: " + s.letterCombinations("23"));
        System.out.println("case2: " + s.letterCombinations(""));
        System.out.println("case3: " + s.letterCombinations("2"));
    }''',
"Permutations": '''    public static void main(String[] args) {
        System.out.println("=== Permutations ===");
        Permutations s = new Permutations();
        System.out.println("case1 size: " + s.permute(new int[]{1, 2, 3}).size());
        System.out.println("case2: " + s.permute(new int[]{0, 1}));
        System.out.println("case3: " + s.permute(new int[]{1}));
    }''',
"Subsets": '''    public static void main(String[] args) {
        System.out.println("=== Subsets ===");
        Subsets s = new Subsets();
        System.out.println("case1: " + s.subsets(new int[]{1, 2, 3}));
        System.out.println("case2: " + s.subsets(new int[]{0}));
        System.out.println("case3: " + s.subsets(new int[]{}));
    }''',
"NQueens": '''    public static void main(String[] args) {
        System.out.println("=== NQueens ===");
        NQueens s = new NQueens();
        System.out.println("case1 n=4 count: " + s.solveNQueens(4).size());
        System.out.println("case2 n=1: " + s.solveNQueens(1));
        System.out.println("case3 n=2 count: " + s.solveNQueens(2).size());
    }''',
"ClimbingStairs": '''    public static void main(String[] args) {
        System.out.println("=== ClimbingStairs ===");
        ClimbingStairs s = new ClimbingStairs();
        TestHelper.checkInt("case1", 3, s.climbStairs(3));
        TestHelper.checkInt("case2", 2, s.climbStairs(2));
        TestHelper.checkInt("case3", 89, s.climbStairs(10));
    }''',
"HouseRobber": '''    public static void main(String[] args) {
        System.out.println("=== HouseRobber ===");
        HouseRobber s = new HouseRobber();
        TestHelper.checkInt("case1", 4, s.rob(new int[]{1, 2, 3, 1}));
        TestHelper.checkInt("case2", 12, s.rob(new int[]{2, 7, 9, 3, 1}));
        TestHelper.checkInt("case3", 0, s.rob(new int[]{}));
    }''',
"CoinChange": '''    public static void main(String[] args) {
        System.out.println("=== CoinChange ===");
        CoinChange s = new CoinChange();
        TestHelper.checkInt("case1", 3, s.coinChange(new int[]{1, 2, 5}, 11));
        TestHelper.checkInt("case2", -1, s.coinChange(new int[]{2}, 3));
        TestHelper.checkInt("case3", 0, s.coinChange(new int[]{1}, 0));
    }''',
"EditDistance": '''    public static void main(String[] args) {
        System.out.println("=== EditDistance ===");
        EditDistance s = new EditDistance();
        TestHelper.checkInt("case1", 3, s.minDistance("horse", "ros"));
        TestHelper.checkInt("case2", 5, s.minDistance("intention", "execution"));
        TestHelper.checkInt("case3", 0, s.minDistance("", ""));
    }''',
"BestTimeToBuyAndSellStock": '''    public static void main(String[] args) {
        System.out.println("=== BestTimeToBuyAndSellStock ===");
        BestTimeToBuyAndSellStock s = new BestTimeToBuyAndSellStock();
        TestHelper.checkInt("case1", 5, s.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
        TestHelper.checkInt("case2", 0, s.maxProfit(new int[]{7, 6, 4, 3, 1}));
        TestHelper.checkInt("case3", 1, s.maxProfit(new int[]{1, 2}));
    }''',
"JumpGame": '''    public static void main(String[] args) {
        System.out.println("=== JumpGame ===");
        JumpGame s = new JumpGame();
        TestHelper.checkBool("case1", true, s.canJump(new int[]{2, 3, 1, 1, 4}));
        TestHelper.checkBool("case2", false, s.canJump(new int[]{3, 2, 1, 0, 4}));
        TestHelper.checkBool("case3", true, s.canJump(new int[]{0}));
    }''',
"JumpGameII": '''    public static void main(String[] args) {
        System.out.println("=== JumpGameII ===");
        JumpGameII s = new JumpGameII();
        TestHelper.checkInt("case1", 2, s.jump(new int[]{2, 3, 1, 1, 4}));
        TestHelper.checkInt("case2", 1, s.jump(new int[]{1, 1, 1, 1}));
        TestHelper.checkInt("case3", 1, s.jump(new int[]{1}));
    }''',
"BinarySearch": '''    public static void main(String[] args) {
        System.out.println("=== BinarySearch ===");
        BinarySearch s = new BinarySearch();
        TestHelper.checkInt("case1", 4, s.search(new int[]{-1, 0, 3, 5, 9, 12}, 9));
        TestHelper.checkInt("case2", -1, s.search(new int[]{-1, 0, 3, 5, 9, 12}, 2));
        TestHelper.checkInt("case3", 0, s.search(new int[]{5}, 5));
    }''',
"SearchRotatedSortedArray": '''    public static void main(String[] args) {
        System.out.println("=== SearchRotatedSortedArray ===");
        SearchRotatedSortedArray s = new SearchRotatedSortedArray();
        TestHelper.checkInt("case1", 4, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
        TestHelper.checkInt("case2", -1, s.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
        TestHelper.checkInt("case3", 0, s.search(new int[]{1}, 1));
    }''',
"MedianOfTwoSortedArrays": '''    public static void main(String[] args) {
        System.out.println("=== MedianOfTwoSortedArrays ===");
        MedianOfTwoSortedArrays s = new MedianOfTwoSortedArrays();
        TestHelper.checkDouble("case1", 2.0, s.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));
        TestHelper.checkDouble("case2", 2.5, s.findMedianSortedArrays(new int[]{1, 2}, new int[]{3, 4}));
        TestHelper.checkDouble("case3", 1.0, s.findMedianSortedArrays(new int[]{1}, new int[]{}));
    }''',
"FloodFill": '''    public static void main(String[] args) {
        System.out.println("=== FloodFill ===");
        FloodFill s = new FloodFill();
        int[][] c1 = {{1, 1, 1}, {1, 1, 0}, {1, 0, 1}};
        TestHelper.printArray("case1 row0", s.floodFill(c1, 1, 1, 2)[0]);
        int[][] c2 = {{0, 0, 0}, {0, 0, 0}};
        TestHelper.checkInt("case2", 2, s.floodFill(c2, 0, 0, 2)[0][0]);
        int[][] c3 = {{1}};
        TestHelper.checkInt("case3", 0, s.floodFill(c3, 0, 0, 0)[0][0]);
    }''',
"NumberOfIslands": '''    public static void main(String[] args) {
        System.out.println("=== NumberOfIslands ===");
        NumberOfIslands s = new NumberOfIslands();
        TestHelper.checkInt("case1", 1, s.numIslands(new char[][]{{'1','1','1','1','0'},{'1','1','0','1','0'},{'1','1','0','0','0'},{'0','0','0','0','0'}}));
        TestHelper.checkInt("case2", 3, s.numIslands(new char[][]{{'1','1','0','0','0'},{'1','1','0','0','0'},{'0','0','1','0','0'},{'0','0','0','1','1'}}));
        TestHelper.checkInt("case3", 0, s.numIslands(new char[][]{{'0'}}));
    }''',
"CourseSchedule": '''    public static void main(String[] args) {
        System.out.println("=== CourseSchedule ===");
        CourseSchedule s = new CourseSchedule();
        TestHelper.checkBool("case1", true, s.canFinish(2, new int[][]{{1, 0}}));
        TestHelper.checkBool("case2", false, s.canFinish(2, new int[][]{{1, 0}, {0, 1}}));
        TestHelper.checkBool("case3", true, s.canFinish(1, new int[][]{}));
    }''',
"WordLadder": '''    public static void main(String[] args) {
        System.out.println("=== WordLadder ===");
        WordLadder s = new WordLadder();
        TestHelper.checkInt("case1", 5, s.ladderLength("hit", "cog", java.util.Arrays.asList("hot","dot","dog","lot","log","cog")));
        TestHelper.checkInt("case2", 0, s.ladderLength("hit", "cog", java.util.Arrays.asList("hot","dot","dog","lot","log")));
        TestHelper.checkInt("case3", 2, s.ladderLength("a", "c", java.util.Arrays.asList("a","b","c")));
    }''',
"LastStoneWeight": '''    public static void main(String[] args) {
        System.out.println("=== LastStoneWeight ===");
        LastStoneWeight s = new LastStoneWeight();
        TestHelper.checkInt("case1", 1, s.lastStoneWeight(new int[]{2, 7, 4, 1, 8, 1}));
        TestHelper.checkInt("case2", 0, s.lastStoneWeight(new int[]{1, 1}));
        TestHelper.checkInt("case3", 6, s.lastStoneWeight(new int[]{6}));
    }''',
"KthLargestElement": '''    public static void main(String[] args) {
        System.out.println("=== KthLargestElement ===");
        KthLargestElement s = new KthLargestElement();
        TestHelper.checkInt("case1", 5, s.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
        TestHelper.checkInt("case2", 4, s.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
        TestHelper.checkInt("case3", 1, s.findKthLargest(new int[]{1}, 1));
    }''',
"TopKFrequentElements": '''    public static void main(String[] args) {
        System.out.println("=== TopKFrequentElements ===");
        TopKFrequentElements s = new TopKFrequentElements();
        System.out.println("case1: " + java.util.Arrays.toString(s.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2)));
        TestHelper.checkArray("case2", new int[]{1}, s.topKFrequent(new int[]{1}, 1));
        System.out.println("case3: " + java.util.Arrays.toString(s.topKFrequent(new int[]{4, 1, -1, 2, -1, 2, 3}, 2)));
    }''',
"MedianFinder": '''    public static void main(String[] args) {
        System.out.println("=== MedianFinder ===");
        MedianFinder mf = new MedianFinder();
        mf.addNum(1); mf.addNum(2);
        TestHelper.checkDouble("case1", 1.5, mf.findMedian());
        mf.addNum(3);
        TestHelper.checkDouble("case2", 2.0, mf.findMedian());
        mf.addNum(4);
        TestHelper.checkDouble("case3", 2.5, mf.findMedian());
    }''',
"RedundantConnection": '''    public static void main(String[] args) {
        System.out.println("=== RedundantConnection ===");
        RedundantConnection s = new RedundantConnection();
        TestHelper.checkArray("case1", new int[]{2, 3}, s.findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}}));
        TestHelper.checkArray("case2", new int[]{1, 4}, s.findRedundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}, {3, 4}, {1, 4}}));
        TestHelper.checkArray("case3", new int[]{2, 3}, s.findRedundantConnection(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}}));
    }''',
"NumberOfProvinces": '''    public static void main(String[] args) {
        System.out.println("=== NumberOfProvinces ===");
        NumberOfProvinces s = new NumberOfProvinces();
        TestHelper.checkInt("case1", 2, s.findCircleNum(new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}));
        TestHelper.checkInt("case2", 3, s.findCircleNum(new int[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}}));
        TestHelper.checkInt("case3", 1, s.findCircleNum(new int[][]{{1, 1}, {1, 1}}));
    }''',
"RedundantConnectionII": '''    public static void main(String[] args) {
        System.out.println("=== RedundantConnectionII ===");
        RedundantConnectionII s = new RedundantConnectionII();
        TestHelper.checkArray("case1", new int[]{2, 3}, s.findRedundantDirectedEdge(new int[][]{{1, 2}, {1, 3}, {2, 3}}));
        TestHelper.checkArray("case2", new int[]{3, 4}, s.findRedundantDirectedEdge(new int[][]{{1, 2}, {2, 3}, {3, 4}, {4, 1}, {1, 5}}));
        TestHelper.checkArray("case3", new int[]{2, 5}, s.findRedundantDirectedEdge(new int[][]{{2, 1}, {3, 1}, {4, 1}, {1, 5}}));
    }''',
"ValidAnagram": '''    public static void main(String[] args) {
        System.out.println("=== ValidAnagram ===");
        ValidAnagram s = new ValidAnagram();
        TestHelper.checkBool("case1", true, s.isAnagram("anagram", "nagaram"));
        TestHelper.checkBool("case2", false, s.isAnagram("rat", "car"));
        TestHelper.checkBool("case3", true, s.isAnagram("a", "a"));
    }''',
"LongestPalindromicSubstring": '''    public static void main(String[] args) {
        System.out.println("=== LongestPalindromicSubstring ===");
        LongestPalindromicSubstring s = new LongestPalindromicSubstring();
        TestHelper.checkString("case1", "bab", s.longestPalindrome("babad"));
        TestHelper.checkString("case2", "bb", s.longestPalindrome("cbbd"));
        TestHelper.checkString("case3", "a", s.longestPalindrome("a"));
    }''',
"DecodeString": '''    public static void main(String[] args) {
        System.out.println("=== DecodeString ===");
        DecodeString s = new DecodeString();
        TestHelper.checkString("case1", "aaabcbc", s.decodeString("3[a]2[bc]"));
        TestHelper.checkString("case2", "accaccacc", s.decodeString("3[a2[c]]"));
        TestHelper.checkString("case3", "abcabccd", s.decodeString("2[abc]3[cd]ef"));
    }''',
"RegularExpressionMatching": '''    public static void main(String[] args) {
        System.out.println("=== RegularExpressionMatching ===");
        RegularExpressionMatching s = new RegularExpressionMatching();
        TestHelper.checkBool("case1", true, s.isMatch("aa", "a*"));
        TestHelper.checkBool("case2", false, s.isMatch("aa", "a"));
        TestHelper.checkBool("case3", true, s.isMatch("ab", ".*"));
    }''',
"PalindromeNumber": '''    public static void main(String[] args) {
        System.out.println("=== PalindromeNumber ===");
        PalindromeNumber s = new PalindromeNumber();
        TestHelper.checkBool("case1", true, s.isPalindrome(121));
        TestHelper.checkBool("case2", false, s.isPalindrome(-121));
        TestHelper.checkBool("case3", false, s.isPalindrome(10));
    }''',
"ReverseInteger": '''    public static void main(String[] args) {
        System.out.println("=== ReverseInteger ===");
        ReverseInteger s = new ReverseInteger();
        TestHelper.checkInt("case1", 321, s.reverse(123));
        TestHelper.checkInt("case2", -321, s.reverse(-123));
        TestHelper.checkInt("case3", 0, s.reverse(1534236469));
    }''',
"SquaresOfSortedArray": '''    public static void main(String[] args) {
        System.out.println("=== SquaresOfSortedArray ===");
        SquaresOfSortedArray s = new SquaresOfSortedArray();
        TestHelper.checkArray("case1", new int[]{0, 1, 9, 16, 100}, s.sortedSquares(new int[]{-4, -1, 0, 3, 10}));
        TestHelper.checkArray("case2", new int[]{0, 0, 1, 1}, s.sortedSquares(new int[]{-1, 0, 1, 2}));
        TestHelper.checkArray("case3", new int[]{4}, s.sortedSquares(new int[]{-2}));
    }''',
"MergeIntervals": '''    public static void main(String[] args) {
        System.out.println("=== MergeIntervals ===");
        MergeIntervals s = new MergeIntervals();
        TestHelper.checkArray("case1 row0", new int[]{1, 6}, s.merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}})[0]);
        TestHelper.checkArray("case2", new int[]{1, 5}, s.merge(new int[][]{{1, 4}, {4, 5}})[0]);
        System.out.println("case3 len: " + s.merge(new int[][]{{1, 4}, {0, 4}}).length);
    }''',
"SortColors": '''    public static void main(String[] args) {
        System.out.println("=== SortColors ===");
        SortColors s = new SortColors();
        int[] c1 = {2, 0, 2, 1, 1, 0}; s.sortColors(c1);
        TestHelper.checkArray("case1", new int[]{0, 0, 1, 1, 2, 2}, c1);
        int[] c2 = {2, 0, 1}; s.sortColors(c2);
        TestHelper.checkArray("case2", new int[]{0, 1, 2}, c2);
        int[] c3 = {0}; s.sortColors(c3);
        TestHelper.checkArray("case3", new int[]{0}, c3);
    }''',
"LargestNumber": '''    public static void main(String[] args) {
        System.out.println("=== LargestNumber ===");
        LargestNumber s = new LargestNumber();
        TestHelper.checkString("case1", "9534330", s.largestNumber(new int[]{3, 30, 34, 5, 9}));
        TestHelper.checkString("case2", "0", s.largestNumber(new int[]{0, 0}));
        TestHelper.checkString("case3", "210", s.largestNumber(new int[]{2, 10}));
    }''',
})


def inject(path: Path, class_name: str):
    text = path.read_text(encoding="utf-8")
    if "public static void main" in text:
        return False
    if class_name not in MAINS:
        print(f"SKIP no main: {class_name}")
        return False
    if "import com.john.algorithm.common.TestHelper;" not in text:
        pkg_end = text.find("\n", text.find("package "))
        text = text[:pkg_end + 1] + "\n" + IMPORT + text[pkg_end + 1:]
    # add checkDouble to TestHelper if needed - script uses it
    main_block = "\n" + MAINS[class_name] + "\n"
    if not text.rstrip().endswith("}"):
        return False
    text = text.rstrip()[:-1] + main_block + "}\n"
    path.write_text(text, encoding="utf-8")
    return True


def main():
    updated = 0
    for path in sorted(ROOT.rglob("*.java")):
        if "/common/" in str(path):
            continue
        cls = path.stem
        if inject(path, cls):
            updated += 1
            print(f"OK {path.relative_to(ROOT)}")
    print(f"Updated {updated} files")


if __name__ == "__main__":
    main()
