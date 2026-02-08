const path = require('path');

const devPort = 3000;

function resolve(dir) {
  return path.join(__dirname, dir);
}

module.exports = {
  publicPath: '/',
  outputDir: 'dist',
  transpileDependencies: ['vue-daum-postcode', 'vue-js-modal'],
  devServer: {
    disableHostCheck: true,
    port: devPort,
    proxy: {
      [process.env.VUE_APP_COMMON_API]: {
        target: 'http://localhost:8081/api',
        changeOrigin: true,
        pathRewrite: {
          [`^${process.env.VUE_APP_COMMON_API}`]: '',
        },
      },
      [process.env.VUE_APP_BASE_API]: {
        target: 'http://localhost:8081/api',
        changeOrigin: true,
        pathRewrite: {
          [`^${process.env.VUE_APP_BASE_API}`]: '',
        },
      },
      [process.env.VUE_APP_OAUTH_API]: {
        target: 'http://localhost:8081/oauth',
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
  pluginOptions: {
    i18n: {
      locale: 'en',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableInSFC: false,
      includeLocales: false,
      enableBridge: true,
    },
  },
};
