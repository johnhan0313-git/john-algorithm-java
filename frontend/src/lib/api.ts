import { clearAccessToken, getAccessToken } from "./auth";
import type {
  AuthResponse,
  AuthUser,
  ProblemDetail,
  ProblemsListResponse,
  ProgressMapResponse,
  StatsResponse,
} from "../types";

export class ApiError extends Error {
  status: number;

  constructor(status: number, message: string) {
    super(message);
    this.status = status;
  }
}

async function request<T>(path: string, init: RequestInit = {}): Promise<T> {
  const headers = new Headers(init.headers);
  if (!headers.has("Content-Type") && init.body) {
    headers.set("Content-Type", "application/json");
  }
  const token = getAccessToken();
  if (token) {
    headers.set("Authorization", `Bearer ${token}`);
  }

  const resp = await fetch(path, { ...init, headers });
  if (resp.status === 401) {
    clearAccessToken();
    window.dispatchEvent(new Event("auth:unauthorized"));
  }
  if (!resp.ok) {
    let detail = resp.statusText;
    try {
      const data = await resp.json();
      detail = data.detail || detail;
    } catch {
      /* ignore */
    }
    throw new ApiError(resp.status, String(detail));
  }
  if (resp.status === 204) {
    return undefined as T;
  }
  return resp.json() as Promise<T>;
}

export const authApi = {
  sendEmailCode: (email: string) =>
    request<{ cooldown_seconds: number; dev_code?: string | null }>("/api/auth/email/send-code", {
      method: "POST",
      body: JSON.stringify({ email }),
    }),

  emailLogin: (email: string, code: string) =>
    request<AuthResponse>("/api/auth/email/login", {
      method: "POST",
      body: JSON.stringify({ email, code }),
    }),

  me: () => request<AuthUser>("/api/auth/me"),
};

export const problemsApi = {
  list: (params: Record<string, string>) => {
    const qs = new URLSearchParams(params).toString();
    return request<ProblemsListResponse>(`/api/problems${qs ? `?${qs}` : ""}`);
  },

  detail: (slug: string) => request<ProblemDetail>(`/api/problems/${slug}`),

  stats: () => request<StatsResponse>("/api/problems/stats/summary"),
};

export const progressApi = {
  getAll: () => request<ProgressMapResponse>("/api/progress"),

  upsert: (slug: string, status: "done" | "todo", notes = "") =>
    request<{ slug: string; status: string }>(`/api/progress/${slug}`, {
      method: "PUT",
      body: JSON.stringify({ status, notes }),
    }),
};

export function parseApiError(err: unknown, fallback: string): string {
  if (err instanceof ApiError) return err.message;
  if (err instanceof Error) return err.message;
  return fallback;
}
