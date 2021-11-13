module.exports = {
  devServer: {
    proxy: {
      '/sign': {
        target: process.env.VUE_APP_SIGN,
        changeOrigin: true,
        pathRewrite: {
          '^/sign': '/user'
        }
      },
      '/trans': {
        target: process.env.VUE_APP_FRONT,
        changeOrigin: true,
        pathRewrite: {
          // '^/trans': '/trans/handle'
          '^/trans': '/trans/handleWithSign'
        }
      }
    }
  },
  transpileDependencies: [
    'vuetify'
  ]
}
