import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";

import { RequireAuth } from "./components/RequireAuth";
import { AuthProvider } from "./contexts/AuthContext";
import BoardPage from "./pages/Board";
import LoginPage from "./pages/Login";

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route
            path="/"
            element={
              <RequireAuth>
                <BoardPage />
              </RequireAuth>
            }
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}
