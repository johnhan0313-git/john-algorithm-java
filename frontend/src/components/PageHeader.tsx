import { AppLogo } from "@johnhan0313-git/shared/brand";
import { AppsLauncher } from "@johnhan0313-git/shared/nav";
import "@johnhan0313-git/shared/nav.css";

type SortBy = "category" | "passRateAsc" | "passRateDesc" | "lcNum";

type PageHeaderProps = {
  filteredCount: number;
  activeFilterCount: number;
  doneCount: number;
  total: number;
  sortBy: SortBy;
  onSortChange: (value: SortBy) => void;
  onMenuClick: () => void;
  menuLabel: string;
  showMenuButton: boolean;
};

function IconMenu() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" aria-hidden="true">
      <path d="M4 7h16M4 12h16M4 17h16" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
    </svg>
  );
}

export default function PageHeader({
  filteredCount,
  activeFilterCount,
  doneCount,
  total,
  sortBy,
  onSortChange,
  onMenuClick,
  menuLabel,
  showMenuButton,
}: PageHeaderProps) {
  const summary =
    activeFilterCount > 0 ? `已筛选 ${filteredCount} 题` : `共 ${filteredCount} 题`;

  return (
    <header className="page-header">
      {showMenuButton ? (
        <button
          type="button"
          className="shell-icon-btn page-header-menu"
          aria-label={menuLabel}
          onClick={onMenuClick}
        >
          <IconMenu />
          {activeFilterCount > 0 && <span className="filter-badge">{activeFilterCount}</span>}
        </button>
      ) : (
        <div className="page-header-menu-placeholder" aria-hidden="true" />
      )}

      <div className="page-header-brand">
        <AppLogo appId="algorithm" size={28} className="brand-logo brand-logo-sm" alt="算法助手" />
        <strong className="page-header-app-name">算法助手</strong>
      </div>

      <div className="page-header-spacer" aria-hidden="true" />

      <div className="page-header-intro">
        <h2 className="page-title">题目列表</h2>
        <p className="page-subtitle">
          {summary}
          {activeFilterCount > 0 && " · 已筛选"}
          <span className="toolbar-progress-hint">
            {" "}
            · 已完成 {doneCount}/{total}
          </span>
        </p>
      </div>

      <p className="page-header-summary">
        {summary}
        <span className="toolbar-progress-hint">
          {" "}
          · 已完成 {doneCount}/{total}
        </span>
      </p>

      <div className="page-header-actions">
        <AppsLauncher current="algorithm" />
        <select
          id="sortBy"
          value={sortBy}
          aria-label="排序方式"
          onChange={(e) => onSortChange(e.target.value as SortBy)}
        >
          <option value="category">按类别</option>
          <option value="passRateAsc">通过率 ↑</option>
          <option value="passRateDesc">通过率 ↓</option>
          <option value="lcNum">按题号</option>
        </select>
      </div>
    </header>
  );
}
