export function escapeHtml(str: string): string {
  return String(str)
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;");
}

export function highlightJavaLine(line: string): string {
  const tokens: string[] = [];
  const re =
    /("(?:\\.|[^"\\])*")|(\/\/.*$)|(\b\d+(?:\.\d+)?\b)|(\b(?:public|private|protected|static|final|class|interface|extends|implements|return|if|else|for|while|do|switch|case|break|continue|new|this|super|void|int|long|double|float|boolean|char|byte|short|null|true|false|throw|try|catch|enum)\b)|(\b(?:String|Integer|List|Map|Set|Deque|PriorityQueue|ArrayList|HashMap|Arrays|Math|Collections)\b)/g;
  let last = 0;
  let m: RegExpExecArray | null;
  while ((m = re.exec(line)) !== null) {
    if (m.index > last) tokens.push(escapeHtml(line.slice(last, m.index)));
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
  if (last < line.length) tokens.push(escapeHtml(line.slice(last)));
  return tokens.join("");
}

export function renderJavaCode(code: string): string {
  return String(code)
    .split("\n")
    .map((line, index) => {
      const content = line.length === 0 ? " " : highlightJavaLine(line);
      return `<div class="code-row"><span class="code-gutter">${index + 1}</span><span class="code-content">${content}</span></div>`;
    })
    .join("");
}

export function shortCompanies(text: string): string {
  if (!text) return "—";
  return text.length > 18 ? `${text.slice(0, 18)}…` : text;
}
