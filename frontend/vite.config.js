import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import path from 'path';  // Agrega esta línea para importar path

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // URL del backend
        changeOrigin: true,
        secure: false
      }
    }
  },
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src') // Esto define '@' como el directorio 'src'
    }
  }
});
