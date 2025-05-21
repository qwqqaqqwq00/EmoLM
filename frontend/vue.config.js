const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 80,
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // Backend server address
        changeOrigin: true, // Support cross-origin
        pathRewrite: { '^/api': '/api' }, // Keep /api prefix
      },
    },
  }
})
