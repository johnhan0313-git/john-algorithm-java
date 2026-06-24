/** JavaDoc 中 &gt; / &lt; 等实体在纯文本展示时需解码 */
export function decodeHtmlEntities(str: string): string {
  return String(str)
    .replace(/&amp;/g, "&")
    .replace(/&lt;/g, "<")
    .replace(/&gt;/g, ">")
    .replace(/&quot;/g, '"')
    .replace(/&#39;/g, "'");
}
