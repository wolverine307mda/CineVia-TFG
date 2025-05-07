import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
    plugins: [vue()],
    resolve: {
        alias: {
            '@': path.resolve(__dirname, './src'),
        },
    },
    server: {
        proxy: {
            '/api': {
                target: 'http://localhost:8080/api',
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
                headers: {
                    'X-Forwarded-Proto': 'https',
                    'X-Forwarded-Host': 'localhost',
                },
            },
        },
    },
})