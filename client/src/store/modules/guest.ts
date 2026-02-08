import {
 Module, VuexModule, Mutation, Action,
} from 'vuex-module-decorators';
import {
  getGuestRecruitmentList,
  getGuestRecruitmentDetail,
  getGuestRecruitmentsByDateRange,
  createGuestRecruitment,
  applyAsGuest,
  processGuestApplication,
  getMyGuestApplications,
  getRecruitmentApplications,
  CreateGuestRecruitmentRequest,
  ApplyGuestRequest,
  ProcessGuestApplicationRequest,
  GuestRecruitment,
  GuestApplication,
} from '@/api/guest';

export interface GuestState {
  recruitments: GuestRecruitment[]
  currentRecruitment: GuestRecruitment | null
  calendarRecruitments: GuestRecruitment[]
  myApplications: GuestApplication[]
  recruitmentApplications: GuestApplication[]
  loading: boolean
  error: string | null
}

@Module({ namespaced: true, name: 'guest' })
export default class GuestModule extends VuexModule implements GuestState {
  recruitments: GuestRecruitment[] = [];

  currentRecruitment: GuestRecruitment | null = null;

  calendarRecruitments: GuestRecruitment[] = [];

  myApplications: GuestApplication[] = [];

  recruitmentApplications: GuestApplication[] = [];

  loading = false;

  error: string | null = null;

  get activeRecruitments(): GuestRecruitment[] {
    return this.recruitments.filter((r) => r.status === 'RECRUITING');
  }

  get pendingApplications(): GuestApplication[] {
    return this.myApplications.filter((a) => a.status === 'PENDING');
  }

  @Mutation
  SET_RECRUITMENTS(recruitments: GuestRecruitment[]): void {
    this.recruitments = recruitments;
  }

  @Mutation
  SET_CURRENT_RECRUITMENT(recruitment: GuestRecruitment | null): void {
    this.currentRecruitment = recruitment;
  }

  @Mutation
  SET_CALENDAR_RECRUITMENTS(recruitments: GuestRecruitment[]): void {
    this.calendarRecruitments = recruitments;
  }

  @Mutation
  SET_MY_APPLICATIONS(applications: GuestApplication[]): void {
    this.myApplications = applications;
  }

  @Mutation
  SET_RECRUITMENT_APPLICATIONS(applications: GuestApplication[]): void {
    this.recruitmentApplications = applications;
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
  async fetchRecruitmentList(params?: {
    region?: string
    date?: string
    status?: string
  }): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getGuestRecruitmentList(params);
      this.SET_RECRUITMENTS(response.data.content || response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '게스트 모집 목록을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchRecruitmentDetail(uid: string): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getGuestRecruitmentDetail(uid);
      this.SET_CURRENT_RECRUITMENT(response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '게스트 모집 정보를 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchRecruitmentsByDateRange(params: { startDate: string, endDate: string }): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getGuestRecruitmentsByDateRange(params.startDate, params.endDate);
      this.SET_CALENDAR_RECRUITMENTS(response.data.content || response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '캘린더 게스트 모집을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async createRecruitment(data: CreateGuestRecruitmentRequest): Promise<GuestRecruitment> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await createGuestRecruitment(data);
      return response.data;
    } catch (error: any) {
      this.SET_ERROR(error.message || '게스트 모집 생성에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async applyToRecruitment(data: ApplyGuestRequest): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      await applyAsGuest(data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '게스트 신청에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async processApplication(data: ProcessGuestApplicationRequest): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      await processGuestApplication(data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '신청 처리에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchMyApplications(): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getMyGuestApplications();
      this.SET_MY_APPLICATIONS(response.data || []);
    } catch (error: any) {
      this.SET_ERROR(error.message || '내 신청 목록을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchRecruitmentApplications(recruitmentUid: string): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getRecruitmentApplications(recruitmentUid);
      this.SET_RECRUITMENT_APPLICATIONS(response.data || []);
    } catch (error: any) {
      this.SET_ERROR(error.message || '신청자 목록을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }
}
