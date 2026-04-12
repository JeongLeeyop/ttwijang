<template>
  <div class="user-wrap">
    <div class="user-title">
      <div class="tl__box">
        <p class="tl">리그 관리</p>
        <p class="usernumber">전체 {{ leagueList.length }}개 리그</p>
      </div>
      <div class="user__tab">
        <div class="user__subtab">
          <el-select v-model="filterSido" clearable placeholder="시/도" style="width:120px" @change="onFilterSidoChange">
            <el-option v-for="r in sidoList" :key="r.code" :label="r.name" :value="r.name" />
          </el-select>
          <el-select v-model="filterSigungu" clearable placeholder="시/군/구" style="width:130px" @change="fetchList">
            <el-option v-for="r in filterSigunguList" :key="r.code" :label="r.name" :value="r.name" />
          </el-select>
        </div>
        <button class="tool-btn" @click="openLeagueForm()">리그 생성 +</button>
      </div>
    </div>

    <el-table
      :data="leagueList"
      v-loading="loading"
      :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
      border
    >
      <el-table-column label="리그명" prop="name" min-width="200" />
      <el-table-column label="지역" width="160">
        <template slot-scope="scope">
          {{ scope.row.region || '-' }}
        </template>
      </el-table-column>
      <el-table-column label="시즌" prop="season" width="120" />
      <el-table-column label="기간" min-width="200">
        <template slot-scope="scope">
          <span v-if="scope.row.startDate">{{ scope.row.startDate }} ~ {{ scope.row.endDate }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="팀 수" width="90" align="center">
        <template slot-scope="scope">
          {{ scope.row.currentTeams }} / {{ scope.row.maxTeams }}
        </template>
      </el-table-column>
      <el-table-column label="상태" width="100">
        <template slot-scope="scope">
          <el-tag :type="statusType(scope.row.status)" size="small">
            {{ statusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="관리" width="200">
        <template slot-scope="scope">
          <div style="white-space:nowrap">
            <el-button size="mini" @click="openLeagueForm(scope.row)">수정</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">삭제</el-button>
            <el-dropdown size="mini" trigger="click" @command="(cmd) => handleCommand(cmd, scope.row)" style="margin-left:4px">
              <el-button size="mini" type="primary">
                더보기 <i class="el-icon-arrow-down el-icon--right" />
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="teams">팀 배정</el-dropdown-item>
                <el-dropdown-item command="match">경기 생성</el-dropdown-item>
                <el-dropdown-item command="schedule">경기 현황</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </template>
      </el-table-column>
    </el-table>

    <!-- 리그 생성/수정 다이얼로그 -->
    <el-dialog
      :title="leagueForm.uid ? '리그 수정' : '리그 생성'"
      :visible.sync="leagueDialogVisible"
      width="600px"
      @close="resetLeagueForm"
    >
      <el-form ref="leagueFormRef" :model="leagueForm" :rules="leagueRules" label-width="130px">
        <el-form-item label="리그명" prop="name">
          <el-input v-model="leagueForm.name" placeholder="예: 2025 진주 A리그" />
        </el-form-item>
        <el-form-item label="설명">
          <el-input v-model="leagueForm.description" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="시즌" prop="season">
          <el-input v-model="leagueForm.season" placeholder="예: 2025-2026" />
        </el-form-item>
        <el-form-item label="시작일" prop="startDate">
          <el-date-picker
            v-model="leagueForm.startDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="시작일 선택"
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="종료일" prop="endDate">
          <el-date-picker
            v-model="leagueForm.endDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="종료일 선택"
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="지역 (시/도)" prop="regionSido">
          <el-select v-model="leagueForm.regionSido" style="width:100%" @change="onLeagueSidoChange">
            <el-option v-for="r in sidoList" :key="r.code" :label="r.name" :value="r.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="지역 (시/군/구)">
          <el-select v-model="leagueForm.regionSigungu" clearable placeholder="선택 (선택사항)" style="width:100%">
            <el-option v-for="r in leagueSigunguList" :key="r.code" :label="r.name" :value="r.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="최대 팀 수" prop="maxTeams">
          <el-input-number v-model="leagueForm.maxTeams" :min="2" :max="32" />
        </el-form-item>
        <el-form-item label="규칙">
          <el-input v-model="leagueForm.rules" type="textarea" :rows="3" placeholder="리그 규칙을 입력하세요" />
        </el-form-item>
        <el-form-item v-if="leagueForm.uid" label="상태">
          <el-select v-model="leagueForm.status" style="width:100%">
            <el-option label="모집중" value="RECRUITING" />
            <el-option label="진행중" value="IN_PROGRESS" />
            <el-option label="완료" value="COMPLETED" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="leagueDialogVisible = false">취소</el-button>
        <el-button type="primary" :loading="saving" @click="handleLeagueSave">저장</el-button>
      </div>
    </el-dialog>

    <!-- 경기 현황 다이얼로그 -->
    <el-dialog
      :title="`경기 현황 - ${scheduleLeagueName}`"
      :visible.sync="scheduleDialogVisible"
      width="900px"
    >
      <el-table
        :data="scheduleMatches"
        v-loading="scheduleLoading"
        :header-cell-style="{ background: '#0f2027', color: '#fff', padding: '8px 0' }"
        border
        size="small"
      >
        <el-table-column label="라운드" prop="round" width="70" align="center" />
        <el-table-column label="경기일시" width="160">
          <template slot-scope="scope">
            {{ scope.row.matchDate }}
            <span v-if="scope.row.matchTime"> {{ scope.row.matchTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="홈팀" min-width="120">
          <template slot-scope="scope">{{ scope.row.homeTeamName }}</template>
        </el-table-column>
        <el-table-column label="스코어" width="90" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.homeScore !== null && scope.row.homeScore !== undefined">
              {{ scope.row.homeScore }} : {{ scope.row.awayScore }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="원정팀" min-width="120">
          <template slot-scope="scope">{{ scope.row.awayTeamName }}</template>
        </el-table-column>
        <el-table-column label="구장" min-width="140">
          <template slot-scope="scope">{{ scope.row.stadiumName || '-' }}</template>
        </el-table-column>
        <el-table-column label="상태" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="matchStatusType(scope.row.status)" size="mini">
              {{ matchStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="결과 입력" width="120" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.status !== 'FINISHED'"
              size="mini"
              type="primary"
              @click="openResultForm(scope.row)"
            >결과 입력</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer">
        <el-button @click="scheduleDialogVisible = false">닫기</el-button>
      </div>
    </el-dialog>

    <!-- 경기 결과 입력 다이얼로그 -->
    <el-dialog
      title="경기 결과 입력"
      :visible.sync="resultDialogVisible"
      width="360px"
    >
      <el-form ref="resultFormRef" :model="resultForm" label-width="100px">
        <el-form-item label="홈팀">
          <span>{{ selectedMatch ? selectedMatch.homeTeamName : '' }}</span>
        </el-form-item>
        <el-form-item label="홈팀 득점" :rules="[{ required: true }]">
          <el-input-number v-model="resultForm.homeScore" :min="0" />
        </el-form-item>
        <el-form-item label="원정팀">
          <span>{{ selectedMatch ? selectedMatch.awayTeamName : '' }}</span>
        </el-form-item>
        <el-form-item label="원정팀 득점" :rules="[{ required: true }]">
          <el-input-number v-model="resultForm.awayScore" :min="0" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="resultDialogVisible = false">취소</el-button>
        <el-button type="primary" :loading="savingResult" @click="handleResultSave">저장</el-button>
      </div>
    </el-dialog>

    <!-- 경기 생성 다이얼로그 -->
    <el-dialog
      :title="`경기 생성 - ${selectedLeague ? selectedLeague.name : ''}`"
      :visible.sync="matchDialogVisible"
      width="560px"
      @close="resetMatchForm"
    >
      <el-form ref="matchFormRef" :model="matchForm" :rules="matchRules" label-width="120px">
        <el-form-item label="홈팀" prop="homeTeamUid">
          <el-select v-model="matchForm.homeTeamUid" placeholder="홈팀 선택" style="width:100%">
            <el-option
              v-for="t in selectedLeagueTeams"
              :key="t.uid || t.teamUid"
              :label="t.name || t.teamName"
              :value="t.uid || t.teamUid"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="원정팀" prop="awayTeamUid">
          <el-select v-model="matchForm.awayTeamUid" placeholder="원정팀 선택" style="width:100%">
            <el-option
              v-for="t in selectedLeagueTeams"
              :key="t.uid || t.teamUid"
              :label="t.name || t.teamName"
              :value="t.uid || t.teamUid"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="경기 일시" prop="matchDate">
          <el-date-picker
            v-model="matchForm.matchDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="날짜 선택"
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="시작 시간" prop="matchTime">
          <el-time-picker
            v-model="matchForm.matchTime"
            value-format="HH:mm"
            placeholder="시간 선택"
            style="width:100%"
          />
        </el-form-item>
        <el-form-item label="소요 시간(분)" prop="durationMinutes">
          <el-select v-model="matchForm.durationMinutes" style="width:100%">
            <el-option label="60분" :value="60" />
            <el-option label="90분" :value="90" />
            <el-option label="120분" :value="120" />
          </el-select>
        </el-form-item>
        <el-form-item label="회차" prop="round">
          <el-input-number v-model="matchForm.round" :min="1" />
        </el-form-item>
        <el-form-item label="구장명" prop="stadiumName">
          <el-input v-model="matchForm.stadiumName" />
        </el-form-item>
        <el-form-item label="구장 주소">
          <el-input v-model="matchForm.stadiumAddress" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="matchDialogVisible = false">취소</el-button>
        <el-button type="primary" :loading="savingMatch" @click="handleMatchSave">생성</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import {
  getLeagueList, createLeague, updateLeague, deleteLeague,
  getLeagueTeams, createLeagueMatch, updateMatchResult, getAdminLeagueMatches,
} from '@/api/league';
import { getSidoList, getSigunguList } from '@/api/region';
import { ElForm } from 'element-ui/types/form';

@Component({ name: 'LeagueList' })
export default class extends Vue {
  private loading = false;

  private saving = false;

  private savingMatch = false;

  private leagueList: any[] = [];

  private sidoList: any[] = [];

  private filterSido = '';

  private filterSigungu = '';

  private filterSigunguList: any[] = [];

  // 리그 폼
  private leagueDialogVisible = false;

  private leagueSigunguList: any[] = [];

  private leagueForm: any = {
    uid: null,
name: '',
description: '',
season: '',
    startDate: '',
endDate: '',
    regionSido: '',
regionSigungu: '',
maxTeams: 8,
rules: '',
status: 'RECRUITING',
  };

  private leagueRules = {
    name: [{ required: true, message: '리그명을 입력하세요', trigger: 'blur' }],
    season: [{ required: true, message: '시즌을 입력하세요', trigger: 'blur' }],
    regionSido: [{ required: true, message: '지역을 선택하세요', trigger: 'change' }],
    startDate: [{ required: true, message: '시작일을 선택하세요', trigger: 'change' }],
    endDate: [{ required: true, message: '종료일을 선택하세요', trigger: 'change' }],
    maxTeams: [{ required: true, message: '최대 팀 수를 입력하세요', trigger: 'blur' }],
  };

  // 경기 현황
  private scheduleDialogVisible = false;

  private scheduleLoading = false;

  private scheduleLeagueName = '';

  private scheduleMatches: any[] = [];

  private currentScheduleLeagueUid = '';

  // 경기 결과 입력
  private resultDialogVisible = false;

  private savingResult = false;

  private selectedMatch: any = null;

  private resultForm = { matchUid: '', homeScore: 0, awayScore: 0 };

  // 경기 폼
  private matchDialogVisible = false;

  private selectedLeague: any = null;

  private selectedLeagueTeams: any[] = [];

  private matchForm: any = {
    leagueUid: '',
homeTeamUid: '',
awayTeamUid: '',
    matchDate: '',
matchTime: '',
durationMinutes: 90,
round: 1,
    stadiumName: '',
stadiumAddress: '',
  };

  private matchRules = {
    homeTeamUid: [{ required: true, message: '홈팀을 선택하세요', trigger: 'change' }],
    awayTeamUid: [{ required: true, message: '원정팀을 선택하세요', trigger: 'change' }],
    matchDate: [{ required: true, message: '날짜를 선택하세요', trigger: 'change' }],
    matchTime: [{ required: true, message: '시간을 선택하세요', trigger: 'change' }],
    stadiumName: [{ required: true, message: '구장명을 입력하세요', trigger: 'blur' }],
    round: [{ required: true, message: '회차를 입력하세요', trigger: 'blur' }],
  };

  statusType(s: string) {
    return ({ RECRUITING: 'success', IN_PROGRESS: 'warning', COMPLETED: 'info' } as any)[s] || 'info';
  }

  statusLabel(s: string) {
    return ({ RECRUITING: '모집중', IN_PROGRESS: '진행중', COMPLETED: '완료' } as any)[s] || s;
  }

  async created() {
    const [, sidoRes] = await Promise.all([this.fetchList(), getSidoList()]);
    this.sidoList = sidoRes.data || [];
  }

  async fetchList() {
    this.loading = true;
    try {
      const params: any = {};
      if (this.filterSido) params.regionSido = this.filterSido;
      if (this.filterSigungu) params.regionSigungu = this.filterSigungu;
      const res = await getLeagueList(params);
      this.leagueList = res.data?.content || res.data || [];
    } finally {
      this.loading = false;
    }
  }

  async onFilterSidoChange(name: string) {
    this.filterSigungu = '';
    this.filterSigunguList = [];
    if (name) {
      const sido = this.sidoList.find((r: any) => r.name === name);
      if (sido) {
        const res = await getSigunguList(sido.code);
        this.filterSigunguList = res.data || [];
      }
    }
    this.fetchList();
  }

  // ── 리그 폼 ──
  openLeagueForm(row?: any) {
    if (row) {
      this.leagueForm = { ...row };
      if (row.regionSido) this.onLeagueSidoChange(row.regionSido);
    } else {
      this.resetLeagueForm();
    }
    this.leagueDialogVisible = true;
  }

  resetLeagueForm() {
    this.leagueForm = {
      uid: null,
name: '',
description: '',
season: '',
      startDate: '',
endDate: '',
      regionSido: '',
regionSigungu: '',
maxTeams: 8,
rules: '',
status: 'RECRUITING',
    };
    this.leagueSigunguList = [];
  }

  async onLeagueSidoChange(name: string) {
    this.leagueForm.regionSigungu = '';
    this.leagueSigunguList = [];
    if (name) {
      const sido = this.sidoList.find((r: any) => r.name === name);
      if (sido) {
        const res = await getSigunguList(sido.code);
        this.leagueSigunguList = res.data || [];
      }
    }
  }

  handleLeagueSave() {
    (this.$refs.leagueFormRef as ElForm).validate(async (valid) => {
      if (!valid) return;
      this.saving = true;
      try {
        if (this.leagueForm.uid) {
          await updateLeague(this.leagueForm.uid, this.leagueForm);
          this.$message.success('수정되었습니다.');
        } else {
          await createLeague(this.leagueForm);
          this.$message.success('리그가 생성되었습니다.');
        }
        this.leagueDialogVisible = false;
        await this.fetchList();
      } catch (e: any) {
        const msg = e?.response?.data?.message || '저장 중 오류가 발생했습니다.';
        this.$message.error(msg);
      } finally {
        this.saving = false;
      }
    });
  }

  async handleDelete(row: any) {
    await this.$confirm(`"${row.name}" 리그를 삭제하시겠습니까?`, '삭제 확인', { type: 'warning' });
    try {
      await deleteLeague(row.uid);
      this.$message.success('삭제되었습니다.');
      await this.fetchList();
    } catch {
      this.$message.error('삭제 중 오류가 발생했습니다.');
    }
  }

  goTeamAssign(row: any) {
    this.$router.push({ name: 'LeagueTeams', params: { uid: row.uid } });
  }

  handleCommand(command: string, row: any) {
    if (command === 'teams') this.goTeamAssign(row);
    else if (command === 'match') this.openMatchForm(row);
    else if (command === 'schedule') this.openScheduleDialog(row);
  }

  // ── 경기 폼 ──
  async openMatchForm(league: any) {
    this.selectedLeague = league;
    this.matchForm = {
      leagueUid: league.uid,
homeTeamUid: '',
awayTeamUid: '',
      matchDate: '',
matchTime: '',
durationMinutes: 90,
round: 1,
      stadiumName: '',
stadiumAddress: '',
    };
    try {
      const res = await getLeagueTeams(league.uid);
      this.selectedLeagueTeams = res.data || [];
    } catch {
      this.selectedLeagueTeams = [];
    }
    this.matchDialogVisible = true;
  }

  resetMatchForm() {
    this.selectedLeague = null;
    this.selectedLeagueTeams = [];
  }

  // ── 경기 현황 ──
  async openScheduleDialog(league: any) {
    this.scheduleLeagueName = league.name;
    this.currentScheduleLeagueUid = league.uid;
    this.scheduleDialogVisible = true;
    this.scheduleLoading = true;
    try {
      const res = await getAdminLeagueMatches(league.uid);
      this.scheduleMatches = res.data || [];
    } catch {
      this.scheduleMatches = [];
    } finally {
      this.scheduleLoading = false;
    }
  }

  matchStatusType(s: string) {
    return ({
 SCHEDULED: '', IN_PROGRESS: 'warning', FINISHED: 'success', CANCELLED: 'danger',
} as any)[s] || '';
  }

  matchStatusLabel(s: string) {
    return ({
 SCHEDULED: '예정', IN_PROGRESS: '진행중', FINISHED: '종료', CANCELLED: '취소',
} as any)[s] || s;
  }

  openResultForm(match: any) {
    this.selectedMatch = match;
    this.resultForm = {
      matchUid: match.uid,
      homeScore: match.homeScore ?? 0,
      awayScore: match.awayScore ?? 0,
    };
    this.resultDialogVisible = true;
  }

  async handleResultSave() {
    this.savingResult = true;
    try {
      await updateMatchResult(this.resultForm);
      this.$message.success('결과가 저장되었습니다.');
      this.resultDialogVisible = false;
      // 현황 새로고침
      const res = await getAdminLeagueMatches(this.currentScheduleLeagueUid);
      this.scheduleMatches = res.data || [];
    } catch (e: any) {
      const msg = e?.response?.data?.message || '저장 중 오류가 발생했습니다.';
      this.$message.error(msg);
    } finally {
      this.savingResult = false;
    }
  }

  handleMatchSave() {
    (this.$refs.matchFormRef as ElForm).validate(async (valid) => {
      if (!valid) return;
      this.savingMatch = true;
      try {
        await createLeagueMatch(this.matchForm);
        this.$message.success('경기가 생성되었습니다.');
        this.matchDialogVisible = false;
      } catch (e: any) {
        const msg = e?.response?.data?.message || '경기 생성 중 오류가 발생했습니다.';
        this.$message.error(msg);
      } finally {
        this.savingMatch = false;
      }
    });
  }
}
</script>
