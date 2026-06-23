import { useId } from "react";

type BrandLogoProps = {
  size?: "sm" | "md";
};

export default function BrandLogo({ size = "md" }: BrandLogoProps) {
  const bgGradientId = useId();

  return (
    <span className={`brand-logo brand-logo-${size}`} aria-label="算法助手">
      <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
        <defs>
          <linearGradient id={bgGradientId} x1="6" y1="4" x2="28" y2="28" gradientUnits="userSpaceOnUse">
            <stop stopColor="#ffffff" />
            <stop offset="1" stopColor="#eef2ff" />
          </linearGradient>
        </defs>
        <rect x="0.5" y="0.5" width="31" height="31" rx="9" fill={`url(#${bgGradientId})`} />
        <rect x="0.5" y="0.5" width="31" height="31" rx="9" stroke="#dbeafe" strokeWidth="1" />
        <text x="16" y="22" textAnchor="middle" className="brand-logo-pi">
          π
        </text>
      </svg>
    </span>
  );
}
