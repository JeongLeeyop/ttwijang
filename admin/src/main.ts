import Vue from 'vue';

import ElementUI from 'element-ui';
import locale from 'element-ui/lib/locale/lang/ko';

import 'xe-utils';
import VModal from 'vue-js-modal';
import 'element-ui/lib/theme-chalk/index.css';
import Loading from 'vue-loading-overlay';
import 'vue-loading-overlay/dist/vue-loading.css';

import VueDaumPostcode from 'vue-daum-postcode';

import { library } from '@fortawesome/fontawesome-svg-core';
import {
  faCog,
  faUser,
  faUserCircle,
  faChevronDown,
  faChevronUp,
  faSquare,
  faCheckSquare,
  faExpand,
  faTimes,
  faQuestion,
  faEnvelope,
  faLock,
  faPlus,
  faUpload,
  faPencilAlt,
  faEdit,
  faLevelDownAlt,
  faChevronLeft,
  faChevronRight,
  faTrashAlt,
  faExternalLinkAlt,
  faPencilRuler,
  faPaintBrush,
} from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';

import * as filters from '@/utils/filters';

import App from './App.vue';
import router from './router';
import store from './store';

import '@toast-ui/editor/dist/toastui-editor.css';
import '@toast-ui/editor/dist/i18n/ko-kr';

import '@/permission';

import 'moment/locale/ko';

Vue.use(ElementUI, { locale });

library.add(
  faCog,
  faUser,
  faUserCircle,
  faChevronDown,
  faChevronUp,
  faSquare,
  faCheckSquare,
  faExpand,
  faTimes,
  faQuestion,
  faEnvelope,
  faLock,
  faPlus,
  faUpload,
  faPencilAlt,
  faEdit,
  faLevelDownAlt,
  faChevronLeft,
  faChevronRight,
  faTrashAlt,
  faExternalLinkAlt,
  faPencilRuler,
  faPaintBrush,
);

// const config = {
//   locale: 'ko',
//   dictionary: {
//     ko,
//   },
// };
// Vue.use(VeeValidator, config, { inject: 'false' });
Vue.use(Loading, {
  color: 'skyblue',
});

Vue.use(VModal);
Vue.use(VueDaumPostcode);
Vue.component('font-awesome-icon', FontAwesomeIcon);
Vue.config.productionTip = false;

Object.keys(filters).forEach((key) => {
  Vue.filter(key, (filters as { [key: string ]: Function })[key]);
});

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
