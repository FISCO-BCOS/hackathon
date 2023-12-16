import vue from '@vitejs/plugin-vue'

export default {
    base: './',
    plugins: [vue()],
    optimizeDeps: {
        include: ['schart.js']
    },
    resolve: {
        alias: {
          '@': '/src'
        }
    },
    server: {
        port: 8081,
        https: false,
        hmr: { overlay: false },
        proxy: {
        '/api': {
            // target: 'http://localhost:2333',
            target: 'https://console-mock.apipost.cn/mock/d89b27af-3909-44f6-a929-18a4c7aab5a4/',
            changeOrigin: true,
            rewrite: path => path.replace(/^\/api/, '')
            }
        }
    }
}