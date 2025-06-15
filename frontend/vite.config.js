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
        host: true,
        port: 5173,
        proxy: {
            '/api': {
                target: `${process.env.VITE_API_URL}/api`,
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
                headers: {
                    'X-Forwarded-Proto': 'https',
                    'X-Forwarded-Host': 'localhost',
                },
            },
        },
    }
})