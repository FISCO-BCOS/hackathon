// Ant Design of Vue 项目打包优化
// https://juejin.cn/post/6844904045937704968

const path = require('path');
const webpack = require('webpack');
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer');

function resolve(dir) {
  return path.join(__dirname, dir);
}

const isProd = process.env.NODE_ENV === 'production';

const assetsCDN = {
  // webpack build externals
  externals: {
    vue: 'Vue',
    'vue-router': 'VueRouter',
    vuex: 'Vuex',
    // axios: 'axios',
    moment: 'moment',
  },
  css: [],
  js: [
    '//cdnjs.cloudflare.com/ajax/libs/vue/2.6.11/vue.min.js',
    '//cdnjs.cloudflare.com/ajax/libs/vue-router/3.2.0/vue-router.min.js',
    '//cdnjs.cloudflare.com/ajax/libs/vuex/3.1.1/vuex.min.js',
    // '//cdnjs.cloudflare.com/ajax/libs/axios/0.21.1/axios.min.js',
    '//cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js',
  ],
};

const vueConfig = {
  publicPath: './',
  // Vue-Cli webpack 相关
  // https://cli.vuejs.org/zh/guide/webpack.html
  configureWebpack: {
    plugins: [
      // Ignore all locale files of moment.js
      new webpack.IgnorePlugin(/^\.\/locale$/, /moment$/),
      new BundleAnalyzerPlugin(),
    ],
    // if prod, add externals
    externals: isProd ? assetsCDN.externals : {},
    // vue-cli脚手架build目录中的webpack.base.conf.js配置文件
    // https://www.cnblogs.com/shy1766IT/p/11094274.html
    // configureWebpack配置alias别名的两种方式
    // https://segmentfault.com/a/1190000039772292
    resolve: {
      alias: {
        '@ant-design/icons/lib/dist$': resolve('./src/utils/icons.js'),
      },
    },
  },
  chainWebpack: (config) => {
    // if prod is on
    // assets require on cdn
    if (isProd) {
      config.plugin('html').tap((args) => {
        args[0].cdn = assetsCDN;
        return args;
      });
    }
  },
  productionSourceMap: false,
  lintOnSave: false, // 关闭eslint检查
  devServer:{
    proxy:{
      ["/dev-api"]:{
        target:'http://59.110.170.246:6660',
        changeOrigin:true,
        pathRewrite: {
          ['^' + "/dev-ap"]: ''
        }
      }
    }

  }
};

module.exports = vueConfig;
