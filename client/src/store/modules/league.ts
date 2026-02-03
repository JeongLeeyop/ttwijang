import {
 Module, VuexModule, Mutation, Action,
} from 'vuex-module-decorators';
import {
  getLeagueList,
  getLeagueDetail,
  getLeagueStandings,
  getLeagueSchedule,
  applyToLeague,
  League,
  LeagueStanding,
  LeagueMatch,
} from '@/api/league';

export interface LeagueState {
  leagues: League[]
  currentLeague: League | null
  standings: LeagueStanding[]
  schedule: LeagueMatch[]
  loading: boolean
  error: string | null
}

@Module({ namespaced: true, name: 'league' })
export default class LeagueModule extends VuexModule implements LeagueState {
  leagues: League[] = [];

  currentLeague: League | null = null;

  standings: LeagueStanding[] = [];

  schedule: LeagueMatch[] = [];

  loading = false;

  error: string | null = null;

  get sortedStandings(): LeagueStanding[] {
    return [...this.standings].sort((a, b) => a.rank - b.rank);
  }

  get upcomingMatches(): LeagueMatch[] {
    const now = new Date();
    return this.schedule.filter((match) => new Date(match.matchDate) > now);
  }

  get completedMatches(): LeagueMatch[] {
    return this.schedule.filter((match) => match.status === 'FINISHED');
  }

  @Mutation
  SET_LEAGUES(leagues: League[]): void {
    this.leagues = leagues;
  }

  @Mutation
  SET_CURRENT_LEAGUE(league: League | null): void {
    this.currentLeague = league;
  }

  @Mutation
  SET_STANDINGS(standings: LeagueStanding[]): void {
    this.standings = standings;
  }

  @Mutation
  SET_SCHEDULE(schedule: LeagueMatch[]): void {
    this.schedule = schedule;
  }

  @Mutation
  SET_LOADING(loading: boolean): void {
    this.loading = loading;
  }

  @Mutation
  SET_ERROR(error: string | null): void {
    this.error = error;
  }

  @Action
  async fetchLeagueList(params?: {
    region?: string
    grade?: string
    status?: string
  }): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getLeagueList(params);
      this.SET_LEAGUES(response.data.content || response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '리그 목록을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchLeagueDetail(uid: string): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getLeagueDetail(uid);
      this.SET_CURRENT_LEAGUE(response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '리그 정보를 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchLeagueStandings(leagueUid: string): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getLeagueStandings(leagueUid);
      this.SET_STANDINGS(response.data || []);
    } catch (error: any) {
      this.SET_ERROR(error.message || '순위표를 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchLeagueSchedule(params: {
    leagueUid: string
    round?: number
    startDate?: string
    endDate?: string
  }): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const { leagueUid, ...queryParams } = params;
      const response = await getLeagueSchedule(leagueUid, queryParams);
      this.SET_SCHEDULE(response.data.content || response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '리그 일정을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async applyToJoinLeague(params: { leagueUid: string, teamUid: string }): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      await applyToLeague(params.leagueUid, params.teamUid);
    } catch (error: any) {
      this.SET_ERROR(error.message || '리그 참가 신청에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }
}
