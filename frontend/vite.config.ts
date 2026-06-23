import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [
    react(),
    {
      name: "favicon-ico-fallback",
      configureServer(server) {
        server.middlewares.use((req, _res, next) => {
          if (req.url === "/favicon.ico" || req.url?.startsWith("/favicon.ico?")) {
            req.url = "/favicon.svg?v=3";
          }
          next();
        });
      },
    },
  ],
  server: {
    port: 3004,
    proxy: {
      "/api": {
        target: "http://127.0.0.1:8004",
        changeOrigin: true,
      },
    },
  },
});
