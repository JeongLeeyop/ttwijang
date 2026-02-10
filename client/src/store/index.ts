import Vue from 'vue';
import Vuex from 'vuex';
import { IUserState } from './modules/user';
import TeamModule from './modules/team';
import LeagueModule from './modules/league';
import MatchModule from './modules/match';
import GuestModule from './modules/guest';
import CashModule from './modules/cash';

Vue.use(Vuex);

export interface IRootState {
  user: IUserState
}

const store = new Vuex.Store<IRootState>({
  modules: {
    team: TeamModule,
    league: LeagueModule,
    match: MatchModule,
    guest: GuestModule,
    cash: CashModule,
  },
});

export default store;
