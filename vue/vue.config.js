const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8080, // 前端端口
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 后端接口地址
        changeOrigin: true,
        // pathRewrite: {
        //   '^/api': ''
        // }
      }
    }
  },
  // === 新增下面这一段来解决 stompjs 的 net 报错 ===
  configureWebpack: {
    resolve: {
      fallback: {
        net: false,
        tls: false
      }
    }
  }
})
