<template>
  <div class="league-detail-page">
    <div class="page-header">
      <button class="back-btn" @click="$router.go(-1)">
        <i class="el-icon-arrow-left" />
      </button>
      <h2 class="page-title">{{ league ? league.name : '리그 소개' }}</h2>
    </div>

    <div v-if="isLoading" class="loading-wrap">
      <i class="el-icon-loading" />
    </div>

    <template v-else-if="league">
      <div class="league-meta">
        <el-tag :type="statusTagType" size="small" class="status-tag">{{ statusLabel }}</el-tag>
        <span v-if="league.season" class="meta-item">시즌: {{ league.season }}</span>
        <span class="meta-item">{{ league.regionSido }} {{ league.regionSigungu }}</span>
        <span class="meta-item">팀: {{ league.currentTeams }}/{{ league.maxTeams }}</span>
      </div>

      <div v-if="league.startDate" class="league-period">
        {{ league.startDate }} ~ {{ league.endDate }}
      </div>

      <div
        v-if="league.introContent"
        class="league-intro"
        v-html="safeIntroContent"
      />
      <div v-else class="league-empty-intro">
        등록된 소개글이 없습니다.
      </div>

      <div class="join-section">
        <div v-if="league.participationPoints > 0" class="points-notice">
          참여비: <strong>{{ league.participationPoints.toLocaleString() }} 캐쉬</strong>
        </div>
        <button
          class="join-btn"
          :class="{ 'join-btn--disabled': joinButtonDisabled }"
          :disabled="joinButtonDisabled || applying"
          @click="handleJoin"
        >
          <i v-if="applying" class="el-icon-loading" />
          {{ joinButtonText }}
        </button>
      </div>
    </template>

    <div v-else class="error-wrap">
      리그 정보를 불러오지 못했습니다.
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getLeagueDetail, getLeagueTeams, applyToLeague } from '@/api/league';
import { getMyTeams, checkMembershipStatus } from '@/api/team';
import { UserModule } from '@/store/modules/user';

@Component({ name: 'LeagueDetail' })
export default class extends Vue {
  private isLoading = false;

  private applying = false;

  private league: any = null;

  private isOwner = false;

  private alreadyJoined = false;

  get statusTagType(): string {
    if (!this.league) return '';
    return ({ RECRUITING: 'success', IN_PROGRESS: 'warning', COMPLETED: 'info' } as any)[this.league.status] || 'info';
  }

  get statusLabel(): string {
    if (!this.league) return '';
    return ({ RECRUITING: '모집중', IN_PROGRESS: '진행중', COMPLETED: '완료' } as any)[this.league.status] || this.league.status;
  }

  get joinButtonDisabled(): boolean {
    if (!UserModule.isLogin) return true;
    if (!this.league) return true;
    if (this.league.status !== 'RECRUITING') return true;
    if (this.league.currentTeams >= this.league.maxTeams) return true;
    if (this.alreadyJoined) return true;
    if (!this.isOwner) return true;
    return false;
  }

  get joinButtonText(): string {
    if (!UserModule.isLogin) return '로그인 후 이용 가능합니다';
    if (!this.league) return '리그 참여하기';
    if (this.league.status !== 'RECRUITING') return '모집중이 아닙니다';
    if (this.league.currentTeams >= this.league.maxTeams) return '모집 마감';
    if (this.alreadyJoined) return '이미 참여 중';
    if (!this.isOwner) return '팀 운영자만 신청 가능';
    return '리그 참여하기';
  }

