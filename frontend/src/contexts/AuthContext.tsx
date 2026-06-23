import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useState,
  type ReactNode,
} from "react";
import { useNavigate } from "react-router-dom";

import { authApi } from "../lib/api";
import { clearAccessToken, getAccessToken, setAccessToken } from "../lib/auth";
import type { AuthUser } from "../types";

interface AuthContextValue {
  user: AuthUser | null;
  isLoading: boolean;
  isAuthenticated: boolean;
  loginWithEmail: (email: string, code: string) => Promise<void>;
  logout: () => void;
  refreshUser: () => Promise<void>;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const navigate = useNavigate();
  const [user, setUser] = useState<AuthUser | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const refreshUser = useCallback(async () => {
    const token = getAccessToken();
    if (!token) {
      setUser(null);
      return;
    }
    try {
      const me = await authApi.me();
      setUser(me);
    } catch {
      clearAccessToken();
      setUser(null);
    }
  }, []);

  const loginWithEmail = useCallback(async (email: string, code: string) => {
    const res = await authApi.emailLogin(email, code);
    setAccessToken(res.access_token);
    setUser(res.user);
  }, []);

  const logout = useCallback(() => {
    clearAccessToken();
    setUser(null);
    navigate("/login");
  }, [navigate]);

  useEffect(() => {
    refreshUser().finally(() => setIsLoading(false));
  }, [refreshUser]);

  useEffect(() => {
    const onUnauthorized = () => {
      clearAccessToken();
      setUser(null);
      const next = encodeURIComponent(window.location.pathname);
      navigate(`/login?next=${next}`);
    };
    window.addEventListener("auth:unauthorized", onUnauthorized);
    return () => window.removeEventListener("auth:unauthorized", onUnauthorized);
  }, [navigate]);

  const value = useMemo(
    () => ({
      user,
      isLoading,
      isAuthenticated: !!user,
      loginWithEmail,
      logout,
      refreshUser,
    }),
    [user, isLoading, loginWithEmail, logout, refreshUser],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error("useAuth must be used within AuthProvider");
  return ctx;
}
