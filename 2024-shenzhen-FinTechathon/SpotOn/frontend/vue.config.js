const { defineConfig } = require('@vue/cli-service');
const { DefinePlugin } = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
module.exports = defineConfig({
  transpileDependencies: true,

  configureWebpack: {
    plugins: [
      new DefinePlugin({
        __VUE_PROD_DEVTOOLS__: JSON.stringify(false),  // 禁用生产环境中的 devtools
        __VUE_OPTIONS_API__: JSON.stringify(false),     // 启用 Options API（如果你使用它）
        __VUE_PROD_HYDRATION_MISMATCH_DETAILS__: JSON.stringify(false)  // 解决 Hydration Mismatch 报错
      })
    ],
    module: {
      rules: [
        {
          test: /\.worker\.js$/,
          use: { loader: 'worker-loader' },
        },
      ],
    },
  }
});
module.exports = {
  chainWebpack: config => {
    config.plugin('html').tap(args => {
      args[0].title = '影映'; // 设置你想要的标题
      return args;
    });
  }
};