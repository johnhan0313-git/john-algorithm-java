import { BrandMarkSvg } from "./BrandMarkSvg";

type BrandLogoProps = {
  size?: "sm" | "md";
};

export default function BrandLogo({ size = "md" }: BrandLogoProps) {
  return (
    <span className={`brand-logo brand-logo-${size}`} aria-label="算法助手">
      <svg viewBox="0 0 32 32" fill="none" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
        <BrandMarkSvg />
      </svg>
    </span>
  );
}
