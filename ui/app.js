(function () {
  const STORAGE_KEY = "algo-progress-v1";

  const state = {
    search: "",
    difficulties: new Set(),
    freqLevels: new Set(),
    categories: new Set(),
    sortBy: "category",
    showTodoOnly: false,
    selectedId: null,
    progress: loadProgress(),
  };

  const data = window.ALGO_DATA;
  if (!data) {
    document.body.innerHTML = "<p style='padding:24px'>未找到 data.js，请先运行：python3 scripts/generate-ui-data.py</p>";
    return;
  }

  const els = {
    search: document.getElementById("search"),
    difficultyFilters: document.getElementById("difficultyFilters"),
    freqFilters: document.getElementById("freqFilters"),
    categoryFilters: document.getElementById("categoryFilters"),
    statsCards: document.getElementById("statsCards"),
    sortBy: document.getElementById("sortBy"),
    problemGrid: document.getElementById("problemGrid"),
    emptyState: document.getElementById("emptyState"),
    progressFill: document.getElementById("progressFill"),
    progressText: document.getElementById("progressText"),
    showTodoBtn: document.getElementById("showTodoBtn"),
    resetProgressBtn: document.getElementById("resetProgressBtn"),
    refreshHintBtn: document.getElementById("refreshHintBtn"),
    detailDialog: document.getElementById("detailDialog"),
    closeDialogBtn: document.getElementById("closeDialogBtn"),
    toggleDoneBtn: document.getElementById("toggleDoneBtn"),
    toast: document.getElementById("toast"),
  };

  init();

  function init() {
    renderStats();
    renderDifficultyFilters();
    renderFreqFilters();
    renderCategoryFilters();
    bindEvents();
    render();
  }

  function bindEvents() {
    els.search.addEventListener("input", (e) => {
      state.search = e.target.value.trim().toLowerCase();
      render();
    });

    els.sortBy.addEventListener("change", (e) => {
      state.sortBy = e.target.value;
      render();
    });

    els.showTodoBtn.addEventListener("click", () => {
      state.showTodoOnly = !state.showTodoOnly;
      els.showTodoBtn.classList.toggle("active", state.showTodoOnly);
      render();
    });

    els.resetProgressBtn.addEventListener("click", () => {
      if (confirm("确定重置所有学习进度？")) {
        state.progress = {};
        saveProgress();
        render();
      }
    });

    els.refreshHintBtn.addEventListener("click", () => {
      toast("请在项目根目录运行：python3 scripts/generate-ui-data.py");
    });

    els.closeDialogBtn.addEventListener("click", closeDetailDialog);
    els.toggleDoneBtn.addEventListener("click", toggleSelectedDone);

    els.detailDialog.addEventListener("click", (e) => {
      const rect = els.detailDialog.getBoundingClientRect();
      const inside =
        e.clientX >= rect.left &&
        e.clientX <= rect.right &&
        e.clientY >= rect.top &&
        e.clientY <= rect.bottom;
      if (!inside) {
        closeDetailDialog();
        return;
      }

      const btn = e.target.closest("[data-copy]");
      if (!btn) return;
      const type = btn.dataset.copy;
      const p = currentProblem();
      if (!p) return;
      const map = {
        fqn: p.fqn,
        path: p.ideaPath,
        run: p.runCommand,
      };
      copyText(map[type]);
    });

    document.querySelectorAll(".detail-tab").forEach((tab) => {
      tab.addEventListener("click", () => switchDetailTab(tab.dataset.tab));
    });

    document.getElementById("copyCodeBtn").addEventListener("click", () => {
      const p = currentProblem();
      if (p && p.solutionCode) copyText(p.solutionCode);
    });
  }

  function closeDetailDialog() {
    els.detailDialog.close();
    els.detailDialog.classList.remove("code-mode");
  }

  function switchDetailTab(tabName) {
    document.querySelectorAll(".detail-tab").forEach((tab) => {
      tab.classList.toggle("active", tab.dataset.tab === tabName);
    });
    document.getElementById("panelInsight").classList.toggle("active", tabName === "insight");
    document.getElementById("panelCode").classList.toggle("active", tabName === "code");
    els.detailDialog.classList.toggle("code-mode", tabName === "code");
  }

  function renderStats() {
    const s = data.stats;
    els.statsCards.innerHTML = [
      statCard(s.total, "题目总数"),
      statCard(s.highFreq, "极高频"),
      statCard(s.byDifficulty.hard || 0, "Hard"),
      statCard(Object.keys(s.byCategory).length, "解法类别"),
    ].join("");
  }

  function statCard(value, label) {
    return `<div class="stat-card"><strong>${value}</strong><span>${label}</span></div>`;
  }

  function renderDifficultyFilters() {
    ["easy", "medium", "hard"].forEach((d) => {
      const btn = chip(`${labelDifficulty(d)}`, () => toggleSet(state.difficulties, d));
      btn.dataset.value = d;
      els.difficultyFilters.appendChild(btn);
    });
  }

  function renderFreqFilters() {
    ["极高", "高", "中"].forEach((f) => {
      const btn = chip(f, () => toggleSet(state.freqLevels, f));
      btn.dataset.value = f;
      els.freqFilters.appendChild(btn);
    });
  }

  function renderCategoryFilters() {
    data.categories.forEach((c) => {
      const count = data.problems.filter((p) => p.category === c.key).length;
      const btn = document.createElement("button");
      btn.className = "category-item";
      btn.dataset.value = c.key;
      btn.innerHTML = `<span>${c.label}</span><span>${count}</span>`;
      btn.addEventListener("click", () => {
        toggleSet(state.categories, c.key);
        btn.classList.toggle("active", state.categories.has(c.key));
        render();
      });
      els.categoryFilters.appendChild(btn);
    });
  }

  function chip(text, onClick) {
    const btn = document.createElement("button");
    btn.className = "chip";
    btn.textContent = text;
    btn.addEventListener("click", () => {
      onClick();
      btn.classList.toggle("active");
      render();
    });
    return btn;
  }

  function toggleSet(set, value) {
    if (set.has(value)) set.delete(value);
    else set.add(value);
  }

  function filteredProblems() {
    return data.problems.filter((p) => {
      if (state.difficulties.size && !state.difficulties.has(p.difficulty)) return false;
      if (state.freqLevels.size && !state.freqLevels.has(p.freqLevel)) return false;
      if (state.categories.size && !state.categories.has(p.category)) return false;
      if (state.showTodoOnly && state.progress[p.id]) return false;
      if (state.search) {
        const hay = [
          p.lcNum, p.title, p.fullTitle, p.companies, p.fqn,
          p.categoryLabel, p.className, p.description, p.approach,
        ].join(" ").toLowerCase();
        if (!hay.includes(state.search)) return false;
      }
      return true;
    }).sort(sortFn);
  }

  function sortFn(a, b) {
    switch (state.sortBy) {
      case "passRateAsc": return a.passRate - b.passRate;
      case "passRateDesc": return b.passRate - a.passRate;
      case "lcNum": return parseInt(a.lcNum, 10) - parseInt(b.lcNum, 10);
      default:
        if (a.categoryLabel !== b.categoryLabel) return a.categoryLabel.localeCompare(b.categoryLabel, "zh");
        const order = { easy: 0, medium: 1, hard: 2 };
        return order[a.difficulty] - order[b.difficulty] || a.title.localeCompare(b.title, "zh");
    }
  }

  function render() {
    const list = filteredProblems();
    els.problemGrid.innerHTML = list.map(renderCard).join("");
    els.emptyState.classList.toggle("hidden", list.length > 0);
    updateProgress();

    els.problemGrid.querySelectorAll(".card").forEach((card) => {
      card.addEventListener("click", () => openDetail(card.dataset.id));
    });
  }

  function renderCard(p) {
    const done = !!state.progress[p.id];
    return `
      <article class="card ${done ? "done" : ""}" data-id="${p.id}">
        <div class="card-head">
          <h3>LC${p.lcNum} ${escapeHtml(p.title)}</h3>
          <span class="badge ${p.difficulty}">${p.difficultyLabel}</span>
        </div>
        <div class="card-meta">${escapeHtml(p.categoryLabel)} · 通过率 ${p.passRate}%</div>
        <div class="card-tags">
          <span class="tag ${p.freqLevel === "极高" ? "high" : ""}">${escapeHtml(p.freqLevel)}</span>
          <span class="tag">${escapeHtml(shortCompanies(p.companies))}</span>
        </div>
        <div class="card-foot">
          <span>${done ? "✓ 已完成" : "思路 + Java 实现"}</span>
          <span>${p.codeLines ? p.codeLines + " 行" : escapeHtml(p.summary || "打开详情")}</span>
        </div>
      </article>`;
  }

  function openDetail(id) {
    state.selectedId = id;
    const p = currentProblem();
    if (!p) return;

    document.getElementById("detailLc").textContent = `LeetCode ${p.lcNum} · ${p.categoryLabel} · ${p.difficultyLabel}`;
    document.getElementById("detailTitle").textContent = p.title;
    document.getElementById("detailDesc").textContent = decodeHtmlEntities(p.description) || "—";
    document.getElementById("detailExample").textContent = p.example ? `示例：${decodeHtmlEntities(p.example)}` : "";
    document.getElementById("detailApproach").textContent = decodeHtmlEntities(p.approach) || "—";
    document.getElementById("detailNotes").textContent = decodeHtmlEntities(p.notes) || "—";
    document.getElementById("detailPitfalls").textContent = decodeHtmlEntities(p.pitfalls) || "—";
    document.getElementById("detailFqn").textContent = p.fqn;
    document.getElementById("detailPath").textContent = p.ideaPath;
    document.getElementById("detailRun").textContent = p.runCommand;

    document.getElementById("detailTags").innerHTML = [
      tag(p.freqLevel, p.freqLevel === "极高"),
      tag(`通过率 ${p.passRate}%`),
      tag(p.companies),
    ].join("");

    document.getElementById("codeFileName").textContent = `${p.className}.java`;
    document.getElementById("codeLineCount").textContent = `${p.codeLines || 0} 行 · ${p.fqn}`;
    document.getElementById("detailCode").innerHTML = renderJavaCode(p.solutionCode || "// 暂无代码");

    switchDetailTab("insight");

    const done = !!state.progress[p.id];
    els.toggleDoneBtn.textContent = done ? "取消完成标记" : "标记为已完成";
    els.toggleDoneBtn.classList.toggle("primary", !done);

    els.detailDialog.showModal();
  }

  function tag(text, high) {
    return `<span class="tag ${high ? "high" : ""}">${escapeHtml(text)}</span>`;
  }

  function toggleSelectedDone() {
    const p = currentProblem();
    if (!p) return;
    if (state.progress[p.id]) delete state.progress[p.id];
    else state.progress[p.id] = Date.now();
    saveProgress();
    render();
    openDetail(p.id);
  }

  function currentProblem() {
    return data.problems.find((p) => p.id === state.selectedId);
  }

  function updateProgress() {
    const doneCount = Object.keys(state.progress).length;
    const total = data.stats.total;
    const pct = total ? Math.round((doneCount / total) * 100) : 0;
    els.progressFill.style.width = `${pct}%`;
    els.progressText.textContent = `${doneCount} / ${total} 已完成（${pct}%）`;
  }

  function labelDifficulty(d) {
    return { easy: "Easy", medium: "Medium", hard: "Hard" }[d] || d;
  }

  function shortCompanies(text) {
    if (!text) return "—";
    return text.length > 18 ? `${text.slice(0, 18)}…` : text;
  }

  function decodeHtmlEntities(str) {
    return String(str)
      .replace(/&amp;/g, "&")
      .replace(/&lt;/g, "<")
      .replace(/&gt;/g, ">")
      .replace(/&quot;/g, '"')
      .replace(/&#39;/g, "'");
  }

  function escapeHtml(str) {
    return String(str)
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;");
  }

  function renderJavaCode(code) {
    const lines = String(code).split("\n");
    return lines
      .map((line, index) => {
        const content = line.length === 0 ? " " : highlightJavaLine(line);
        return `<div class="code-row"><span class="code-gutter">${index + 1}</span><span class="code-content">${content}</span></div>`;
      })
      .join("");
  }

  function highlightJavaLine(line) {
    const tokens = [];
    const re = /("(?:\\.|[^"\\])*")|(\/\/.*$)|(\b\d+(?:\.\d+)?\b)|(\b(?:public|private|protected|static|final|class|interface|extends|implements|return|if|else|for|while|do|switch|case|break|continue|new|this|super|void|int|long|double|float|boolean|char|byte|short|null|true|false|throw|try|catch|enum)\b)|(\b(?:String|Integer|List|Map|Set|Deque|PriorityQueue|ArrayList|HashMap|Arrays|Math|Collections)\b)/g;
    let last = 0;
    let m;
    while ((m = re.exec(line)) !== null) {
      if (m.index > last) {
        tokens.push(escapeHtml(line.slice(last, m.index)));
      }
      const value = m[0];
      let cls = "";
      if (m[1]) cls = "hl-str";
      else if (m[2]) cls = "hl-cm";
      else if (m[3]) cls = "hl-num";
      else if (m[4]) cls = "hl-kw";
      else if (m[5]) cls = "hl-ty";
      tokens.push(`<span class="${cls}">${escapeHtml(value)}</span>`);
      last = m.index + value.length;
    }
    if (last < line.length) {
      tokens.push(escapeHtml(line.slice(last)));
    }
    return tokens.join("");
  }

  function copyText(text) {
    navigator.clipboard.writeText(text).then(() => toast("已复制到剪贴板"));
  }

  function toast(msg) {
    els.toast.textContent = msg;
    els.toast.classList.add("show");
    setTimeout(() => els.toast.classList.remove("show"), 2200);
  }

  function loadProgress() {
    try {
      return JSON.parse(localStorage.getItem(STORAGE_KEY) || "{}");
    } catch (e) {
      return {};
    }
  }

  function saveProgress() {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(state.progress));
  }
})();
