const path = require('path');

const devPort = 3001;

function resolve(dir) {
  return path.join(__dirname, dir);
}

module.exports = {
  publicPath: process.env.NODE_ENV === 'production' ? '/adm' : '/',
  outputDir: 'dist',
  transpileDependencies: ['vue-daum-postcode', 'vue-js-modal'],
  lintOnSave: false,
  devServer: {
    disableHostCheck: true,
    port: devPort,
    proxy: {
      [`^${process.env.VUE_APP_BASE_API}`]: {
        target: 'http://localhost:8080/api',
        changeOrigin: true,
        pathRewrite: {
          [`^${process.env.VUE_APP_BASE_API}`]: '',
        },
      },
      [`^${process.env.VUE_APP_OAUTH_API}`]: {
        target: 'http://localhost:8080/oauth',
        changeOrigin: true,
        pathRewrite: {
          [`^${process.env.VUE_APP_OAUTH_API}`]: '',
        },
      },
    },
  },
  configureWebpack: {
    resolve: {
      alias: {
        '@': resolve('src'),
      },
    },
  },
};