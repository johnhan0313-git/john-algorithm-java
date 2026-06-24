import { useCallback, useEffect, useMemo, useRef, useState } from "react";

import { AppLogo } from "@johnhan0313-git/shared/brand";
import PageHeader from "../components/PageHeader";
import { useAuth } from "../contexts/AuthContext";
import { useMediaQuery } from "../hooks/useMediaQuery";
import { problemsApi, progressApi } from "../lib/api";
import { DRAWER_LAYOUT_MQ } from "../lib/breakpoints";
import { renderJavaCode, shortCompanies } from "../lib/codeHighlight";
import { decodeHtmlEntities } from "../lib/text";
import type { CategoryMeta, ProblemDetail, ProblemSummary, StatsResponse } from "../types";

type SortBy = "category" | "passRateAsc" | "passRateDesc" | "lcNum";

const SIDEBAR_COLLAPSED_KEY = "algo-sidebar-collapsed";

function readSidebarCollapsed(): boolean {
  try {
    return localStorage.getItem(SIDEBAR_COLLAPSED_KEY) === "1";
  } catch {
    return false;
  }
}

function IconChevronLeft() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path
        d="M15 18l-6-6 6-6"
        stroke="currentColor"
        strokeWidth="2"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}

function IconClose() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M18 6L6 18M6 6l12 12" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
    </svg>
  );
}

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
  const [detailLoading, setDetailLoading] = useState(false);
  const [detailTab, setDetailTab] = useState<"insight" | "code">("insight");
  const detailCacheRef = useRef<Map<string, ProblemDetail>>(new Map());
  const detailRequestRef = useRef(0);
  const dialogRef = useRef<HTMLDialogElement>(null);
  const logoutDialogRef = useRef<HTMLDialogElement>(null);
  const [logoutConfirmOpen, setLogoutConfirmOpen] = useState(false);
  const [toast, setToast] = useState("");
  const [loading, setLoading] = useState(true);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [sidebarCollapsed, setSidebarCollapsed] = useState(readSidebarCollapsed);
  const isDrawerLayout = useMediaQuery(DRAWER_LAYOUT_MQ);

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

  useEffect(() => {
    (document.activeElement as HTMLElement | null)?.blur();
  }, []);

  useEffect(() => {
    document.body.style.overflow = sidebarOpen || selectedId || logoutConfirmOpen ? "hidden" : "";
    return () => {
      document.body.style.overflow = "";
    };
  }, [sidebarOpen, selectedId, logoutConfirmOpen]);

  useEffect(() => {
    if (!isDrawerLayout) setSidebarOpen(false);
  }, [isDrawerLayout]);

  useEffect(() => {
    const el = dialogRef.current;
    if (!el) return;
    if (selectedId) {
      if (!el.open) el.showModal();
    } else if (el.open) {
      el.close();
    }
  }, [selectedId]);

  useEffect(() => {
    const el = logoutDialogRef.current;
    if (!el) return;
    if (logoutConfirmOpen) {
      if (!el.open) el.showModal();
    } else if (el.open) {
      el.close();
    }
  }, [logoutConfirmOpen]);

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

  const openDetail = (id: string) => {
    setSelectedId(id);
    setDetailTab("insight");

    const cached = detailCacheRef.current.get(id);
    if (cached) {
      setDetail(cached);
      setDetailLoading(false);
      return;
    }

    setDetail(null);
    setDetailLoading(true);
    const reqId = ++detailRequestRef.current;
    problemsApi
      .detail(id)
      .then((d) => {
        if (reqId !== detailRequestRef.current) return;
        detailCacheRef.current.set(id, d);
        setDetail(d);
      })
      .finally(() => {
        if (reqId === detailRequestRef.current) setDetailLoading(false);
      });
  };

  const closeDetail = () => {
    detailRequestRef.current += 1;
    setSelectedId(null);
    setDetail(null);
    setDetailLoading(false);
  };

  const selectedSummary = useMemo(
    () => (selectedId ? problems.find((p) => p.id === selectedId) ?? null : null),
    [problems, selectedId],
  );

  const highlightedCode = useMemo(() => {
    if (!detail || detailTab !== "code") return "";
    return renderJavaCode(detail.solution_code || "// 暂无代码");
  }, [detail, detailTab]);

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

  const requestLogout = () => setLogoutConfirmOpen(true);

  const handleLogoutConfirm = () => {
    setLogoutConfirmOpen(false);
    logout();
  };

  const toggleSidebarCollapsed = () => {
    if (isDrawerLayout) {
      setSidebarOpen(false);
      return;
    }
    setSidebarCollapsed((prev) => {
      const next = !prev;
      try {
        localStorage.setItem(SIDEBAR_COLLAPSED_KEY, next ? "1" : "0");
      } catch {
        /* ignore */
      }
      return next;
    });
  };

  const activeFilterCount =
    difficulties.size + freqLevels.size + categoryFilter.size + (showTodoOnly ? 1 : 0) + (search.trim() ? 1 : 0);

  const clearFilters = () => {
    setSearch("");
    setDifficulties(new Set());
    setFreqLevels(new Set());
    setCategoryFilter(new Set());
    setShowTodoOnly(false);
  };

  const userLabel = user?.email || user?.display_name || "";
  const userInitial = userLabel ? userLabel.charAt(0).toUpperCase() : "?";
  const showSidebar = !isDrawerLayout || sidebarOpen;
  const showSidebarBackdrop = isDrawerLayout && sidebarOpen;
  const showMenuButton = isDrawerLayout || sidebarCollapsed;

  const onPageMenuClick = () => {
    if (isDrawerLayout) setSidebarOpen(true);
    else toggleSidebarCollapsed();
  };

  if (loading) {
    return (
      <div className="login-page">
        <div className="loading-state">加载题目…</div>
      </div>
    );
  }

  return (
    <div
      className={`app${sidebarOpen ? " sidebar-open" : ""}${!isDrawerLayout && !sidebarOpen && sidebarCollapsed ? " sidebar-collapsed" : ""}`}
    >
      {showSidebarBackdrop && (
        <button
          type="button"
          className="sidebar-backdrop"
          aria-label="关闭菜单"
          onClick={() => setSidebarOpen(false)}
        />
      )}

      {showSidebar && (
      <aside className={`sidebar${sidebarOpen ? " open" : ""}`}>
        <div className="sidebar-head">
          <div className="brand brand-with-logo">
            <AppLogo appId="algorithm" size={32} className="brand-logo" alt="算法助手" />
            <div className="brand-text">
              <h1>算法助手</h1>
            </div>
          </div>
          <div className="sidebar-head-actions">
            <button
              type="button"
              className="shell-icon-btn sidebar-collapse"
              aria-label="收起侧栏"
              title="收起侧栏"
              onClick={toggleSidebarCollapsed}
            >
              <IconChevronLeft />
            </button>
            <button
              type="button"
              className="shell-icon-btn sidebar-close"
              aria-label="关闭菜单"
              onClick={() => setSidebarOpen(false)}
            >
              <IconClose />
            </button>
          </div>
        </div>

        <div className="user-chip">
          <span className="user-avatar" aria-hidden="true">
            {userInitial}
          </span>
          <div className="user-chip-text">
            <span className="user-chip-label">账号</span>
            <span className="user-email">{userLabel}</span>
          </div>
          <button type="button" className="btn btn-sm ghost user-logout" onClick={requestLogout}>
            退出
          </button>
        </div>

        <div className="sidebar-body">
          <div className="sidebar-section">
            <div className="section-head">
              <label className="label" htmlFor="search">
                搜索
              </label>
              {activeFilterCount > 0 && (
                <button type="button" className="text-btn" onClick={clearFilters}>
                  清除筛选
                </button>
              )}
            </div>
            <div className="search-field">
              <input
                id="search"
                type="search"
                placeholder="题号 / 标题 / 公司 / 类名…"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
              {search && (
                <button
                  type="button"
                  className="search-clear"
                  aria-label="清空搜索"
                  onClick={() => setSearch("")}
                >
                  ×
                </button>
              )}
            </div>
          </div>

          <div className="sidebar-section">
            <span className="label">难度</span>
            <div className="chip-group">
              {(["easy", "medium", "hard"] as const).map((d) => (
                <button
                  key={d}
                  type="button"
                  className={`chip chip-${d} ${difficulties.has(d) ? "active" : ""}`}
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

          <div className="sidebar-section sidebar-section-grow">
            <span className="label">类别</span>
            <div className="category-list">
              {categories.map((c) => {
                const count = problems.filter((p) => p.category === c.key).length;
                return (
                  <button
                    key={c.key}
                    type="button"
                    className={`category-item ${categoryFilter.has(c.key) ? "active" : ""}`}
                    onClick={() => toggleSet(categoryFilter, c.key, setCategoryFilter)}
                  >
                    <span className="category-name">{c.label}</span>
                    <span className="category-count">{count}</span>
                  </button>
                );
              })}
            </div>
          </div>
        </div>

        <div className="sidebar-footer progress-box">
          <span className="label">学习进度</span>
          <div className="progress-bar">
            <div className="progress-fill" style={{ width: `${pct}%` }} />
          </div>
          <p className="progress-text">
            <strong>{doneCount}</strong>
            <span>
              {" "}
              / {total} 已完成 · {pct}%
            </span>
          </p>
          <div className="progress-actions">
            <button
              type="button"
              className={`btn btn-sm ghost ${showTodoOnly ? "active" : ""}`}
              onClick={() => setShowTodoOnly((v) => !v)}
            >
              仅看未完成
            </button>
            <button type="button" className="btn btn-sm ghost danger" onClick={resetProgress}>
              重置进度
            </button>
          </div>
        </div>
      </aside>
      )}

      <main className="main">
        <PageHeader
          filteredCount={filtered.length}
          activeFilterCount={activeFilterCount}
          doneCount={doneCount}
          total={total}
          sortBy={sortBy}
          onSortChange={setSortBy}
          onMenuClick={onPageMenuClick}
          menuLabel={isDrawerLayout ? "打开菜单" : "展开侧栏"}
          showMenuButton={showMenuButton}
        />

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
                <span className={`card-status ${progress[p.id] ? "done" : ""}`}>
                  {progress[p.id] ? "已完成" : "待学习"}
                </span>
                <span>{p.code_lines ? `${p.code_lines} 行代码` : decodeHtmlEntities(p.summary) || "查看详情"}</span>
              </div>
            </article>
          ))}
        </section>
        <div id="emptyState" className={`empty ${filtered.length ? "hidden" : ""}`}>
          没有匹配的题目，试试调整筛选条件。
        </div>
      </main>

      {selectedId && selectedSummary && (
        <dialog
          ref={dialogRef}
          className={`detail-dialog${detailTab === "code" ? " code-mode" : ""}`}
          onClose={closeDetail}
          onClick={(e) => {
            if (e.target === dialogRef.current) closeDetail();
          }}
        >
          <div className="dialog-header">
            <div className="dialog-header-main">
              <p className="detail-meta">
                LC {detail?.lc_num ?? selectedSummary.lc_num} · {detail?.category_label ?? selectedSummary.category_label}
              </p>
              <h2>{detail?.title ?? selectedSummary.title}</h2>
            </div>
            <button type="button" className="icon-btn" aria-label="关闭" onClick={closeDetail}>
              ×
            </button>
          </div>
          <div className="dialog-body">
            <div className="detail-tags">
              <span className={`badge ${selectedSummary.difficulty}`}>
                {detail?.difficulty_label ?? selectedSummary.difficulty_label}
              </span>
              <span className={`tag ${(detail?.freq_level ?? selectedSummary.freq_level) === "极高" ? "high" : ""}`}>
                {detail?.freq_level ?? selectedSummary.freq_level}
              </span>
            </div>
            <div className="detail-meta-line">
              <span className="detail-companies">
                {(detail?.companies ?? selectedSummary.companies) || "—"}
              </span>
              <span className="detail-pass-rate">通过率 {detail?.pass_rate ?? selectedSummary.pass_rate}%</span>
            </div>
            <div className="detail-tabs" role="tablist" aria-label="题目详情">
              <button
                type="button"
                role="tab"
                aria-selected={detailTab === "insight"}
                className={`detail-tab ${detailTab === "insight" ? "active" : ""}`}
                onClick={() => setDetailTab("insight")}
              >
                解题思路
              </button>
              <button
                type="button"
                role="tab"
                aria-selected={detailTab === "code"}
                className={`detail-tab ${detailTab === "code" ? "active" : ""}`}
                onClick={() => setDetailTab("code")}
                disabled={detailLoading}
              >
                Java 实现
              </button>
            </div>

            {detailLoading && !detail ? (
              <div className="detail-loading">
                <div className="detail-loading-line" />
                <div className="detail-loading-line short" />
                <div className="detail-loading-block" />
                <div className="detail-loading-block" />
              </div>
            ) : detail ? (
              <>
                <div id="panelInsight" className={`detail-panel detail-prose ${detailTab === "insight" ? "active" : ""}`}>
                  <section>
                    <h3>题目描述</h3>
                    <p>{decodeHtmlEntities(detail.description) || "—"}</p>
                    {detail.example && <p className="example">示例：{decodeHtmlEntities(detail.example)}</p>}
                  </section>
                  <section>
                    <h3>核心解法</h3>
                    <p>{decodeHtmlEntities(detail.approach) || "—"}</p>
                  </section>
                  <section>
                    <h3>注意点</h3>
                    <p>{decodeHtmlEntities(detail.notes) || "—"}</p>
                  </section>
                  <section>
                    <h3>疑难点</h3>
                    <p>{decodeHtmlEntities(detail.pitfalls) || "—"}</p>
                  </section>
                  <section className="detail-actions">
                    <button type="button" className="copy-chip" onClick={() => copyText(detail.fqn)}>
                      <span className="copy-chip-label">类名</span>
                      <code>{detail.class_name}</code>
                    </button>
                  </section>
                </div>

                {detailTab === "code" && (
                  <div id="panelCode" className="detail-panel active">
                    <div className="code-toolbar">
                      <div className="code-meta">
                        <span className="code-filename">{detail.class_name}.java</span>
                        <span className="code-lines">
                          {detail.code_lines || 0} 行 · {detail.fqn}
                        </span>
                      </div>
                      <button type="button" className="btn btn-sm" onClick={() => copyText(detail.solution_code)}>
                        复制代码
                      </button>
                    </div>
                    <div className="code-panel">
                      <div className="code-block" dangerouslySetInnerHTML={{ __html: highlightedCode }} />
                    </div>
                  </div>
                )}
              </>
            ) : null}
          </div>
          <div className="dialog-footer">
            <button
              type="button"
              className={`btn btn-block ${progress[selectedId] ? "" : "primary"}`}
              onClick={toggleDone}
              disabled={!detail || detailLoading}
            >
              {progress[selectedId] ? "取消完成标记" : "标记为已完成"}
            </button>
          </div>
        </dialog>
      )}

      <dialog
        ref={logoutDialogRef}
        className="confirm-dialog"
        onClose={() => setLogoutConfirmOpen(false)}
        onClick={(e) => {
          if (e.target === logoutDialogRef.current) setLogoutConfirmOpen(false);
        }}
      >
        <div className="confirm-body">
          <h3>退出登录</h3>
          <p>确定要退出当前账号吗？</p>
        </div>
        <div className="confirm-actions">
          <button type="button" className="btn ghost" onClick={() => setLogoutConfirmOpen(false)}>
            取消
          </button>
          <button type="button" className="btn primary" onClick={handleLogoutConfirm}>
            退出
          </button>
        </div>
      </dialog>

      <div className={`toast ${toast ? "show" : ""}`}>{toast}</div>
    </div>
  );
}
