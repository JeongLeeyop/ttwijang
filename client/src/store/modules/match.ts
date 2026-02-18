import {
 Module, VuexModule, Mutation, Action,
} from 'vuex-module-decorators';
import {
  getMatchList,
  getMatchDetail,
  getMatchesByDateRange,
  createMatch,
  applyToMatch,
  cancelMatchApplication,
  deleteMatch,
  CreateMatchRequest,
  ApplyMatchRequest,
  FutsalMatch,
} from '@/api/match';

export interface MatchState {
  matches: FutsalMatch[]
  currentMatch: FutsalMatch | null
  calendarMatches: FutsalMatch[]
  loading: boolean
  error: string | null
}

@Module({ namespaced: true, name: 'match' })
export default class MatchModule extends VuexModule implements MatchState {
  matches: FutsalMatch[] = [];

  currentMatch: FutsalMatch | null = null;

  calendarMatches: FutsalMatch[] = [];

  loading = false;

  error: string | null = null;

  get recruitingMatches(): FutsalMatch[] {
    return this.matches.filter((m) => m.status === 'RECRUITING');
  }

  get matchedMatches(): FutsalMatch[] {
    return this.matches.filter((m) => m.status === 'MATCHED');
  }

  @Mutation
  SET_MATCHES(matches: FutsalMatch[]): void {
    this.matches = matches;
  }

  @Mutation
  SET_CURRENT_MATCH(match: FutsalMatch | null): void {
    this.currentMatch = match;
  }

  @Mutation
  SET_CALENDAR_MATCHES(matches: FutsalMatch[]): void {
    this.calendarMatches = matches;
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
  async fetchMatchList(params?: {
    date?: string
    status?: string
    region?: string
    matchType?: string
  }): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getMatchList(params);
      this.SET_MATCHES(response.data.content || response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '매치 목록을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchMatchDetail(uid: string): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getMatchDetail(uid);
      this.SET_CURRENT_MATCH(response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '매치 정보를 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchMatchesByDateRange(params: { startDate: string, endDate: string }): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getMatchesByDateRange(params.startDate, params.endDate);
      this.SET_CALENDAR_MATCHES(response.data.content || response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '캘린더 매치를 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async createNewMatch(data: CreateMatchRequest): Promise<FutsalMatch> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await createMatch(data);
      return response.data;
    } catch (error: any) {
      this.SET_ERROR(error.message || '매치 생성에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async applyMatch(data: ApplyMatchRequest): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      await applyToMatch(data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '매치 신청에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async cancelApplication(matchUid: string): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      await cancelMatchApplication(matchUid);
    } catch (error: any) {
      this.SET_ERROR(error.message || '매치 신청 취소에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async removeMatch(uid: string): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      await deleteMatch(uid);
      await this.fetchMatchList();
    } catch (error: any) {
      this.SET_ERROR(error.message || '매치 삭제에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }
}
