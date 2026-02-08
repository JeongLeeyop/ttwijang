import {
 Module, VuexModule, Mutation, Action,
} from 'vuex-module-decorators';
import {
  getTeamList,
  getTeamDetail,
  getMyTeams,
  createTeam,
  updateTeam,
  joinTeam,
  processJoinRequest,
  getTeamMembers,
  checkMembershipStatus,
  CreateTeamRequest,
  UpdateTeamRequest,
  JoinTeamRequest,
  ProcessJoinRequest,
  MembershipStatus,
} from '@/api/team';

export interface Team {
  uid: string
  name: string
  teamCode: string
  description?: string
  logoUrl?: string
  establishedYear?: number
  activityDays?: string
  activityTimes?: string
  regionSido?: string
  regionSigungu?: string
  homeStadium?: string
  mannerScore: number
  memberCount: number
  totalSponsorship?: number
  ownerUid: string
}

export interface TeamMember {
  uid: string
  userUid: string
  userName: string
  profileImageUrl?: string
  role: 'OWNER' | 'MANAGER' | 'MEMBER'
  position?: string
  status: 'PENDING' | 'ACTIVE' | 'REJECTED' | 'LEFT'
  joinedDate?: string
}

export interface TeamState {
  teams: Team[]
  myTeams: Team[]
  currentTeam: Team | null
  teamMembers: TeamMember[]
  membershipStatus: MembershipStatus | null
  loading: boolean
  error: string | null
}

@Module({ namespaced: true, name: 'team' })
export default class TeamModule extends VuexModule implements TeamState {
  teams: Team[] = [];

  myTeams: Team[] = [];

  currentTeam: Team | null = null;

  teamMembers: TeamMember[] = [];

  membershipStatus: MembershipStatus | null = null;

  loading = false;

  error: string | null = null;

  get getTeamByUid() {
    return (uid: string): Team | undefined => this.teams.find((t) => t.uid === uid);
  }

  get hasTeam(): boolean {
    return this.myTeams.length > 0;
  }

  get canCreateTeam(): boolean {
    return this.membershipStatus?.canCreateTeam ?? true;
  }

  get canJoinTeam(): boolean {
    return this.membershipStatus?.canJoinTeam ?? true;
  }

  @Mutation
  SET_TEAMS(teams: Team[]): void {
    this.teams = teams;
  }

  @Mutation
  SET_MY_TEAMS(teams: Team[]): void {
    this.myTeams = teams;
  }

  @Mutation
  SET_CURRENT_TEAM(team: Team | null): void {
    this.currentTeam = team;
  }

  @Mutation
  SET_TEAM_MEMBERS(members: TeamMember[]): void {
    this.teamMembers = members;
  }

  @Mutation
  SET_LOADING(loading: boolean): void {
    this.loading = loading;
  }

  @Mutation
  SET_ERROR(error: string | null): void {
    this.error = error;
  }

  @Mutation
  SET_MEMBERSHIP_STATUS(status: MembershipStatus | null): void {
    this.membershipStatus = status;
  }

  @Action
  async fetchTeamList(params?: {
    region?: string
    activityDay?: string
    ageGroup?: string
  }): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getTeamList(params);
      this.SET_TEAMS(response.data.content || response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '팀 목록을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchMyTeams(): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getMyTeams();
      this.SET_MY_TEAMS(response.data || []);
    } catch (error: any) {
      this.SET_ERROR(error.message || '내 팀 목록을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchTeamDetail(uid: string): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getTeamDetail(uid);
      this.SET_CURRENT_TEAM(response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '팀 정보를 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchTeamMembers(teamUid: string): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getTeamMembers(teamUid);
      this.SET_TEAM_MEMBERS(response.data || []);
    } catch (error: any) {
      this.SET_ERROR(error.message || '팀원 목록을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async createNewTeam(data: CreateTeamRequest): Promise<Team> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await createTeam(data);
      await this.fetchMyTeams();
      return response.data;
    } catch (error: any) {
      this.SET_ERROR(error.message || '팀 생성에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async updateTeamInfo(data: UpdateTeamRequest): Promise<Team> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await updateTeam(data);
      this.SET_CURRENT_TEAM(response.data);
      return response.data;
    } catch (error: any) {
      this.SET_ERROR(error.message || '팀 정보 수정에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async applyToJoinTeam(data: JoinTeamRequest): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      await joinTeam(data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '팀 가입 신청에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async handleJoinRequest(data: ProcessJoinRequest): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      await processJoinRequest(data);
      if (this.currentTeam) {
        await this.fetchTeamMembers(this.currentTeam.uid);
      }
    } catch (error: any) {
      this.SET_ERROR(error.message || '가입 신청 처리에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchMembershipStatus(): Promise<MembershipStatus | null> {
    try {
      const response = await checkMembershipStatus();
      const status = response.data as MembershipStatus;
      this.SET_MEMBERSHIP_STATUS(status);
      return status;
    } catch (error: any) {
      console.error('Failed to check membership status:', error);
      return null;
    }
  }
}
