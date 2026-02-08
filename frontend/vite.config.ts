import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Determine proxy target based on environment
// For Docker: http://backend:8080
// For local development: http://localhost:8080
const getProxyTarget = () => {
  // In Docker, the backend service is reachable via http://backend:8080
  // Locally, use http://localhost:8080
  if (process.env.VITE_API_PROXY_TARGET) {
    return process.env.VITE_API_PROXY_TARGET;
  }
  // Default: assume local development
  return 'http://localhost:8080';
};

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: getProxyTarget(),
        changeOrigin: true,
        secure: false,
      },
    },
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
      'Access-Control-Allow-Headers': '*',
    },
  },
  resolve: {
    alias: {
      '@': '/src',
    },
  },
});
