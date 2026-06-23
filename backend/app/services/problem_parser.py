from __future__ import annotations

import json
import logging
import re
from pathlib import Path

from app.constants import CATEGORIES, DIFF_LABELS

logger = logging.getLogger(__name__)


def strip_html(text: str) -> str:
    return re.sub(r"\s+", " ", text.replace("<p>", "").replace("</p>", " ").strip())


def parse_javadoc_block(text: str, tag: str) -> str:
    match = re.search(rf"\*\s*<p>{re.escape(tag)}[：:]([^\n]+)", text)
    return match.group(1).strip() if match else ""


def parse_method_doc(text: str) -> dict[str, str]:
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


def parse_java_file(path: Path, root: Path) -> dict | None:
    text = path.read_text(encoding="utf-8")
    parts = path.parts
    if "algorithm" not in parts:
        return None
    idx = parts.index("algorithm")
    if idx + 2 >= len(parts):
        return None
    category = parts[idx + 1]
    difficulty = parts[idx + 2]
    if difficulty not in DIFF_LABELS or category == "common":
        return None

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

    rel = path.relative_to(root).as_posix()
    fqn = path.relative_to(root / "src/main/java").with_suffix("").as_posix().replace("/", ".")
    method_doc = parse_method_doc(text)
    solution_code = extract_solution_code(text)
    freq_level = "极高" if "极高" in frequency else "高" if frequency.startswith("高") else "中"
    slug = f"{category}-{difficulty}-{path.stem}"

    return {
        "slug": slug,
        "type_code": "leetcode",
        "lc_num": lc_num,
        "title": short_title,
        "full_title": title,
        "difficulty": difficulty,
        "category_code": category,
        "description": description,
        "example": example,
        "frequency": frequency,
        "freq_level": freq_level,
        "companies": companies,
        "pass_rate": pass_rate,
        "pass_rate_text": pass_rate_raw,
        "file_path": rel,
        "idea_path": f"/{rel}",
        "solution_fqn": fqn,
        "class_name": path.stem,
        "run_command": f"java -cp target/classes {fqn}",
        "solution_code": solution_code,
        "code_lines": len(solution_code.splitlines()) if solution_code else 0,
        **method_doc,
        "extra": {},
    }


def scan_java_problems(repo_root: Path) -> list[dict]:
    src = repo_root / "src/main/java/com/john/algorithm"
    results: list[dict] = []
    for path in sorted(src.rglob("*.java")):
        if "/common/" in str(path):
            continue
        parsed = parse_java_file(path, repo_root)
        if parsed:
            results.append(parsed)
    return results
