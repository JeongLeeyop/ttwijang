<template>
  <div class="main league-page team-page-view" :class="{ 'non-owner': !isOwner }">
    <div class="background-wave"></div>
    <!-- Content -->
    <div class="content">
      <!-- Team Header (Centered) -->
      <div class="team-profile-section">
        <div class="team-profile-logo-wrap">
          <img
            :src="teamInfo.logoUrl || defaultLogo"
            :alt="teamInfo.name"
            class="team-profile-logo"
          >
        </div>
        <h2 class="team-profile-name">{{ teamInfo.name || '팀 이름' }}</h2>
        <p class="team-profile-owner">{{ teamInfo.ownerName || '운영자' }} ({{ myRoleLabel }})</p>
        <div v-if="isOwner" class="team-profile-buttons">
          <button class="team-btn team-btn-outline" @click="goToDashboard">
            대시 보드
          </button>
          <button class="team-btn team-btn-primary" @click="goToTeamSettings">
            팀 설정
          </button>
        </div>
      </div>
    </div>

    <!-- Tab Section (흰색 영역 - 토글) -->
    <div
      class="league-section"
      :class="{ 'expanded': isExpanded, 'dragging': isDragging }"
      :style="sectionStyle"
    >
      <div
        class="league-section-handle"
        @touchstart.prevent="onDragStart"
        @mousedown.prevent="onDragStart"
      >
        <div class="handle-bar"></div>
      </div>
      <div class="league-section-content">
        <!-- 구단주 Info Bar -->
        <div class="team-owner-bar">
          <span class="owner-bar-text">
            구단주 : {{ ownerSponsorName || 'OOO' }} ({{ teamInfo.name || '팀명' }})
          </span>
          <span class="owner-bar-link" @click="showOwnerInfo">구단주 정보</span>
        </div>
        <!-- Tab Nav -->
        <div class="team-tab-nav">
          <div
            v-for="tab in tabs"
            :key="tab.key"
            class="team-tab-item"
            :class="{ 'active': activeTab === tab.key }"
            @click="activeTab = tab.key"
          >
            {{ tab.label }}
          </div>
        </div>

        <!-- ========== 커뮤니티 Tab ========== -->
        <div v-show="activeTab === 'community'" class="team-tab-content community-tab">
          <!-- 공지사항 카드 -->
          <div v-if="noticePosts.length > 0" class="notice-card">
            <div class="notice-card-header">
              <span class="notice-icon">📢</span>
              <span class="notice-title">공지사항</span>
            </div>
            <div
              v-for="notice in noticePosts"
              :key="'notice-' + notice.uid"
              class="notice-content"
              @click="togglePostExpand(notice.uid)"
            >
              <p class="notice-text">{{ notice.title }}</p>
            </div>
          </div>

          <!-- 게시글 목록 -->
          <div v-if="communityPosts.length === 0 && noticePosts.length === 0" class="empty-state">
            <i class="el-icon-document"></i>
            <p>등록된 게시글이 없습니다.</p>
          </div>
          <div v-else class="community-feed">
            <div
              v-for="post in regularPosts"
              :key="post.uid"
              class="community-post-card"
              :class="{ 'expanded': expandedPostUid === post.uid }"
            >
              <div class="post-header" @click="togglePostExpand(post.uid)">
                <div class="post-writer-info">
                  <span class="post-writer-dot">●</span>
                  <span class="post-writer-name">{{ post.writer }}</span>
                </div>
                <span class="post-date">{{ post.createDate }}</span>
              </div>
              <div class="post-body" @click="togglePostExpand(post.uid)">
                <p class="post-content-text">{{ post.title }}</p>
                <p v-if="post.content" class="post-content-desc">{{ post.content }}</p>
                <div v-if="getPostImage(post)" class="post-image-wrap">
                  <img :src="getPostImage(post)" alt="게시글 이미지" class="post-image">
                </div>
              </div>
              <div class="post-footer">
                <div class="post-reactions">
                  <span
                    class="reaction-item like-btn"
                    :class="{ 'liked': post.likeStatus }"
                    @click.stop="toggleLike(post)"
                  >
                    <i :class="post.likeStatus ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
                    {{ post.likeCount || 0 }}
                  </span>
                  <span class="reaction-item">
                    <i class="el-icon-chat-dot-round"></i>
                    {{ post.commentCount || 0 }}
                  </span>
                </div>
                <div v-if="post.hasAuthority" class="post-actions">
                  <span class="post-action-btn" @click.stop="editPost(post)">수정</span>
                  <span class="post-action-btn delete" @click.stop="confirmDeletePost(post)">삭제</span>
                </div>
              </div>

              <!-- 확장 댓글 영역 -->
              <transition name="slide-fade">
                <div v-if="expandedPostUid === post.uid" class="post-comments-section">
                  <div class="comments-divider"></div>
                  <div v-if="postComments[post.uid] && postComments[post.uid].length > 0" class="comments-list">
                    <div
                      v-for="comment in postComments[post.uid]"
                      :key="comment.uid"
                      class="comment-item"
                    >
                      <div class="comment-header">
                        <span class="comment-writer">{{ comment.writer }}</span>
                        <span class="comment-date">{{ formatDateTime(comment.createDate) }}</span>
                      </div>
                      <p class="comment-text">{{ comment.contents }}</p>
                      <span
                        v-if="comment.isMine"
                        class="comment-delete"
                        @click.stop="confirmDeleteComment(comment)"
                      >삭제</span>
                    </div>
                  </div>
                  <div v-else class="no-comments">
                    <span>댓글이 없습니다.</span>
                  </div>
                  <div class="comment-input-row">
                    <input
                      v-model="commentTexts[post.uid]"
                      type="text"
                      placeholder="댓글을 입력하세요..."
                      class="comment-input"
                      @keyup.enter="submitComment(post.uid)"
                    >
                    <button
                      class="comment-submit-btn"
                      :disabled="!commentTexts[post.uid]"
                      @click="submitComment(post.uid)"
                    >
                      전송
                    </button>
                  </div>
                </div>
              </transition>
            </div>
          </div>

          <!-- 글쓰기 버튼 (모든 팀원) -->
          <div v-if="isMember || isOwner" class="community-write-btn" @click="openWriteModal">
            <i class="el-icon-edit"></i>
            <span>글쓰기</span>
          </div>
        </div>

        <!-- ========== 매치경기 Tab ========== -->
        <div v-show="activeTab === 'match'" class="team-tab-content match-tab">
          <div class="match-filter-row">
            <span
              class="match-filter-tag"
              :class="{ 'active': matchFilter === 'ALL' }"
              @click="matchFilter = 'ALL'"
            >전체</span>
            <span
              class="match-filter-tag"
              :class="{ 'active': matchFilter === 'FRIENDLY' }"
              @click="matchFilter = 'FRIENDLY'"
            >친선경기</span>
            <span
              class="match-filter-tag"
              :class="{ 'active': matchFilter === 'FREE' }"
              @click="matchFilter = 'FREE'"
            >자체경기</span>
          </div>
          <div v-if="filteredMatches.length === 0" class="empty-state">
            <i class="el-icon-football"></i>
            <p>매치 경기가 없습니다.</p>
          </div>
          <div v-else class="match-card-list">
            <div
              v-for="match in filteredMatches"
              :key="match.uid"
              class="match-card"
              :class="{ 'completed': match.status === 'COMPLETED' }"
              @click="goToMatchDetail(match)"
            >
              <div class="match-card-badges">
                <span class="match-badge format-badge">{{ getMatchFormatLabel(match.matchFormat) }}</span>
                <span
                  class="match-badge type-badge"
                  :class="match.matchType === 'FRIENDLY' ? 'friendly' : 'free'"
                >
                  {{ match.matchType === 'FRIENDLY' ? '친선 경기' : '자체 경기' }}
                </span>
                <span class="match-badge size-badge">{{ match.matchFormat }}</span>
              </div>
              <div class="match-card-datetime">
                {{ match.matchDate }} ({{ match.matchDay }}) {{ match.matchTime }}
              </div>
              <div class="match-card-venue">
                {{ match.stadiumName }}
                <i class="el-icon-arrow-right"></i>
              </div>
              <div class="match-card-recruit">
                <span class="recruit-dot"></span>
                <span class="recruit-count">{{ match.currentPlayers || 0 }}/{{ match.maxPlayers || 0 }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- ========== 리그경기 Tab ========== -->
        <div v-show="activeTab === 'league'" class="team-tab-content league-tab">
          <div v-if="teamLeagues.length === 0" class="empty-state">
            <i class="el-icon-trophy"></i>
            <p>참가 중인 리그가 없습니다.</p>
          </div>
          <div v-else>
            <!-- League Selector -->
            <div v-if="teamLeagues.length > 1" class="league-selector-row">
              <el-select
                v-model="selectedLeagueUid"
                placeholder="리그 선택"
                size="small"
                :popper-append-to-body="false"
              >
                <el-option
                  v-for="league in teamLeagues"
                  :key="league.uid"
                  :label="league.name"
                  :value="league.uid"
                ></el-option>
              </el-select>
            </div>

            <!-- Standings Table -->
            <div class="league-standings" v-if="leagueStandings.length > 0">
              <div class="standings-table">
                <table>
                  <thead>
                    <tr>
                      <th class="rank-th"></th>
                      <th class="team-th"></th>
                      <th>경기</th>
                      <th>승점</th>
                      <th>승</th>
                      <th>무</th>
                      <th>패</th>
                      <th>득</th>
                      <th>실</th>
                      <th>차</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr
                      v-for="(standing, idx) in leagueStandings"
                      :key="idx"
                      :class="{ 'my-team-row': standing.teamUid === teamUid }"
                    >
                      <td class="rank-cell">{{ standing.ranking || (idx + 1) }}</td>
                      <td class="team-name-cell">
                        <img
                          :src="standing.teamLogoUrl || defaultLogo"
                          class="standings-team-logo"
                          :alt="standing.teamName"
                        >
                        <span>{{ standing.teamName }}</span>
                      </td>
                      <td>{{ standing.played }}</td>
                      <td class="points-cell">{{ standing.points }}</td>
                      <td>{{ standing.wins }}</td>
                      <td>{{ standing.draws }}</td>
                      <td>{{ standing.losses }}</td>
                      <td>{{ standing.goalsFor }}</td>
                      <td>{{ standing.goalsAgainst }}</td>
                      <td>{{ standing.goalDifference }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <!-- Action Buttons -->
            <div class="league-action-buttons">
              <button class="league-action-btn" @click="goToLeagueSchedule">
                전체 일정
              </button>
              <button class="league-action-btn outline" @click="goToLeagueStatus">
                리그 현황
              </button>
              <button class="league-action-btn link" @click="goToTeamLeagueStatus">
                팀 리그 현황 보기
              </button>
            </div>

            <!-- 팀 리그 일정 -->
            <div class="league-schedule-section" v-if="leagueSchedule.length > 0">
              <h4 class="sub-section-title">팀 리그 일정</h4>
              <div
                v-for="(lm, idx) in leagueSchedule"
                :key="idx"
                class="league-match-card"
              >
                <div class="league-match-date">
                  {{ formatLeagueMatchDate(lm.matchDate) }} {{ lm.matchTime || '' }}
                </div>
                <div class="league-match-venue">
                  {{ lm.stadiumName || '' }}
                </div>
                <div class="league-match-teams">
                  <div class="lm-team">
                    <img
                      :src="lm.homeTeamLogoUrl || defaultLogo"
                      class="lm-team-logo"
                      :alt="lm.homeTeamName"
                    >
                    <span class="lm-team-name">{{ lm.homeTeamName }}</span>
                  </div>
                  <div class="lm-center">
                    <span v-if="lm.status === 'COMPLETED'" class="lm-score">
                      {{ lm.homeScore }} - {{ lm.awayScore }}
                    </span>
                    <span v-else class="lm-vs">VS</span>
                  </div>
                  <div class="lm-team">
                    <img
                      :src="lm.awayTeamLogoUrl || defaultLogo"
                      class="lm-team-logo"
                      :alt="lm.awayTeamName"
                    >
                    <span class="lm-team-name">{{ lm.awayTeamName }}</span>
                  </div>
                  <i class="el-icon-arrow-right lm-arrow"></i>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- ========== 회원정보 Tab ========== -->
        <div v-show="activeTab === 'members'" class="team-tab-content members-tab">
          <!-- 회원 통계 -->
          <div class="members-stats">
            <p class="stat-line">
              <span class="stat-bullet">·</span>
              전체 회원수 : {{ members.length }}명
              (남자 {{ memberStats.male }}명 여자/ {{ memberStats.female }}명)
            </p>
            <p class="stat-line">
              <span class="stat-bullet">·</span>
              평균 연령대 : {{ memberStats.avgAge }}세
            </p>
          </div>

          <div v-if="members.length === 0" class="empty-state">
            <i class="el-icon-user"></i>
            <p>팀원이 없습니다.</p>
          </div>
          <div v-else class="members-list">
            <div
              v-for="member in sortedMembers"
              :key="member.uid"
              class="member-item"
              @click="goToMemberDetail(member)"
            >
              <img
                :src="member.profileImageUrl || defaultAvatar"
                :alt="member.userName"
                class="member-avatar"
              >
              <div class="member-info">
                <div class="member-name-row">
                  <span
                    v-if="member.role === 'OWNER'"
                    class="member-role-badge role-owner"
                  >운영자</span>
                  <span
                    v-else
                    class="member-role-badge role-member"
                  >구단원</span>
                  <span class="member-name">{{ member.userName }}</span>
                </div>
              </div>
              <div class="member-go-arrow" @click.stop="goToMemberDetail(member)">
                <span class="go-arrow-icon">»</span>
              </div>
            </div>
          </div>
        </div>

        <!-- ========== 후원내역 Tab ========== -->
        <div v-show="activeTab === 'sponsorship'" class="team-tab-content sponsorship-tab">
          <!-- 배너 슬라이더 -->
          <div v-if="teamBanners.length > 0" class="sponsor-banner-area">
            <VueSlickCarousel v-bind="bannerSlickSettings">
              <div v-for="banner in teamBanners" :key="banner.uid" class="sponsor-banner-slide">
                <a :href="banner.linkUrl || '#'" class="banner-link">
                  <img
                    :src="banner.imageUrl"
                    :alt="banner.title"
                    class="sponsor-banner-img"
                  >
                </a>
              </div>
            </VueSlickCarousel>
          </div>
          <div v-else class="sponsor-banner-placeholder">
            <div class="banner-placeholder-inner">
              <i class="el-icon-picture-outline"></i>
              <p>배너 이미지가 없습니다</p>
            </div>
          </div>

          <!-- 후원 멤버 리스트 -->
          <div v-if="sponsorships.length === 0" class="empty-state">
            <i class="el-icon-present"></i>
            <p>후원 내역이 없습니다.</p>
          </div>
          <div v-else class="sponsor-member-list">
            <div
              v-for="sp in sponsorships"
              :key="sp.uid"
              class="sponsor-member-item"
            >
              <img
                :src="sp.sponsorProfileUrl || defaultAvatar"
                :alt="sp.sponsorName"
                class="sponsor-avatar"
              >
              <div class="sponsor-member-info">
                <span
                  class="sponsor-role-badge"
                  :class="getSponsorRoleBadgeClass(sp.sponsorshipType)"
                >{{ getSponsorTypeLabel(sp.sponsorshipType) }}</span>
                <span class="sponsor-member-name">{{ sp.sponsorName || '익명' }}</span>
              </div>
              <div class="sponsor-go-arrow" @click="goToSponsorDetail(sp)">
                <span class="go-arrow-icon">»</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- FAB + Button (Owner only) -->
    <div v-if="isOwner" class="team-fab" @click="showActionModal = true">
      <i class="el-icon-plus"></i>
    </div>

    <!-- Action Modal (Bottom Sheet) -->
    <transition name="fade">
      <div v-if="showActionModal" class="action-modal-overlay" @click.self="showActionModal = false">
        <transition name="slide-up">
          <div v-if="showActionModal" class="action-modal-sheet">
            <div class="action-modal-header">
              <span class="action-modal-title">무엇을 하시겠습니까?</span>
              <i class="el-icon-close action-modal-close" @click="showActionModal = false"></i>
            </div>
            <div class="action-modal-body">
              <button class="action-modal-btn" @click="handleAction('match')">
                매치 일정 만들기
              </button>
              <button class="action-modal-btn" @click="handleAction('guest')">
                게스트 모집하기
              </button>
              <button class="action-modal-btn" @click="handleAction('recruit')">
                신규 회원 모집하기
              </button>
              <button class="action-modal-btn" @click="handleAction('invite')">
                초대 링크 복사하기
              </button>
            </div>
          </div>
        </transition>
      </div>
    </transition>

    <!-- 글쓰기 모달 -->
    <transition name="fade">
      <div v-if="showWriteModal" class="write-modal-overlay" @click.self="closeWriteModal">
        <transition name="slide-up">
          <div v-if="showWriteModal" class="write-modal-sheet">
            <div class="write-modal-header">
              <span class="write-modal-title">{{ editingPost ? '게시글 수정' : '게시글 작성' }}</span>
              <i class="el-icon-close write-modal-close" @click="closeWriteModal"></i>
            </div>
            <div class="write-modal-body">
              <div class="write-field">
                <input
                  v-model="writeForm.title"
                  type="text"
                  placeholder="제목을 입력하세요"
                  class="write-input"
                >
              </div>
              <div class="write-field">
                <textarea
                  v-model="writeForm.content"
                  placeholder="내용을 입력하세요"
                  class="write-textarea"
                  rows="6"
                ></textarea>
              </div>
              <div class="write-image-field">
                <div v-if="writeForm.imagePreview" class="write-image-preview">
                  <img :src="writeForm.imagePreview" alt="미리보기">
                  <span class="remove-image" @click="removeImage">
                    <i class="el-icon-close"></i>
                  </span>
                </div>
                <label v-else class="write-image-upload">
                  <i class="el-icon-picture-outline"></i>
                  <span>이미지 첨부 (1장)</span>
                  <input
                    type="file"
                    accept="image/*"
                    style="display:none"
                    @change="handleImageUpload"
                  >
                </label>
              </div>
              <div v-if="isOwner" class="write-notice-field">
                <label class="notice-checkbox">
                  <input v-model="writeForm.isNotice" type="checkbox">
                  <span>공지사항으로 등록</span>
                </label>
              </div>
            </div>
            <div class="write-modal-footer">
              <button class="write-cancel-btn" @click="closeWriteModal">취소</button>
              <button
                class="write-submit-btn"
                :disabled="!writeForm.title"
                @click="submitWriteForm"
              >
                {{ editingPost ? '수정' : '등록' }}
              </button>
            </div>
          </div>
        </transition>
      </div>
    </transition>
  </div>
</template>

<script lang="ts">
import {
  Vue, Component, Watch, Prop,
} from 'vue-property-decorator';
import {
  getTeamDetail, getTeamMembers, getTeamByCode,
  checkMembershipStatus, getMyTeams,
} from '@/api/team';
import { getMyTeamMatches } from '@/api/match';
import {
  getLeaguesByTeam, getLeagueStandings, getLeagueSchedule,
} from '@/api/league';
import {
  getTeamSponsorships,
  getTeamSponsorshipSummary,
} from '@/api/cash';
import { getActiveBanners } from '@/api/banner';
import {
  getTeamPosts, addTeamPost, updateTeamPost,
  deleteTeamPost, likeTeamPost,
} from '@/api/teamPost';
import {
  getComments, addComment, deleteComment,
} from '@/api/comment';
import { uploadFile } from '@/api/attachedFile';

// eslint-disable-next-line no-unused-vars
import VueSlickCarousel from 'vue-slick-carousel';
import 'vue-slick-carousel/dist/vue-slick-carousel.css';
import 'vue-slick-carousel/dist/vue-slick-carousel-theme.css';

interface TabItem {
  key: string
  label: string
}

interface WriteForm {
  title: string
  content: string
  imagePreview: string
  imageFileUid: string
  isNotice: boolean
}

interface MemberStats {
  total: number
  male: number
  female: number
  avgAge: number
}

@Component({
  components: {
    VueSlickCarousel,
  },
})
export default class TeamPage extends Vue {
  @Prop({ default: '' }) private selectedRegion!: string

  // Layout state
  private isExpanded = false

  private isDragging = false

  private dragStartY = 0

  private dragStartTop = 0

  private sectionTop: number | null = null

  private collapsedTop = 280

  private expandedTop = 70

  // Team state
  private teamUid = ''

  private activeTab = 'community'

  private matchFilter = 'ALL'

  private selectedLeagueUid = ''

  private teamInfo: any = {}

  private communityPosts: any[] = []

  private matchData: any[] = []

  private teamLeagues: any[] = []

  private leagueStandings: any[] = []

  private leagueSchedule: any[] = []

  private members: any[] = []

  private sponsorships: any[] = []

  private sponsorshipSummary: any = null

  private ownerSponsorName = ''

  private isLoading = false

  private showActionModal = false

  private myRole = ''

  // Community state
  private expandedPostUid = ''

  private postComments: Record<string, any[]> = {}

  private commentTexts: Record<string, string> = {}

  private showWriteModal = false

  private editingPost: any = null

  private writeForm: WriteForm = {
    title: '',
    content: '',
    imagePreview: '',
    imageFileUid: '',
    isNotice: false,
  }

  // Members state
  private memberStats: MemberStats = {
    total: 0,
    male: 0,
    female: 0,
    avgAge: 0,
  }

  // Sponsorship state
  private teamBanners: any[] = []

  private bannerSlickSettings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    autoplaySpeed: 3000,
    arrows: false,
  }

  private tabs: TabItem[] = [
    { key: 'community', label: '커뮤니티' },
    { key: 'match', label: '매치경기' },
    { key: 'league', label: '리그경기' },
    { key: 'members', label: '회원정보' },
    { key: 'sponsorship', label: '후원내역' },
  ]

  // ===== Computed =====
  get defaultLogo(): string {
    return 'https://ui-avatars.com/api/?name=T&background=061da1&color=fff&size=60';
  }

  get defaultAvatar(): string {
    return 'https://ui-avatars.com/api/?name=U&background=ccc&color=fff&size=40';
  }

  get isOwner(): boolean {
    return this.myRole === 'OWNER';
  }

  get myRoleLabel(): string {
    switch (this.myRole) {
      case 'OWNER': return '운영자';
      case 'MANAGER': return '구단주';
      case 'MEMBER': return '구단원';
      default: return '운영자';
    }
  }

  get isMember(): boolean {
    return this.myRole === 'MEMBER' || this.myRole === 'MANAGER';
  }

  get filteredMatches(): any[] {
    if (this.matchFilter === 'ALL') {
      return this.matchData;
    }
    return this.matchData.filter((m) => m.matchType === this.matchFilter);
  }

  get noticePosts(): any[] {
    return this.communityPosts.filter((p) => p.noticeStatus);
  }

  get regularPosts(): any[] {
    return this.communityPosts.filter((p) => !p.noticeStatus);
  }

  get sortedMembers(): any[] {
    const roleOrder: Record<string, number> = {
      OWNER: 0,
      MANAGER: 1,
      MEMBER: 2,
    };
    return [...this.members].sort((a, b) => (roleOrder[a.role] || 2) - (roleOrder[b.role] || 2));
  }

  get sectionStyle(): Record<string, string> {
    if (this.sectionTop !== null) {
      return {
        top: `${this.sectionTop}px`,
        transition: this.isDragging ? 'none' : 'top 0.3s ease',
      };
    }
    return {};
  }

  // ===== Lifecycle =====
  async created() {
    const teamCode = this.$route.params.teamCode
      || this.$route.query.teamCode as string || '';
    if (teamCode) {
      try {
        const res = await getTeamByCode(teamCode);
        if (res.data && res.data.uid) {
          this.teamUid = res.data.uid;
        }
      } catch (e) {
        console.error('팀 코드로 조회 실패:', e);
        this.$message.error('존재하지 않는 팀입니다.');
        return;
      }
    } else {
      try {
        const res = await getMyTeams();
        if (res.data && res.data.uid) {
          this.teamUid = res.data.uid;
        }
      } catch (e) {
        console.warn('내 팀 조회 실패:', e);
      }
    }
    if (this.teamUid) {
      await this.loadTeamInfo();
    }
  }

  // ===== Watchers =====
  @Watch('activeTab')
  async onTabChange() {
    await this.loadTabData();
  }

  @Watch('selectedLeagueUid')
  async onLeagueChange() {
    if (this.selectedLeagueUid) {
      await this.loadLeagueData();
    }
  }

  // ===== Data Loading =====
  private async loadTeamInfo(): Promise<void> {
    this.isLoading = true;
    try {
      const res = await getTeamDetail(this.teamUid);
      this.teamInfo = res.data || {};
      await Promise.all([
        this.loadOwnerSponsor(),
        this.loadMyRole(),
      ]);
      await this.loadTabData();
    } catch (error) {
      console.error('팀 정보 로드 실패:', error);
    } finally {
      this.isLoading = false;
    }
  }

  private async loadMyRole(): Promise<void> {
    try {
      const statusRes = await checkMembershipStatus();
      const status = statusRes.data;
      if (status && status.hasCreatedTeam) {
        const myTeamsRes = await getMyTeams();
        const myTeam = Array.isArray(myTeamsRes.data)
          ? myTeamsRes.data[0] : myTeamsRes.data;
        if (myTeam && myTeam.uid === this.teamUid) {
          this.myRole = 'OWNER';
          return;
        }
      }
      if (status && status.hasTeam) {
        const myTeamsRes = await getMyTeams();
        const myTeam = Array.isArray(myTeamsRes.data)
          ? myTeamsRes.data[0] : myTeamsRes.data;
        if (myTeam && myTeam.uid === this.teamUid) {
          this.myRole = 'MEMBER';
        }
      }
    } catch (e) {
      console.warn('내 역할 확인 실패:', e);
    }
  }

  private async loadOwnerSponsor(): Promise<void> {
    try {
      const res = await getTeamSponsorshipSummary(this.teamUid);
      const summary = res.data;
      if (summary && summary.ownerName) {
        this.ownerSponsorName = summary.ownerName;
      }
    } catch (e) {
      // 구단주 정보 없을 수 있음
    }
  }

  private async loadTabData(): Promise<void> {
    switch (this.activeTab) {
      case 'community':
        await this.loadCommunityPosts();
        break;
      case 'match':
        await this.loadMatchData();
        break;
      case 'league':
        await this.loadLeagues();
        break;
      case 'members':
        await this.loadMembers();
        break;
      case 'sponsorship':
        await this.loadSponsorships();
        break;
      default:
        break;
    }
  }

  // ===== Community Methods =====
  private async loadCommunityPosts(): Promise<void> {
    try {
      const res = await getTeamPosts(this.teamUid, {
        page: 1,
        size: 50,
      });
      const posts = res.data?.content || res.data || [];
      this.communityPosts = Array.isArray(posts) ? posts : [];
    } catch (error) {
      console.warn('커뮤니티 로드 실패:', error);
      this.communityPosts = [];
    }
  }

  private async togglePostExpand(postUid: string): Promise<void> {
    if (this.expandedPostUid === postUid) {
      this.expandedPostUid = '';
      return;
    }
    this.expandedPostUid = postUid;
    if (!this.commentTexts[postUid]) {
      this.$set(this.commentTexts, postUid, '');
    }
    await this.loadPostComments(postUid);
  }

  private async loadPostComments(postUid: string): Promise<void> {
    try {
      const res = await getComments(postUid);
      const comments = res.data?.content || res.data || [];
      this.$set(this.postComments, postUid, Array.isArray(comments) ? comments : []);
    } catch (e) {
      this.$set(this.postComments, postUid, []);
    }
  }

  private async toggleLike(post: any): Promise<void> {
    try {
      await likeTeamPost(post.uid);
      if (post.likeStatus) {
        post.likeCount = (post.likeCount || 1) - 1;
        post.likeStatus = false;
      } else {
        post.likeCount = (post.likeCount || 0) + 1;
        post.likeStatus = true;
      }
    } catch (e) {
      this.$message.error('좋아요 처리에 실패했습니다.');
    }
  }

  private async submitComment(postUid: string): Promise<void> {
    const text = this.commentTexts[postUid];
    if (!text || !text.trim()) return;
    try {
      await addComment({
        postUid,
        contents: text.trim(),
      });
      this.$set(this.commentTexts, postUid, '');
      await this.loadPostComments(postUid);
      // 댓글 카운트 업데이트
      const post = this.communityPosts.find((p) => p.uid === postUid);
      if (post) {
        post.commentCount = (post.commentCount || 0) + 1;
      }
    } catch (e) {
      this.$message.error('댓글 작성에 실패했습니다.');
    }
  }

  private async confirmDeleteComment(comment: any): Promise<void> {
    try {
      await this.$confirm('댓글을 삭제하시겠습니까?', '댓글 삭제', {
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
        type: 'warning',
      });
      await deleteComment(comment.uid);
      if (this.expandedPostUid) {
        await this.loadPostComments(this.expandedPostUid);
      }
      this.$message.success('댓글이 삭제되었습니다.');
    } catch (e) {
      // 취소
    }
  }

  private getPostImage(post: any): string {
    if (post.fileList && post.fileList.length > 0) {
      const file = post.fileList[0];
      if (file.fileUrl) return file.fileUrl;
      if (file.fileUid) {
        return `${process.env.VUE_APP_COMMON_API || ''}/attached-file/${file.fileUid}`;
      }
    }
    return '';
  }

  private openWriteModal(): void {
    this.editingPost = null;
    this.writeForm = {
      title: '',
      content: '',
      imagePreview: '',
      imageFileUid: '',
      isNotice: false,
    };
    this.showWriteModal = true;
  }

  private editPost(post: any): void {
    this.editingPost = post;
    this.writeForm = {
      title: post.title || '',
      content: post.content || '',
      imagePreview: this.getPostImage(post),
      imageFileUid: post.fileList?.[0]?.fileUid || '',
      isNotice: post.noticeStatus || false,
    };
    this.showWriteModal = true;
  }

  private closeWriteModal(): void {
    this.showWriteModal = false;
    this.editingPost = null;
  }

  private async handleImageUpload(event: Event): Promise<void> {
    const input = event.target as HTMLInputElement;
    if (!input.files || input.files.length === 0) return;
    const file = input.files[0];
    // 미리보기
    const reader = new FileReader();
    reader.onload = (e) => {
      this.writeForm.imagePreview = (e.target?.result as string) || '';
    };
    reader.readAsDataURL(file);
    // 파일 업로드
    try {
      const formData = new FormData();
      formData.append('file', file);
      const res = await uploadFile('/attached-file', formData);
      if (res.data && res.data.uid) {
        this.writeForm.imageFileUid = res.data.uid;
      }
    } catch (e) {
      this.$message.error('이미지 업로드에 실패했습니다.');
    }
  }

  private removeImage(): void {
    this.writeForm.imagePreview = '';
    this.writeForm.imageFileUid = '';
  }

  private async submitWriteForm(): Promise<void> {
    if (!this.writeForm.title.trim()) {
      this.$message.warning('제목을 입력하세요.');
      return;
    }
    try {
      const fileList = this.writeForm.imageFileUid
        ? [{ fileUid: this.writeForm.imageFileUid }] : [];
      if (this.editingPost) {
        await updateTeamPost(this.editingPost.uid, {
          title: this.writeForm.title,
          content: this.writeForm.content,
          noticeStatus: this.writeForm.isNotice,
          fileList,
        });
        this.$message.success('게시글이 수정되었습니다.');
      } else {
        await addTeamPost({
          teamUid: this.teamUid,
          title: this.writeForm.title,
          content: this.writeForm.content,
          noticeStatus: this.writeForm.isNotice,
          fileList,
        });
        this.$message.success('게시글이 등록되었습니다.');
      }
      this.closeWriteModal();
      await this.loadCommunityPosts();
    } catch (e) {
      this.$message.error('게시글 처리에 실패했습니다.');
    }
  }

  private async confirmDeletePost(post: any): Promise<void> {
    try {
      await this.$confirm('게시글을 삭제하시겠습니까?', '게시글 삭제', {
        confirmButtonText: '삭제',
        cancelButtonText: '취소',
        type: 'warning',
      });
      await deleteTeamPost(post.uid);
      this.$message.success('게시글이 삭제되었습니다.');
      await this.loadCommunityPosts();
    } catch (e) {
      // 취소
    }
  }

  // ===== Match Methods =====
  private async loadMatchData(): Promise<void> {
    try {
      const res = await getMyTeamMatches(this.teamUid, {});
      const matches = res.data?.content || res.data || [];
      const dayNames = ['일', '월', '화', '수', '목', '금', '토'];

      this.matchData = matches.map((m: any) => {
        const matchDate = new Date(m.matchDate);
        const maxPlayers = this.getMaxPlayers(m.matchFormat);
        return {
          uid: m.uid,
          matchDate: this.formatMatchDate(m.matchDate),
          matchDay: dayNames[matchDate.getDay()],
          matchTime: `Pm ${this.formatTime(m.matchTime)}`,
          matchType: m.matchType,
          matchFormat: m.matchFormat || '',
          stadiumName: m.stadiumName || '',
          status: m.status,
          homeName: m.hostTeamName || this.teamInfo.name || '',
          homeLogo: m.hostTeamLogoUrl || this.teamInfo.logoUrl || this.defaultLogo,
          homeScore: m.homeScore,
          awayName: m.opponentTeamName || m.guestTeamName || '-',
          awayLogo: m.guestTeamLogoUrl || this.defaultLogo,
          awayScore: m.awayScore,
          maxPlayers,
          currentPlayers: m.currentPlayers || m.applicationCount || 0,
        };
      });
    } catch (error) {
      console.warn('매치 데이터 로드 실패:', error);
      this.matchData = [];
    }
  }

  private getMaxPlayers(format: string): number {
    const formatMap: Record<string, number> = {
      '4vs4': 8,
      '5vs5': 10,
      '6vs6': 12,
      '7vs7': 14,
      FOUR_VS_FOUR: 8,
      FIVE_VS_FIVE: 10,
      SIX_VS_SIX: 12,
      SEVEN_VS_SEVEN: 14,
    };
    return formatMap[format] || 10;
  }

  private getMatchFormatLabel(format: string): string {
    const labelMap: Record<string, string> = {
      '4vs4': '매치4:4전',
      '5vs5': '매치5:5전',
      '6vs6': '매치6:6전',
      '7vs7': '매치7:7전',
      FOUR_VS_FOUR: '매치4:4전',
      FIVE_VS_FIVE: '매치5:5전',
      SIX_VS_SIX: '매치6:6전',
      SEVEN_VS_SEVEN: '매치7:7전',
    };
    return labelMap[format] || format;
  }

  private goToMatchDetail(match: any): void {
    this.$router.push({
      path: `/match-detail/${match.uid}`,
      query: { type: 'match' },
    });
  }

  // ===== League Methods =====
  private async loadLeagues(): Promise<void> {
    try {
      const res = await getLeaguesByTeam(this.teamUid);
      this.teamLeagues = res.data || [];
      if (this.teamLeagues.length > 0 && !this.selectedLeagueUid) {
        this.selectedLeagueUid = this.teamLeagues[0].uid;
        await this.loadLeagueData();
      }
    } catch (error) {
      console.warn('리그 정보 로드 실패:', error);
      this.teamLeagues = [];
    }
  }

  private async loadLeagueData(): Promise<void> {
    if (!this.selectedLeagueUid) return;
    try {
      const [standingsRes, scheduleRes] = await Promise.all([
        getLeagueStandings(this.selectedLeagueUid),
        getLeagueSchedule(this.selectedLeagueUid, {}),
      ]);
      this.leagueStandings = standingsRes.data || [];
      this.leagueSchedule = scheduleRes.data || [];
    } catch (error) {
      console.warn('리그 데이터 로드 실패:', error);
    }
  }

  private goToLeagueSchedule(): void {
    this.$router.push({
      path: '/league-schedule',
      query: { leagueUid: this.selectedLeagueUid },
    });
  }

  private goToLeagueStatus(): void {
    this.$router.push({
      path: '/league-status',
      query: { leagueUid: this.selectedLeagueUid },
    });
  }

  private goToTeamLeagueStatus(): void {
    this.$router.push({
      path: '/league-status',
      query: {
        leagueUid: this.selectedLeagueUid,
        teamUid: this.teamUid,
      },
    });
  }

  // ===== Members Methods =====
  private async loadMembers(): Promise<void> {
    try {
      const res = await getTeamMembers(this.teamUid);
      this.members = res.data || [];
      this.computeMemberStats();
    } catch (error) {
      console.warn('회원 정보 로드 실패:', error);
      this.members = [];
    }
  }

  private computeMemberStats(): void {
    const total = this.members.length;
    let male = 0;
    let female = 0;
    let totalAge = 0;
    let ageCount = 0;
    const now = new Date();
    const currentYear = now.getFullYear();

    this.members.forEach((m: any) => {
      if (m.gender === 0 || m.userGender === 0) male += 1;
      else if (m.gender === 1 || m.userGender === 1) female += 1;

      const birth = m.birth || m.userBirth;
      if (birth) {
        const birthYear = new Date(birth).getFullYear();
        const age = currentYear - birthYear;
        if (age > 0 && age < 100) {
          totalAge += age;
          ageCount += 1;
        }
      }
    });

    this.memberStats = {
      total,
      male,
      female,
      avgAge: ageCount > 0 ? Math.round((totalAge / ageCount) * 10) / 10 : 0,
    };
  }

  private goToMemberDetail(member: any): void {
    this.$router.push({
      path: `/member-detail/${member.uid}`,
      query: { teamUid: this.teamUid },
    });
  }

  // ===== Sponsorship Methods =====
  private async loadSponsorships(): Promise<void> {
    try {
      const [spRes, bannerRes] = await Promise.all([
        getTeamSponsorships(this.teamUid),
        getActiveBanners({ targetPage: 'TEAM' }),
      ]);
      this.sponsorships = spRes.data || [];
      this.teamBanners = bannerRes.data || [];
    } catch (error) {
      console.warn('후원 내역 로드 실패:', error);
      this.sponsorships = [];
    }
  }

  private getSponsorTypeLabel(type: string): string {
    const typeMap: Record<string, string> = {
      OWNER: '구단주',
      REGULAR: '정기',
      ONE_TIME: '1회',
    };
    return typeMap[type] || '후원';
  }

  private getSponsorRoleBadgeClass(type: string): string {
    const classMap: Record<string, string> = {
      OWNER: 'badge-owner',
      REGULAR: 'badge-regular',
      ONE_TIME: 'badge-onetime',
    };
    return classMap[type] || 'badge-onetime';
  }

  private goToSponsorDetail(sp: any): void {
    if (sp.sponsorMemberUid) {
      this.$router.push({
        path: `/member-detail/${sp.sponsorMemberUid}`,
        query: { teamUid: this.teamUid },
      });
    }
  }

  // ===== Navigation =====
  private goToDashboard(): void {
    this.$message.info('대시보드 기능은 준비 중입니다.');
  }

  private goToTeamSettings(): void {
    this.$message.info('팀 설정 기능은 준비 중입니다.');
  }

  private showOwnerInfo(): void {
    this.$message.info('구단주 정보 기능은 준비 중입니다.');
  }

  private handleAction(action: string): void {
    this.showActionModal = false;
    switch (action) {
      case 'match':
        this.$router.push({
          path: '/match-create',
          query: { teamUid: this.teamUid },
        });
        break;
      case 'guest':
        this.$router.push({
          path: '/guest-recruit',
          query: { teamUid: this.teamUid },
        });
        break;
      case 'recruit':
        this.$message.info('신규 회원 모집하기 기능은 준비 중입니다.');
        break;
      case 'invite':
        this.copyInviteLink();
        break;
      default:
        break;
    }
  }

  private copyInviteLink(): void {
    const teamCode = this.teamInfo.teamCode || '';
    const link = `${window.location.origin}/team/${teamCode}`;
    navigator.clipboard.writeText(link).then(() => {
      this.$message.success('초대 링크가 복사되었습니다.');
    }).catch(() => {
      this.$message.error('링크 복사에 실패했습니다.');
    });
  }

  // ===== Drag Handlers =====
  private onDragStart(e: TouchEvent | MouseEvent): void {
    this.isDragging = true;
    this.dragStartY = 'touches' in e ? e.touches[0].clientY : e.clientY;
    const el = (e.target as HTMLElement).closest('.league-section') as HTMLElement;
    if (el) {
      this.dragStartTop = el.getBoundingClientRect().top;
    }
    document.addEventListener('touchmove', this.onDragMove, { passive: false });
    document.addEventListener('mousemove', this.onDragMove);
    document.addEventListener('touchend', this.onDragEnd);
    document.addEventListener('mouseup', this.onDragEnd);
  }

  private onDragMove(e: TouchEvent | MouseEvent): void {
    if (!this.isDragging) return;
    if ('cancelable' in e && e.cancelable) e.preventDefault();
    const clientY = 'touches' in e
      ? (e as TouchEvent).touches[0].clientY : (e as MouseEvent).clientY;
    const delta = clientY - this.dragStartY;
    let newTop = this.dragStartTop + delta;
    newTop = Math.max(this.expandedTop, Math.min(newTop, this.collapsedTop));
    this.sectionTop = newTop;
  }

  private onDragEnd(): void {
    this.isDragging = false;
    document.removeEventListener('touchmove', this.onDragMove);
    document.removeEventListener('mousemove', this.onDragMove);
    document.removeEventListener('touchend', this.onDragEnd);
    document.removeEventListener('mouseup', this.onDragEnd);
    if (this.sectionTop === null) return;
    const mid = (this.collapsedTop + this.expandedTop) / 2;
    if (this.sectionTop < mid) {
      this.sectionTop = this.expandedTop;
      this.isExpanded = true;
    } else {
      this.sectionTop = this.collapsedTop;
      this.isExpanded = false;
    }
  }

  // ===== Formatters =====
  private formatMatchDate(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${month}월 ${day}일`;
  }

  private formatLeagueMatchDate(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const dayNames = ['일요일', '월요일', '화요일', '수요일', '목요일', '금요일', '토요일'];
    const dayName = dayNames[date.getDay()];
    return `${month}월 ${day}일 (${dayName})`;
  }

  private formatTime(time: string): string {
    if (!time) return '';
    const parts = time.split(':');
    if (parts.length < 2) return time;
    return `${parts[0]}:${parts[1]}`;
  }

  private formatDateTime(dateStr: string): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const y = date.getFullYear();
    const m = String(date.getMonth() + 1).padStart(2, '0');
    const d = String(date.getDate()).padStart(2, '0');
    return `${y}.${m}.${d}`;
  }

  private formatAmount(amount: number | null | undefined): string {
    if (!amount) return '0원';
    return `${amount.toLocaleString()}원`;
  }
}
</script>

<style scoped>
/* ===== Team Profile Section ===== */
.team-profile-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 16px 0;
  text-align: center;
}

.team-profile-logo-wrap {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 10px;
  border: 3px solid rgba(255, 255, 255, 0.4);
  overflow: hidden;
}

.team-profile-logo {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
}

.team-profile-name {
  color: #fff;
  font-size: 20px;
  font-weight: 800;
  margin: 0 0 2px;
  letter-spacing: -0.3px;
}

.team-profile-owner {
  color: rgba(255, 255, 255, 0.85);
  font-size: 13px;
  margin: 0 0 14px;
  font-weight: 400;
}

.team-profile-buttons {
  display: flex;
  gap: 10px;
}

.team-btn {
  padding: 8px 20px;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
  border: none;
  transition: all 0.2s ease;
  letter-spacing: -0.2px;
}

.team-btn-outline {
  background: #fff;
  color: #333;
  border: 1.5px solid #fff;
}

.team-btn-outline:active {
  background: #f0f0f0;
}

.team-btn-primary {
  background: #f7c600;
  color: #fff;
  border: 1.5px solid #f7c600;
}

.team-btn-primary:active {
  background: #e0b400;
}

/* ===== Owner Bar ===== */
.team-owner-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 0 16px;
  padding: 12px 14px;
  border-bottom: 1px solid #eee;
}

.owner-bar-text {
  color: #333;
  font-size: 13px;
  font-weight: 500;
}

.owner-bar-link {
  color: #f7c600;
  font-size: 12px;
  font-weight: 700;
  cursor: pointer;
  padding: 4px 10px;
  background: #fff8e1;
  border-radius: 12px;
}

.owner-bar-link:active {
  opacity: 0.7;
}

/* ===== Tab Nav ===== */
.team-tab-nav {
  display: flex;
  border-bottom: 1px solid #eee;
  padding: 0 16px;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none;
}

.team-tab-nav::-webkit-scrollbar {
  display: none;
}

.team-tab-item {
  flex-shrink: 0;
  padding: 12px 14px;
  font-size: 14px;
  color: #999;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.team-tab-item.active {
  color: #061da1;
  font-weight: 700;
  border-bottom-color: #061da1;
}

/* ===== Tab Content Container ===== */
.team-tab-content {
  padding: 16px;
  min-height: 300px;
}

/* ===== Empty State ===== */
.empty-state {
  text-align: center;
  padding: 60px 20px;
  color: #bbb;
}

.empty-state i {
  font-size: 40px;
  margin-bottom: 12px;
  display: block;
}

.empty-state p {
  font-size: 14px;
  margin: 0;
}

/* ========================================
   커뮤니티 Tab
   ======================================== */

/* 공지사항 카드 */
.notice-card {
  background: #fff;
  border-left: 4px solid #061da1;
  border-radius: 8px;
  padding: 14px 16px;
  margin-bottom: 16px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
}

.notice-card-header {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 8px;
}

.notice-icon {
  font-size: 16px;
}

.notice-title {
  font-size: 14px;
  font-weight: 700;
  color: #333;
}

.notice-content {
  cursor: pointer;
}

.notice-text {
  font-size: 13px;
  color: #555;
  margin: 0;
  line-height: 1.5;
}

/* 게시글 피드 */
.community-feed {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.community-post-card {
  background: #fff;
  border-radius: 12px;
  border: 1px solid #f0f0f0;
  overflow: hidden;
  transition: box-shadow 0.2s ease;
}

.community-post-card.expanded {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 14px 16px 0;
  cursor: pointer;
}

.post-writer-info {
  display: flex;
  align-items: center;
  gap: 6px;
}

.post-writer-dot {
  color: #4CAF50;
  font-size: 10px;
}

.post-writer-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.post-date {
  font-size: 12px;
  color: #bbb;
}

.post-body {
  padding: 8px 16px;
  cursor: pointer;
}

.post-content-text {
  font-size: 14px;
  color: #333;
  font-weight: 500;
  margin: 0 0 4px;
  line-height: 1.5;
}

.post-content-desc {
  font-size: 13px;
  color: #666;
  margin: 0 0 10px;
  line-height: 1.5;
}

.post-image-wrap {
  border-radius: 10px;
  overflow: hidden;
  margin-top: 8px;
}

.post-image {
  width: 100%;
  max-height: 300px;
  object-fit: cover;
  display: block;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 16px 14px;
}

.post-reactions {
  display: flex;
  gap: 16px;
}

.reaction-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #999;
  cursor: pointer;
}

.reaction-item.like-btn:active {
  transform: scale(0.95);
}

.reaction-item.liked {
  color: #ff4757;
}

.reaction-item.liked i {
  color: #ff4757;
}

.post-actions {
  display: flex;
  gap: 10px;
}

.post-action-btn {
  font-size: 12px;
  color: #999;
  cursor: pointer;
  padding: 2px 6px;
}

.post-action-btn:active {
  color: #666;
}

.post-action-btn.delete {
  color: #ff4757;
}

/* 댓글 영역 */
.post-comments-section {
  border-top: 1px solid #f0f0f0;
  padding: 0 16px 14px;
}

.comments-divider {
  height: 1px;
  background: #f0f0f0;
  margin: 0 -16px 12px;
}

.comments-list {
  margin-bottom: 12px;
}

.comment-item {
  padding: 8px 0;
  border-bottom: 1px solid #f8f8f8;
  position: relative;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.comment-writer {
  font-size: 13px;
  font-weight: 600;
  color: #333;
}

.comment-date {
  font-size: 11px;
  color: #bbb;
}

.comment-text {
  font-size: 13px;
  color: #555;
  margin: 0;
  line-height: 1.4;
}

.comment-delete {
  position: absolute;
  top: 8px;
  right: 0;
  font-size: 11px;
  color: #ff4757;
  cursor: pointer;
}

.no-comments {
  text-align: center;
  padding: 16px 0;
  color: #ccc;
  font-size: 13px;
}

.comment-input-row {
  display: flex;
  gap: 8px;
  margin-top: 8px;
}

.comment-input {
  flex: 1;
  padding: 10px 14px;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  font-size: 13px;
  outline: none;
  transition: border-color 0.2s;
}

.comment-input:focus {
  border-color: #061da1;
}

.comment-submit-btn {
  padding: 8px 16px;
  border: none;
  border-radius: 20px;
  background: #061da1;
  color: #fff;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
}

.comment-submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

/* 글쓰기 버튼 */
.community-write-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  margin-top: 16px;
  padding: 12px;
  background: #f5f7ff;
  border: 1px dashed #061da1;
  border-radius: 10px;
  color: #061da1;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.community-write-btn:active {
  background: #e8ecff;
}

/* slide-fade transition */
.slide-fade-enter-active {
  transition: all 0.3s ease;
}

.slide-fade-leave-active {
  transition: all 0.2s ease;
}

.slide-fade-enter,
.slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* ========================================
   매치경기 Tab
   ======================================== */
.match-filter-row {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.match-filter-tag {
  padding: 6px 14px;
  border-radius: 20px;
  font-size: 13px;
  color: #666;
  background: #f5f5f5;
  cursor: pointer;
  transition: all 0.2s ease;
}

.match-filter-tag.active {
  background: #061da1;
  color: #fff;
}

.match-card-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.match-card {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 14px;
  padding: 16px;
  cursor: pointer;
  transition: box-shadow 0.2s;
}

.match-card:active {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.match-card.completed {
  opacity: 0.65;
}

.match-card-badges {
  display: flex;
  gap: 6px;
  margin-bottom: 10px;
  flex-wrap: wrap;
}

.match-badge {
  padding: 3px 10px;
  border-radius: 4px;
  font-size: 11px;
  font-weight: 700;
}

.match-badge.format-badge {
  background: #8B4513;
  color: #fff;
}

.match-badge.type-badge.friendly {
  background: #4CAF50;
  color: #fff;
}

.match-badge.type-badge.free {
  background: #2196F3;
  color: #fff;
}

.match-badge.size-badge {
  background: #3F51B5;
  color: #fff;
}

.match-card-datetime {
  font-size: 15px;
  font-weight: 700;
  color: #333;
  margin-bottom: 6px;
}

.match-card-venue {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #4CAF50;
  font-weight: 600;
  margin-bottom: 8px;
  cursor: pointer;
}

.match-card-venue i {
  font-size: 12px;
}

.match-card-recruit {
  display: flex;
  align-items: center;
  gap: 6px;
}

.recruit-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #4CAF50;
}

.recruit-count {
  font-size: 13px;
  font-weight: 700;
  color: #4CAF50;
}

/* ========================================
   리그경기 Tab
   ======================================== */
.league-selector-row {
  margin-bottom: 16px;
}

.league-standings {
  margin-bottom: 16px;
}

.standings-table {
  overflow-x: auto;
}

.standings-table table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}

.standings-table th {
  background: #f8f9fa;
  color: #666;
  font-weight: 600;
  padding: 8px 4px;
  text-align: center;
  white-space: nowrap;
  border-bottom: 2px solid #e0e0e0;
}

.standings-table td {
  padding: 10px 4px;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
  font-size: 12px;
}

.standings-table .rank-th {
  width: 30px;
}

.standings-table .team-th {
  text-align: left;
  min-width: 80px;
}

.standings-table .rank-cell {
  font-weight: 700;
  color: #333;
  font-size: 14px;
}

.standings-table .team-name-cell {
  text-align: left;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 6px;
}

.standings-team-logo {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  object-fit: cover;
}

.standings-table .points-cell {
  font-weight: 700;
  color: #f08717;
}

.my-team-row {
  background: #061da1 !important;
}

.my-team-row td {
  color: #fff !important;
  font-weight: 600;
}

.my-team-row .rank-cell {
  color: #fff !important;
}

.my-team-row .points-cell {
  color: #ffd700 !important;
}

/* 리그 액션 버튼 */
.league-action-buttons {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.league-action-btn {
  padding: 8px 16px;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  border: none;
  background: #061da1;
  color: #fff;
  transition: opacity 0.2s;
}

.league-action-btn:active {
  opacity: 0.8;
}

.league-action-btn.outline {
  background: #fff;
  color: #061da1;
  border: 1px solid #061da1;
}

.league-action-btn.link {
  background: transparent;
  color: #061da1;
  text-decoration: underline;
  padding: 8px 4px;
}

/* 팀 리그 일정 */
.league-schedule-section {
  margin-top: 4px;
}

.sub-section-title {
  font-size: 15px;
  font-weight: 700;
  color: #333;
  margin: 0 0 12px;
}

.league-match-card {
  background: #fff;
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 14px 16px;
  margin-bottom: 10px;
}

.league-match-date {
  font-size: 14px;
  font-weight: 700;
  color: #333;
  margin-bottom: 4px;
}

.league-match-venue {
  font-size: 12px;
  color: #4CAF50;
  font-weight: 600;
  margin-bottom: 12px;
}

.league-match-teams {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  position: relative;
}

.lm-team {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  flex: 1;
}

.lm-team-logo {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #e0e0e0;
}

.lm-team-name {
  font-size: 12px;
  font-weight: 600;
  color: #333;
  text-align: center;
}

.lm-center {
  flex-shrink: 0;
}

.lm-vs {
  font-size: 16px;
  font-weight: 800;
  color: #061da1;
}

.lm-score {
  font-size: 18px;
  font-weight: 800;
  color: #f08717;
}

.lm-arrow {
  position: absolute;
  right: 0;
  color: #ccc;
  font-size: 16px;
}

/* ========================================
   회원정보 Tab
   ======================================== */
.members-stats {
  background: #f8f9fb;
  border-radius: 10px;
  padding: 14px 16px;
  margin-bottom: 16px;
}

.stat-line {
  font-size: 13px;
  color: #555;
  margin: 0 0 4px;
  line-height: 1.6;
}

.stat-line:last-child {
  margin-bottom: 0;
}

.stat-bullet {
  color: #061da1;
  font-weight: 700;
  margin-right: 4px;
}

.members-list {
  display: flex;
  flex-direction: column;
}

.member-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
}

.member-item:last-child {
  border-bottom: none;
}

.member-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.member-info {
  flex: 1;
}

.member-name-row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.member-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.member-role-badge {
  padding: 2px 10px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 700;
}

.role-owner {
  background: #fff3e0;
  color: #f08717;
}

.role-member {
  background: #e8f5e9;
  color: #4CAF50;
}

.member-go-arrow {
  flex-shrink: 0;
  cursor: pointer;
  padding: 4px;
}

.go-arrow-icon {
  font-size: 20px;
  font-weight: 800;
  color: #f08717;
}

.role-member + .member-name ~ .member-go-arrow .go-arrow-icon {
  color: #4CAF50;
}

/* ========================================
   후원내역 Tab
   ======================================== */

/* 배너 슬라이더 */
.sponsor-banner-area {
  margin-bottom: 16px;
  border-radius: 12px;
  overflow: hidden;
}

.sponsor-banner-slide {
  outline: none;
}

.banner-link {
  display: block;
}

.sponsor-banner-img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-radius: 12px;
}

.sponsor-banner-placeholder {
  margin-bottom: 16px;
}

.banner-placeholder-inner {
  background: #f5f5f5;
  border-radius: 12px;
  height: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #ccc;
}

.banner-placeholder-inner i {
  font-size: 40px;
  margin-bottom: 8px;
}

.banner-placeholder-inner p {
  font-size: 13px;
  margin: 0;
}

/* 후원 멤버 리스트 */
.sponsor-member-list {
  display: flex;
  flex-direction: column;
}

.sponsor-member-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid #f0f0f0;
}

.sponsor-member-item:last-child {
  border-bottom: none;
}

.sponsor-avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  object-fit: cover;
  flex-shrink: 0;
}

.sponsor-member-info {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 8px;
}

.sponsor-role-badge {
  padding: 2px 10px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 700;
}

.badge-owner {
  background: #fff3e0;
  color: #f08717;
}

.badge-regular {
  background: #e8f4fd;
  color: #1890ff;
}

.badge-onetime {
  background: #e8f5e9;
  color: #4CAF50;
}

.sponsor-member-name {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.sponsor-go-arrow {
  flex-shrink: 0;
  cursor: pointer;
  padding: 4px;
}

/* ========================================
   FAB
   ======================================== */
.team-fab {
  position: fixed;
  bottom: 100px;
  right: 20px;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background: #061da1;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  box-shadow: 0 4px 14px rgba(6, 29, 161, 0.4);
  cursor: pointer;
  z-index: 100;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.team-fab:active {
  transform: scale(0.92);
  box-shadow: 0 2px 8px rgba(6, 29, 161, 0.3);
}

/* ========================================
   Action Modal
   ======================================== */
.action-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 200;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.action-modal-sheet {
  width: 100%;
  max-width: 480px;
  background: #fff;
  border-radius: 20px 20px 0 0;
  padding: 24px 20px calc(32px + 70px);
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.12);
}

.action-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.action-modal-title {
  font-size: 18px;
  font-weight: 700;
  color: #333;
}

.action-modal-close {
  font-size: 20px;
  color: #999;
  cursor: pointer;
  padding: 4px;
}

.action-modal-close:active {
  color: #333;
}

.action-modal-body {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.action-modal-btn {
  width: 100%;
  padding: 16px;
  border: 1px solid #eee;
  border-radius: 12px;
  background: #fff;
  font-size: 15px;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  text-align: center;
  transition: background 0.15s ease;
}

.action-modal-btn:active {
  background: #f5f5f5;
}

/* ========================================
   글쓰기 모달
   ======================================== */
.write-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  z-index: 200;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.write-modal-sheet {
  width: 100%;
  max-width: 480px;
  background: #fff;
  border-radius: 20px 20px 0 0;
  max-height: 90vh;
  overflow-y: auto;
  margin-bottom:80px;
}

.write-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 20px 0;
}

.write-modal-title {
  font-size: 18px;
  font-weight: 700;
  color: #333;
}

.write-modal-close {
  font-size: 20px;
  color: #999;
  cursor: pointer;
  padding: 4px;
}

.write-modal-body {
  padding: 20px;
}

.write-field {
  margin-bottom: 14px;
}

.write-input {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  font-size: 14px;
  outline: none;
  box-sizing: border-box;
}

.write-input:focus {
  border-color: #061da1;
}

.write-textarea {
  width: 100%;
  padding: 12px 14px;
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  font-size: 14px;
  outline: none;
  resize: vertical;
  box-sizing: border-box;
  font-family: inherit;
}

.write-textarea:focus {
  border-color: #061da1;
}

.write-image-field {
  margin-bottom: 14px;
}

.write-image-upload {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 20px;
  border: 2px dashed #e0e0e0;
  border-radius: 10px;
  color: #bbb;
  font-size: 14px;
  cursor: pointer;
  transition: border-color 0.2s;
}

.write-image-upload:active {
  border-color: #061da1;
  color: #061da1;
}

.write-image-preview {
  position: relative;
  border-radius: 10px;
  overflow: hidden;
}

.write-image-preview img {
  width: 100%;
  max-height: 200px;
  object-fit: cover;
}

.remove-image {
  position: absolute;
  top: 8px;
  right: 8px;
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.6);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 14px;
}

.write-notice-field {
  margin-bottom: 14px;
}

.notice-checkbox {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: #555;
  cursor: pointer;
}

.notice-checkbox input[type="checkbox"] {
  width: 18px;
  height: 18px;
  accent-color: #061da1;
}

.write-modal-footer {
  display: flex;
  gap: 10px;
  padding: 0 20px 32px;
}

.write-cancel-btn {
  flex: 1;
  padding: 14px;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  background: #fff;
  font-size: 15px;
  font-weight: 600;
  color: #666;
  cursor: pointer;
}

.write-submit-btn {
  flex: 1;
  padding: 14px;
  border: none;
  border-radius: 12px;
  background: #061da1;
  color: #fff;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
}

.write-submit-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}

/* ========================================
   Transitions
   ======================================== */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.25s ease;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.slide-up-enter-active {
  transition: transform 0.3s ease;
}

.slide-up-leave-active {
  transition: transform 0.2s ease;
}

.slide-up-enter {
  transform: translateY(100%);
}

.slide-up-leave-to {
  transform: translateY(100%);
}

/* ===== Section Overrides ===== */
.league-header {
  margin-bottom: 0;
}

.team-page-view .league-section {
  padding-top: 30px !important;
  overflow: hidden !important;
}

/* ===== Slick Dots Override ===== */
::v-deep .slick-dots {
  bottom: 8px;
}

::v-deep .slick-dots li button:before {
  color: #fff;
  opacity: 0.5;
}

::v-deep .slick-dots li.slick-active button:before {
  color: #fff;
  opacity: 1;
}
</style>
