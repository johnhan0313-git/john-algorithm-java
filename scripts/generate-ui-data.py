#!/usr/bin/env python3
"""从 Java 解法类生成 ui/data.js 供可视化面板使用。"""

import json
import re
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
SRC = ROOT / "src/main/java/com/john/algorithm"
OUT = ROOT / "ui/data.js"

CATEGORIES = {
    "array": "数组",
    "twopointer": "双指针",
    "slidingwindow": "滑动窗口",
    "hashmap": "哈希表",
    "linkedlist": "链表",
    "stack": "栈",
    "queue": "队列",
    "binarytree": "二叉树",
    "backtracking": "回溯",
    "dynamicprogramming": "动态规划",
    "greedy": "贪心",
    "binarysearch": "二分查找",
    "graph": "图",
    "heap": "堆",
    "unionfind": "并查集",
    "string": "字符串",
    "math": "数学",
    "sorting": "排序",
}

DIFF_LABELS = {"easy": "Easy", "medium": "Medium", "hard": "Hard"}


def strip_html(text: str) -> str:
    return re.sub(r"\s+", " ", text.replace("<p>", "").replace("</p>", " ").strip())


def parse_javadoc_block(text: str, tag: str) -> str:
    match = re.search(rf"\*\s*<p>{re.escape(tag)}[：:]([^\n]+)", text)
    return match.group(1).strip() if match else ""


def parse_method_doc(text: str) -> dict:
    defaults = {"summary": "", "approach": "", "notes": "", "pitfalls": ""}
    for match in re.finditer(r"/\*\*(.*?)\*/", text, re.DOTALL):
        after = text[match.end() : match.end() + 100]
        if re.match(r"\s*public\s+class\b", after):
            continue
        if re.match(r"\s*public\s+static\s+void\s+main\b", after):
            continue
        if not re.match(r"\s*public\s+", after):
            continue
        block = match.group(1)
        if "核心解法" not in block:
            continue
        summary = ""
        for line in block.split("\n"):
            line = line.strip().lstrip("* ").strip()
            if not line or line.startswith("<p>"):
                continue
            if any(k in line for k in ("核心解法", "注意点", "疑难点", "LeetCode")):
                continue
            summary = line
            break
        return {
            "summary": summary,
            "approach": parse_javadoc_block(block, "核心解法"),
            "notes": parse_javadoc_block(block, "注意点"),
            "pitfalls": parse_javadoc_block(block, "疑难点"),
        }
    return defaults


def remove_main_method(text: str) -> str:
    match = re.search(r"public\s+static\s+void\s+main\s*\(\s*String\[\]\s+\w+\s*\)\s*\{", text)
    if not match:
        return text
    start = match.start()
    brace_at = match.end() - 1
    depth = 0
    for i in range(brace_at, len(text)):
        if text[i] == "{":
            depth += 1
        elif text[i] == "}":
            depth -= 1
            if depth == 0:
                head = text[:start].rstrip()
                tail = text[i + 1 :].lstrip()
                return f"{head}\n{tail}".strip() if tail else head
    return text


def strip_javadocs(text: str) -> str:
    return re.sub(r"/\*\*.*?\*/", "", text, flags=re.DOTALL)


def extract_solution_code(text: str) -> str:
    class_match = re.search(r"public\s+class\s+\w+", text)
    if not class_match:
        return ""
    code = text[class_match.start() :]
    code = remove_main_method(code)
    code = strip_javadocs(code)
    code = re.sub(r"[ \t]+\n", "\n", code)
    code = re.sub(r"\n{3,}", "\n\n", code)
    return code.strip()


def parse_file(path: Path) -> dict:
    text = path.read_text(encoding="utf-8")
    parts = path.parts
    idx = parts.index("algorithm")
    category = parts[idx + 1]
    difficulty = parts[idx + 2]

    title_match = re.search(r"\* LeetCode ([^\n]+)", text)
    title = title_match.group(1).strip() if title_match else path.stem
    lc_num = title.split(".")[0].strip()
    short_title = title.split(". ", 1)[1] if ". " in title else title

    desc_match = re.search(r"\* LeetCode [^\n]+\n \*\n \* <p>([^\n]+)", text)
    description = desc_match.group(1).strip() if desc_match else ""

    example = parse_javadoc_block(text, "示例")
    frequency = parse_javadoc_block(text, "面试考频")
    companies = parse_javadoc_block(text, "常见公司")
    pass_rate_raw = parse_javadoc_block(text, "LeetCode 通过率")
    pass_rate = float(re.search(r"([\d.]+)", pass_rate_raw).group(1)) if pass_rate_raw else 0.0

    rel = path.relative_to(ROOT).as_posix()
    fqn = path.relative_to(ROOT / "src/main/java").with_suffix("").as_posix().replace("/", ".")

    method_doc = parse_method_doc(text)
    solution_code = extract_solution_code(text)
    freq_level = "极高" if "极高" in frequency else "高" if frequency.startswith("高") else "中"

    return {
        "id": f"{category}-{difficulty}-{path.stem}",
        "lcNum": lc_num,
        "title": short_title,
        "fullTitle": title,
        "description": description,
        "example": example,
        "category": category,
        "categoryLabel": CATEGORIES.get(category, category),
        "difficulty": difficulty,
        "difficultyLabel": DIFF_LABELS.get(difficulty, difficulty),
        "frequency": frequency,
        "freqLevel": freq_level,
        "companies": companies,
        "passRate": pass_rate,
        "passRateText": pass_rate_raw,
        "filePath": rel,
        "ideaPath": f"/{rel}",
        "fqn": fqn,
        "className": path.stem,
        "runCommand": f"java -cp target/classes {fqn}",
        "solutionCode": solution_code,
        "codeLines": len(solution_code.splitlines()) if solution_code else 0,
        **method_doc,
    }


def main() -> None:
    problems = []
    for path in sorted(SRC.rglob("*.java")):
        if "/common/" in str(path):
            continue
        parts = path.parts
        idx = parts.index("algorithm")
        if parts[idx + 2] not in DIFF_LABELS:
            continue
        problems.append(parse_file(path))

    stats = {
        "total": len(problems),
        "byDifficulty": defaultdict(int),
        "byCategory": defaultdict(int),
        "highFreq": sum(1 for p in problems if p["freqLevel"] == "极高"),
    }
    for p in problems:
        stats["byDifficulty"][p["difficulty"]] += 1
        stats["byCategory"][p["categoryLabel"]] += 1

    payload = {
        "generatedAt": __import__("datetime").datetime.now().isoformat(timespec="seconds"),
        "stats": {
            "total": stats["total"],
            "highFreq": stats["highFreq"],
            "byDifficulty": dict(stats["byDifficulty"]),
            "byCategory": dict(stats["byCategory"]),
        },
        "categories": [{"key": k, "label": v} for k, v in CATEGORIES.items()],
        "problems": problems,
    }

    OUT.parent.mkdir(parents=True, exist_ok=True)
    OUT.write_text(
        "// AUTO-GENERATED by scripts/generate-ui-data.py\n"
        f"window.ALGO_DATA = {json.dumps(payload, ensure_ascii=False, indent=2)};\n",
        encoding="utf-8",
    )
    print(f"Generated {OUT} ({len(problems)} problems)")


if __name__ == "__main__":
    main()
