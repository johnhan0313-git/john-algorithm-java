/** Tailwind `lg` — tablet/desktop layout from here up */
export const DESKTOP_MIN_WIDTH = 1024;

/** Mouse/trackpad desktop: wide viewport and fine pointer */
export const DESKTOP_LAYOUT_MQ = `(min-width: ${DESKTOP_MIN_WIDTH}px) and (hover: hover) and (pointer: fine)`;
/** Phone/tablet drawer: narrow viewport or touch-primary device */
export const DRAWER_LAYOUT_MQ = `(max-width: ${DESKTOP_MIN_WIDTH - 1}px), (hover: none) and (pointer: coarse)`;
