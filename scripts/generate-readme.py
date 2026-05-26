#!/usr/bin/env python3
"""从解法类 Javadoc 自动生成 README.md 题目表格。"""

import re
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/john/algorithm"
README = ROOT / "README.md"

CATEGORIES = [
    ("array", "数组"),
    ("twopointer", "双指针"),
    ("slidingwindow", "滑动窗口"),
    ("hashmap", "哈希表"),
    ("linkedlist", "链表"),
    ("stack", "栈"),
    ("queue", "队列"),
    ("binarytree", "二叉树"),
    ("backtracking", "回溯"),
    ("dynamicprogramming", "动态规划"),
    ("greedy", "贪心"),
    ("binarysearch", "二分查找"),
    ("graph", "图"),
    ("heap", "堆"),
    ("unionfind", "并查集"),
    ("string", "字符串"),
    ("math", "数学"),
    ("sorting", "排序"),
]
DIFFS = ["easy", "medium", "hard"]


def parse_file(path: Path) -> dict:
    text = path.read_text(encoding="utf-8")
    title_match = re.search(r"\* LeetCode ([^\n]+)", text)
    title = title_match.group(1).strip() if title_match else path.stem
    companies = re.search(r"\* <p>常见公司：([^\n]+)", text)
    rate = re.search(r"\* <p>LeetCode 通过率：约 ([^\n]+)", text)
    short = title.split(". ", 1)[1] if ". " in title else title
    lc_num = title.split(".")[0]
    rel = str(path.relative_to(ROOT)).replace("\\", "/")
    java_root = ROOT / "src/main/java"
    fqn = path.relative_to(java_root).with_suffix("").as_posix().replace("/", ".")
    return {
        "short": short,
        "lc_num": lc_num,
        "companies": companies.group(1).strip() if companies else "—",
        "rate": rate.group(1).strip() if rate else "—",
        "link": rel,
        "fqn": fqn,
    }


def build_table() -> tuple[str, int]:
    data = defaultdict(lambda: defaultdict(list))
    for path in sorted(SRC.rglob("*.java")):
        if "/common/" in str(path):
            continue
        parts = path.parts
        idx = parts.index("algorithm")
        cat, diff = parts[idx + 1], parts[idx + 2]
        if diff in DIFFS:
            data[cat][diff].append(parse_file(path))

    def cell(items: list) -> str:
        if not items:
            return "—"
        return "<br><br>".join(
            f"[{it['lc_num']} {it['short']}](/{it['link']})<br>"
            f"类：`{it['fqn']}`<br>"
            f"大厂：{it['companies']}<br>通过率：{it['rate']}"
            for it in items
        )

    rows = []
    for cat_key, cat_name in CATEGORIES:
        cols = [cell(data[cat_key][d]) for d in DIFFS]
        rows.append("| **" + cat_name + "** | " + " | ".join(cols) + " |")

    total = sum(len(data[c][d]) for c, _ in CATEGORIES for d in DIFFS)
    table = "| 类别 | Easy | Medium | Hard |\n| --- | --- | --- | --- |\n" + "\n".join(rows)
    return table, total


def main() -> None:
    table, total = build_table()
    content = f"""# john-algorithm-java

Java 8 + Maven 算法题整理项目。按**解法类别 → 难度 → 题目**组织，每道题对应一个 Java 解法类，类头 Javadoc 含题目描述、面试考频、常见公司与 LeetCode 通过率，主方法含核心解法说明。

## 技术栈

- Java 8
- Maven 3.x

## 项目结构

```
src/main/java/com/john/algorithm/
├── common/          # ListNode、TreeNode、TestHelper
├── {{category}}/     # 解法类别（array、hashmap、dp ...）
│   ├── easy/
│   ├── medium/
│   └── hard/
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

## 题目一览（共 {total} 题）

> 在 IDEA **源码编辑区** Ctrl/⌘ + 单击题目名打开解法类；每题附全限定类名便于 Ctrl+N 跳转。大厂与通过率仅供参考。

{table}

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
"""
    README.write_text(content, encoding="utf-8")
    print(f"Generated {README} ({total} problems)")


if __name__ == "__main__":
    main()
