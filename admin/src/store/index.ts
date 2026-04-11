import Vue from 'vue';
import Vuex from 'vuex';
import { IUserState } from './modules/user';
import { IEditState } from './modules/edit';
import { IMenuState } from './modules/menu';
import { ILayoutState } from './modules/layout';

Vue.use(Vuex);

export interface IRootState {
  user: IUserState
  edit: IEditState
  menu: IMenuState
  layout: ILayoutState
}

export default new Vuex.Store<IRootState>({});
