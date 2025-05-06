const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 80,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端服务器地址
        changeOrigin: true, // 支持跨域
        pathRewrite: { '^/api': '/api' }, // 保留 /api 前缀
      },
    },
  }
})
