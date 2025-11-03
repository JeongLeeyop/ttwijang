import Vue from 'vue';
import ElementUI from 'element-ui';
import locale from 'element-ui/lib/locale/lang/ko';
import App from './App.vue';
import router from './router';
import store from './store';
import * as filters from './filters';
import './permission';
// import i18n from './i18n';

Vue.config.productionTip = false;

Vue.use(ElementUI, { locale });

Object.keys(filters).forEach((key) => {
  Vue.filter(key, (filters as { [key: string ]: Function })[key]);
});

new Vue({
  router,
  store,
  // i18n,
  render: (h) => h(App),
}).$mount('#app');