  get safeIntroContent(): string {
    const raw = this.league?.introContent || '';
    return raw
      .replace(/<script\b[^>]*>[\s\S]*?<\/script>/gi, '')
      .replace(/<iframe\b[^>]*>[\s\S]*?<\/iframe>/gi, '')
      .replace(/\son\w+\s*=\s*"(?:[^"]*)"/gi, '')
      .replace(/\son\w+\s*=\s*'(?:[^']*)'/gi, '')
      .replace(/javascript:/gi, '');
  }

  async created(): Promise<void> {
    await this.fetchData();
  }

  private async fetchData(): Promise<void> {
    this.isLoading = true;
    try {
      const uid = this.$route.params.uid;
      const [leagueRes, teamsRes] = await Promise.all([
        getLeagueDetail(uid),
        getLeagueTeams(uid),
      ]);
      this.league = leagueRes.data;
      const leagueTeams: any[] = teamsRes.data || [];

      if (UserModule.isLogin) {
        try {
          const statusRes = await checkMembershipStatus();
          this.isOwner = !!statusRes.data?.hasCreatedTeam;
          if (this.isOwner) {
            const myRes = await getMyTeams();
            const myTeam: any = myRes.data;
            if (myTeam && myTeam.uid) {
              this.alreadyJoined = leagueTeams.some((t: any) => t.teamUid === myTeam.uid);
            }
          }
        } catch {
          // 팀 정보 조회 실패는 무시
        }
      }
    } catch {
      this.league = null;
    } finally {
      this.isLoading = false;
    }
  }

  private async handleJoin(): Promise<void> {
    if (!this.league) return;
    if (this.league.participationPoints > 0) {
      try {
        await this.$confirm(
          `참여비 ${this.league.participationPoints.toLocaleString()} 캐쉬가 차감됩니다.\n리그에 참여하시겠습니까?`,
          '리그 참여 확인',
          { confirmButtonText: '참여', cancelButtonText: '취소', type: 'warning' },
        );
      } catch {
        return;
      }
    }
    this.applying = true;
    try {
      await applyToLeague(this.$route.params.uid);
      this.$message.success('리그에 참여했습니다!');
      this.alreadyJoined = true;
      if (this.league) this.league.currentTeams += 1;
    } catch (e: any) {
      const msg = e?.response?.data?.message || '참여 중 오류가 발생했습니다.';
      this.$message.error(msg);
    } finally {
      this.applying = false;
    }
  }
}
</script>

<style scoped>
.league-detail-page {
  min-height: 100vh;
  background: #f5f7fa;
  padding-bottom: 100px;
}

.page-header {
  display: flex;
  align-items: center;
  padding: 16px;
  background: #fff;
  border-bottom: 1px solid #eee;
  position: sticky;
  top: 0;
  z-index: 10;
}

.back-btn {
  background: none;
  border: none;
  font-size: 20px;
  color: #333;
  cursor: pointer;
  padding: 0 12px 0 0;
}

.page-title {
  font-size: 17px;
  font-weight: 700;
  margin: 0;
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.loading-wrap,
.error-wrap {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 200px;
  font-size: 16px;
  color: #999;
}

.league-meta {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #eee;
}

.status-tag {
  font-size: 12px;
}

.meta-item {
  font-size: 13px;
  color: #666;
}

.league-period {
  padding: 8px 16px;
  font-size: 13px;
  color: #888;
  background: #fff;
  border-bottom: 1px solid #eee;
}

.league-intro {
  padding: 20px 16px;
  background: #fff;
  line-height: 1.7;
  font-size: 15px;
  color: #333;
}

.league-intro ::v-deep img {
  max-width: 100%;
  height: auto;
}

.league-intro ::v-deep h1,
.league-intro ::v-deep h2,
.league-intro ::v-deep h3 {
  margin: 12px 0 8px;
}

.league-intro ::v-deep p {
  margin: 8px 0;
}

.league-empty-intro {
  padding: 40px 16px;
  text-align: center;
  color: #bbb;
  font-size: 14px;
  background: #fff;
}

.join-section {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 100%;
  max-width: 480px;
  padding: 16px;
  box-sizing: border-box;
  background: #fff;
  border-top: 1px solid #eee;
  box-shadow: 0 -4px 12px rgba(0, 0, 0, 0.08);
}

.points-notice {
  text-align: center;
  font-size: 13px;
  color: #666;
  margin-bottom: 10px;
}

.points-notice strong {
  color: #e63946;
}

.join-btn {
  width: 100%;
  height: 48px;
  border: none;
  border-radius: 10px;
  background: #061da1;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: background 0.2s;
}

.join-btn:active {
  background: #04148a;
}

.join-btn--disabled,
.join-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
</style>
