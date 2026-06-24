import { FormEvent, useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

import { useAuth } from "../contexts/AuthContext";
import { authApi, parseApiError } from "../lib/api";

export default function LoginPage() {
  const { loginWithEmail, isAuthenticated } = useAuth();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const next = searchParams.get("next") || "/";

  const [email, setEmail] = useState("");
  const [emailCode, setEmailCode] = useState("");
  const [codeSent, setCodeSent] = useState(false);
  const [cooldown, setCooldown] = useState(0);
  const [error, setError] = useState("");
  const [sendingCode, setSendingCode] = useState(false);
  const [loggingIn, setLoggingIn] = useState(false);

  useEffect(() => {
    if (isAuthenticated) navigate(next);
  }, [isAuthenticated, navigate, next]);

  useEffect(() => {
    if (cooldown <= 0) return;
    const timer = window.setInterval(() => {
      setCooldown((v) => (v > 0 ? v - 1 : 0));
    }, 1000);
    return () => window.clearInterval(timer);
  }, [cooldown]);

  const onSendCode = async () => {
    setError("");
    if (!email.trim()) {
      setError("请输入邮箱");
      return;
    }
    setSendingCode(true);
    try {
      const result = await authApi.sendEmailCode(email.trim());
      setCooldown(result.cooldown_seconds || 60);
      if (result.dev_code) setEmailCode(result.dev_code);
      setCodeSent(true);
    } catch (err) {
      setError(parseApiError(err, "发送验证码失败"));
    } finally {
      setSendingCode(false);
    }
  };

  const onSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError("");
    if (!email.trim()) {
      setError("请输入邮箱");
      return;
    }
    if (!emailCode.trim()) {
      setError("请输入邮箱验证码");
      return;
    }
    setLoggingIn(true);
    try {
      await loginWithEmail(email.trim(), emailCode.trim());
      (document.activeElement as HTMLElement | null)?.blur();
      navigate(next);
    } catch (err) {
      setError(parseApiError(err, "登录失败"));
    } finally {
      setLoggingIn(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <h1>算法助手</h1>
        <p className="login-desc">使用邮箱验证码登录，首次登录将自动注册</p>
        <form onSubmit={onSubmit} className="login-form">
          <label>
            邮箱
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              autoComplete="email"
              placeholder="you@example.com"
            />
          </label>

          {codeSent && <p className="login-hint">验证码已发送，请查收邮件</p>}

          <label>
            邮箱验证码
            <div className="login-code-row">
              <input
                value={emailCode}
                onChange={(e) => setEmailCode(e.target.value)}
                required
                autoComplete="one-time-code"
                placeholder="6 位数字"
              />
              <button
                type="button"
                className="btn"
                disabled={sendingCode || cooldown > 0}
                onClick={onSendCode}
              >
                {cooldown > 0 ? `${cooldown}s` : sendingCode ? "发送中..." : codeSent ? "重新发送" : "获取验证码"}
              </button>
            </div>
          </label>

          {error && <p className="login-error">{error}</p>}

          <button type="submit" className="btn primary login-submit" disabled={loggingIn}>
            {loggingIn ? "登录中..." : "登录 / 注册"}
          </button>
        </form>
      </div>
    </div>
  );
}
