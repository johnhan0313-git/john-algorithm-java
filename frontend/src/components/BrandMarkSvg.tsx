import { useId } from "react";

type BrandMarkSvgProps = {
  gradientId?: string;
};

export function BrandMarkSvg({ gradientId: gradientIdProp }: BrandMarkSvgProps) {
  const autoId = useId();
  const gradientId = gradientIdProp ?? autoId;

  return (
    <>
      <defs>
        <linearGradient id={gradientId} x1="6" y1="4" x2="28" y2="28" gradientUnits="userSpaceOnUse">
          <stop stopColor="#ffffff" />
          <stop offset="1" stopColor="#eef2ff" />
        </linearGradient>
      </defs>
      <rect x="0.5" y="0.5" width="31" height="31" rx="9" fill={`url(#${gradientId})`} />
      <rect x="0.5" y="0.5" width="31" height="31" rx="9" stroke="#dbeafe" strokeWidth="1" />
      <path
        stroke="#4338ca"
        strokeWidth="2.4"
        strokeLinecap="round"
        d="M8.5 11.5h15M11.5 11.5v12M20.5 11.5v12"
      />
    </>
  );
}
