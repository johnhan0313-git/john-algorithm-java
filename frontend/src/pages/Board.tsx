import { useCallback, useEffect, useMemo, useState } from "react";

import { useAuth } from "../contexts/AuthContext";
import { problemsApi, progressApi } from "../lib/api";
import { renderJavaCode, shortCompanies } from "../lib/codeHighlight";
import type { CategoryMeta, ProblemDetail, ProblemSummary, StatsResponse } from "../types";

type SortBy = "category" | "passRateAsc" | "passRateDesc" | "lcNum";

export default function BoardPage() {
  const { user, logout } = useAuth();
  const [problems, setProblems] = useState<ProblemSummary[]>([]);
  const [categories, setCategories] = useState<CategoryMeta[]>([]);
  const [stats, setStats] = useState<StatsResponse | null>(null);
  const [progress, setProgress] = useState<Record<string, boolean>>({});
  const [search, setSearch] = useState("");
  const [difficulties, setDifficulties] = useState<Set<string>>(new Set());
  const [freqLevels, setFreqLevels] = useState<Set<string>>(new Set());
  const [categoryFilter, setCategoryFilter] = useState<Set<string>>(new Set());
  const [sortBy, setSortBy] = useState<SortBy>("category");
  const [showTodoOnly, setShowTodoOnly] = useState(false);
  const [selectedId, setSelectedId] = useState<string | null>(null);
  const [detail, setDetail] = useState<ProblemDetail | null>(null);
  const [detailTab, setDetailTab] = useState<"insight" | "code">("insight");
  const [toast, setToast] = useState("");
  const [loading, setLoading] = useState(true);

  const loadData = useCallback(async () => {
    setLoading(true);
    try {
      const [list, statResp, progResp] = await Promise.all([
        problemsApi.list({}),
        problemsApi.stats(),
        progressApi.getAll(),
      ]);
      setProblems(list.items);
      setCategories(list.categories);
      setStats(statResp);
      const doneMap: Record<string, boolean> = {};
      Object.entries(progResp.items).forEach(([slug, item]) => {
        if (item.status === "done") doneMap[slug] = true;
      });
      setProgress(doneMap);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const toggleSet = (set: Set<string>, value: string, setter: (s: Set<string>) => void) => {
    const next = new Set(set);
    if (next.has(value)) next.delete(value);
    else next.add(value);
    setter(next);
  };

  const filtered = useMemo(() => {
    const q = search.trim().toLowerCase();
    return problems
      .filter((p) => {
        if (difficulties.size && !difficulties.has(p.difficulty)) return false;
        if (freqLevels.size && !freqLevels.has(p.freq_level)) return false;
        if (categoryFilter.size && !categoryFilter.has(p.category)) return false;
        if (showTodoOnly && progress[p.id]) return false;
        if (q) {
          const hay = [p.lc_num, p.title, p.full_title, p.companies, p.fqn, p.category_label, p.class_name, p.summary]
            .join(" ")
            .toLowerCase();
          if (!hay.includes(q)) return false;
        }
        return true;
      })
      .sort((a, b) => {
        switch (sortBy) {
          case "passRateAsc":
            return a.pass_rate - b.pass_rate;
          case "passRateDesc":
            return b.pass_rate - a.pass_rate;
          case "lcNum":
            return parseInt(a.lc_num, 10) - parseInt(b.lc_num, 10);
          default:
            if (a.category_label !== b.category_label) {
              return a.category_label.localeCompare(b.category_label, "zh");
            }
            const order = { easy: 0, medium: 1, hard: 2 };
            return (
              (order[a.difficulty as keyof typeof order] ?? 0) -
                (order[b.difficulty as keyof typeof order] ?? 0) ||
              a.title.localeCompare(b.title, "zh")
            );
        }
      });
  }, [problems, difficulties, freqLevels, categoryFilter, showTodoOnly, progress, search, sortBy]);

  const doneCount = Object.keys(progress).length;
  const total = stats?.total ?? problems.length;
  const pct = total ? Math.round((doneCount / total) * 100) : 0;

  const showToast = (msg: string) => {
    setToast(msg);
    setTimeout(() => setToast(""), 2200);
  };

  const copyText = (text: string) => {
    navigator.clipboard.writeText(text).then(() => showToast("已复制到剪贴板"));
  };

  const openDetail = async (id: string) => {
    setSelectedId(id);
    setDetailTab("insight");
    const d = await problemsApi.detail(id);
    setDetail(d);
  };

  const closeDetail = () => {
    setSelectedId(null);
    setDetail(null);
  };

  const toggleDone = async () => {
    if (!detail) return;
    const isDone = !!progress[detail.id];
    const nextStatus = isDone ? "todo" : "done";
    await progressApi.upsert(detail.id, nextStatus);
    setProgress((prev) => {
      const copy = { ...prev };
      if (nextStatus === "done") copy[detail.id] = true;
      else delete copy[detail.id];
      return copy;
    });
    setProblems((items) =>
      items.map((p) => (p.id === detail.id ? { ...p, done: nextStatus === "done" } : p)),
    );
    setDetail({ ...detail, done: nextStatus === "done" });
  };

  const resetProgress = async () => {
    if (!confirm("确定重置所有学习进度？")) return;
    const doneSlugs = Object.keys(progress);
    await Promise.all(doneSlugs.map((slug) => progressApi.upsert(slug, "todo")));
    setProgress({});
    setProblems((items) => items.map((p) => ({ ...p, done: false })));
    showToast("进度已重置");
  };

  if (loading) {
    return <div className="login-page">加载题目…</div>;
  }

  return (
    <div className="app">
      <aside className="sidebar">
        <div className="brand">
          <h1>算法助手</h1>
          <p>john-algorithm-java</p>
        </div>

        <div className="sidebar-section user-bar">
          <span className="label">账号</span>
          <p className="user-email">{user?.email || user?.display_name}</p>
          <button type="button" className="btn ghost" onClick={logout}>
            退出登录
          </button>
        </div>

        <div className="sidebar-section">
          <label className="label" htmlFor="search">
            搜索
          </label>
          <input
            id="search"
            type="search"
            placeholder="题号 / 标题 / 公司 / 类名…"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>

        <div className="sidebar-section">
          <span className="label">难度</span>
          <div className="chip-group">
            {(["easy", "medium", "hard"] as const).map((d) => (
              <button
                key={d}
                type="button"
                className={`chip ${difficulties.has(d) ? "active" : ""}`}
                onClick={() => toggleSet(difficulties, d, setDifficulties)}
              >
                {{ easy: "Easy", medium: "Medium", hard: "Hard" }[d]}
              </button>
            ))}
          </div>
        </div>

        <div className="sidebar-section">
          <span className="label">考频</span>
          <div className="chip-group">
            {["极高", "高", "中"].map((f) => (
              <button
                key={f}
                type="button"
                className={`chip ${freqLevels.has(f) ? "active" : ""}`}
                onClick={() => toggleSet(freqLevels, f, setFreqLevels)}
              >
                {f}
              </button>
            ))}
          </div>
        </div>

        <div className="sidebar-section">
          <span className="label">类别</span>
          <div className="category-list">
            {categories.map((c) => (
              <button
                key={c.key}
                type="button"
                className={`category-item ${categoryFilter.has(c.key) ? "active" : ""}`}
                onClick={() => toggleSet(categoryFilter, c.key, setCategoryFilter)}
              >
                <span>{c.label}</span>
                <span>{problems.filter((p) => p.category === c.key).length}</span>
              </button>
            ))}
          </div>
        </div>

        <div className="sidebar-section progress-box">
          <span className="label">学习进度</span>
          <div className="progress-bar">
            <div id="progressFill" style={{ width: `${pct}%` }} />
          </div>
          <p id="progressText">
            {doneCount} / {total} 已完成（{pct}%）
          </p>
          <div className="progress-actions">
            <button
              type="button"
              id="showTodoBtn"
              className={`btn ghost ${showTodoOnly ? "active" : ""}`}
              onClick={() => setShowTodoOnly((v) => !v)}
            >
              仅看未完成
            </button>
            <button type="button" className="btn ghost danger" onClick={resetProgress}>
              重置进度
            </button>
          </div>
        </div>
      </aside>

      <main className="main">
        <header className="topbar">
          <div id="statsCards" className="stats">
            {stats && (
              <>
                <div className="stat-card">
                  <strong>{stats.total}</strong>
                  <span>题目总数</span>
                </div>
                <div className="stat-card">
                  <strong>{stats.high_freq}</strong>
                  <span>极高频</span>
                </div>
                <div className="stat-card">
                  <strong>{stats.by_difficulty.hard || 0}</strong>
                  <span>Hard</span>
                </div>
                <div className="stat-card">
                  <strong>{Object.keys(stats.by_category).length}</strong>
                  <span>解法类别</span>
                </div>
              </>
            )}
          </div>
          <div className="topbar-actions">
            <select id="sortBy" value={sortBy} onChange={(e) => setSortBy(e.target.value as SortBy)}>
              <option value="category">按类别</option>
              <option value="passRateAsc">通过率 ↑（难→易）</option>
              <option value="passRateDesc">通过率 ↓（易→难）</option>
              <option value="lcNum">按题号</option>
            </select>
            <button type="button" className="btn" onClick={loadData}>
              刷新数据
            </button>
          </div>
        </header>

        <section id="problemGrid" className="grid">
          {filtered.map((p) => (
            <article
              key={p.id}
              className={`card ${progress[p.id] ? "done" : ""}`}
              onClick={() => openDetail(p.id)}
              onKeyDown={(e) => e.key === "Enter" && openDetail(p.id)}
              role="button"
              tabIndex={0}
            >
              <div className="card-head">
                <h3>
                  LC{p.lc_num} {p.title}
                </h3>
                <span className={`badge ${p.difficulty}`}>{p.difficulty_label}</span>
              </div>
              <div className="card-meta">
                {p.category_label} · 通过率 {p.pass_rate}%
              </div>
              <div className="card-tags">
                <span className={`tag ${p.freq_level === "极高" ? "high" : ""}`}>{p.freq_level}</span>
                <span className="tag">{shortCompanies(p.companies)}</span>
              </div>
              <div className="card-foot">
                <span>{progress[p.id] ? "✓ 已完成" : "思路 + Java 实现"}</span>
                <span>{p.code_lines ? `${p.code_lines} 行` : p.summary || "打开详情"}</span>
              </div>
            </article>
          ))}
        </section>
        <div id="emptyState" className={`empty ${filtered.length ? "hidden" : ""}`}>
          没有匹配的题目，试试调整筛选条件。
        </div>
      </main>

      {detail && selectedId && (
        <dialog open className={detailTab === "code" ? "code-mode" : ""} onClose={closeDetail}>
          <div className="dialog-header">
            <div>
              <p className="detail-meta">
                LeetCode {detail.lc_num} · {detail.category_label} · {detail.difficulty_label}
              </p>
              <h2>{detail.title}</h2>
            </div>
            <button type="button" className="icon-btn" aria-label="关闭" onClick={closeDetail}>
              ×
            </button>
          </div>
          <div className="dialog-body">
            <div className="detail-tags">
              <span className={`tag ${detail.freq_level === "极高" ? "high" : ""}`}>{detail.freq_level}</span>
              <span className="tag">通过率 {detail.pass_rate}%</span>
              <span className="tag">{detail.companies}</span>
            </div>

            <div className="detail-tabs" role="tablist">
              <button
                type="button"
                className={`detail-tab ${detailTab === "insight" ? "active" : ""}`}
                onClick={() => setDetailTab("insight")}
              >
                解题思路
              </button>
              <button
                type="button"
                className={`detail-tab ${detailTab === "code" ? "active" : ""}`}
                onClick={() => setDetailTab("code")}
              >
                Java 实现
              </button>
            </div>

            <div id="panelInsight" className={`detail-panel ${detailTab === "insight" ? "active" : ""}`}>
              <section>
                <h3>题目描述</h3>
                <p>{detail.description || "—"}</p>
                {detail.example && <p className="example">示例：{detail.example}</p>}
              </section>
              <section className="detail-grid insight-cards">
                <div className="insight-card">
                  <h3>核心解法</h3>
                  <p>{detail.approach || "—"}</p>
                </div>
                <div className="insight-card">
                  <h3>注意点</h3>
                  <p>{detail.notes || "—"}</p>
                </div>
                <div className="insight-card">
                  <h3>疑难点</h3>
                  <p>{detail.pitfalls || "—"}</p>
                </div>
              </section>
              <section className="detail-actions">
                <div className="copy-row">
                  <code>{detail.fqn}</code>
                  <button type="button" className="btn" onClick={() => copyText(detail.fqn)}>
                    复制类名
                  </button>
                </div>
                <div className="copy-row">
                  <code>{detail.idea_path}</code>
                  <button type="button" className="btn" onClick={() => copyText(detail.idea_path)}>
                    复制路径
                  </button>
                </div>
                <div className="copy-row">
                  <code>{detail.run_command}</code>
                  <button type="button" className="btn" onClick={() => copyText(detail.run_command)}>
                    复制运行命令
                  </button>
                </div>
              </section>
            </div>

            <div id="panelCode" className={`detail-panel ${detailTab === "code" ? "active" : ""}`}>
              <div className="code-toolbar">
                <div className="code-meta">
                  <span className="code-filename">{detail.class_name}.java</span>
                  <span className="code-lines">
                    {detail.code_lines || 0} 行 · {detail.fqn}
                  </span>
                </div>
                <button type="button" className="btn" onClick={() => copyText(detail.solution_code)}>
                  复制代码
                </button>
              </div>
              <div className="code-panel">
                <div
                  className="code-block"
                  dangerouslySetInnerHTML={{
                    __html: renderJavaCode(detail.solution_code || "// 暂无代码"),
                  }}
                />
              </div>
            </div>
          </div>
          <div className="dialog-footer">
            <button type="button" className={`btn ${progress[detail.id] ? "" : "primary"}`} onClick={toggleDone}>
              {progress[detail.id] ? "取消完成标记" : "标记为已完成"}
            </button>
          </div>
        </dialog>
      )}

      <div className={`toast ${toast ? "show" : ""}`}>{toast}</div>
    </div>
  );
}
