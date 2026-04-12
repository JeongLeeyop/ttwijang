<template>
  <div class="guide-page">
    <!-- Hero -->
    <div class="hero-section">
      <div class="hero-badge">MANNER SCORE SYSTEM</div>
      <h1 class="hero-title">매너점수로<br>더 즐거운 풋살을</h1>
      <p class="hero-sub">경기 후 서로를 평가해 건강한 풋살 문화를 만들어요</p>
      <div class="score-demo">
        <div
          v-for="s in 10"
          :key="s"
          class="score-dot"
          :class="{ active: s <= 8 }"
        ></div>
        <span class="score-demo-val">8.0</span>
      </div>
    </div>

    <div class="content-area">

      <!-- 매너점수란? -->
      <section class="card-section">
        <div class="section-tag">01</div>
        <h2 class="section-title">매너점수란?</h2>
        <p class="section-desc">
          매너점수는 경기 후 상대 팀과 게스트 플레이어가 서로를 평가하는
          <strong>신뢰 지표</strong>입니다.<br><br>
          점수는 <strong>0 ~ 10점</strong> 사이로 평가되며,
          팀의 평균 점수로 누적됩니다. 높은 매너점수를 유지한 팀은
          더 좋은 상대를 만날 기회가 늘어납니다.
        </p>
        <div class="info-row">
          <div class="info-chip"><i class="el-icon-star-on"></i> 0 ~ 10점 척도</div>
          <div class="info-chip"><i class="el-icon-user"></i> 팀 평균 산출</div>
          <div class="info-chip"><i class="el-icon-refresh"></i> 매 경기 갱신</div>
        </div>
      </section>

      <!-- 점수 기준 -->
      <section class="card-section">
        <div class="section-tag">02</div>
        <h2 class="section-title">점수 기준</h2>
        <div class="score-levels">
          <div class="level-item level-excellent">
            <div class="level-range">9 ~ 10</div>
            <div class="level-info">
              <div class="level-name">매너왕 ✨</div>
              <div class="level-desc">최고의 페어플레이어. 함께하고 싶은 팀!</div>
            </div>
          </div>
          <div class="level-item level-good">
            <div class="level-range">7 ~ 8</div>
            <div class="level-info">
              <div class="level-name">좋은 매너 👍</div>
              <div class="level-desc">쾌적한 경기를 만드는 팀</div>
            </div>
          </div>
          <div class="level-item level-normal">
            <div class="level-range">5 ~ 6</div>
            <div class="level-info">
              <div class="level-name">보통 🙂</div>
              <div class="level-desc">평균적인 매너를 보유한 팀</div>
            </div>
          </div>
          <div class="level-item level-bad">
            <div class="level-range">3 ~ 4</div>
            <div class="level-info">
              <div class="level-name">주의 ⚠️</div>
              <div class="level-desc">매너 개선이 필요한 팀</div>
            </div>
          </div>
          <div class="level-item level-danger">
            <div class="level-range">0 ~ 2</div>
            <div class="level-info">
              <div class="level-name">경고 🚫</div>
              <div class="level-desc">매칭 제한이 적용될 수 있어요</div>
            </div>
          </div>
        </div>
      </section>

      <!-- 어떻게 평가하나? -->
      <section class="card-section">
        <div class="section-tag">03</div>
        <h2 class="section-title">어떻게 평가하나요?</h2>
        <div class="step-list">
          <div class="step-item">
            <div class="step-num">1</div>
            <div class="step-content">
              <div class="step-title">경기 종료 후 알림 수신</div>
              <div class="step-desc">경기가 종료되면 상대 팀 평가 알림이 도착해요</div>
            </div>
          </div>
          <div class="step-connector"></div>
          <div class="step-item">
            <div class="step-num">2</div>
            <div class="step-content">
              <div class="step-title">1~10점으로 평가</div>
              <div class="step-desc">경기 매너, 시간 준수, 규칙 준수 등을 종합 평가해요</div>
            </div>
          </div>
          <div class="step-connector"></div>
          <div class="step-item">
            <div class="step-num">3</div>
            <div class="step-content">
              <div class="step-title">팀 평균 점수 반영</div>
              <div class="step-desc">누적 평균이 팀 프로필에 표시됩니다</div>
            </div>
          </div>
        </div>
      </section>

      <!-- 매너왕 팁 -->
      <section class="card-section tip-section">
        <div class="section-tag">04</div>
        <h2 class="section-title">매너왕이 되는 법 💡</h2>
        <div class="tip-list">
          <div class="tip-item">
            <span class="tip-icon">⏰</span>
            <div>
              <div class="tip-title">시간을 지켜요</div>
              <div class="tip-desc">킥오프 10분 전 도착이 기본 매너입니다</div>
            </div>
          </div>
          <div class="tip-item">
            <span class="tip-icon">🤝</span>
            <div>
              <div class="tip-title">경기 전후 인사</div>
              <div class="tip-desc">시작과 끝에 상대 팀과 악수하세요</div>
            </div>
          </div>
          <div class="tip-item">
            <span class="tip-icon">🎽</span>
            <div>
              <div class="tip-title">유니폼 착용</div>
              <div class="tip-desc">색깔이 구분되는 복장을 준비해요</div>
            </div>
          </div>
          <div class="tip-item">
            <span class="tip-icon">🗣️</span>
            <div>
              <div class="tip-title">건전한 언어 사용</div>
              <div class="tip-desc">불필요한 항의나 언쟁은 자제해요</div>
            </div>
          </div>
          <div class="tip-item">
            <span class="tip-icon">🧹</span>
            <div>
              <div class="tip-title">경기장 정리</div>
              <div class="tip-desc">사용 후 깨끗하게 정리하고 나가요</div>
            </div>
          </div>
        </div>
      </section>

      <!-- 서비스 이용 가이드 -->
      <section class="card-section">
        <div class="section-tag">05</div>
        <h2 class="section-title">서비스 이용 가이드</h2>
        <div class="usage-grid">
          <div class="usage-card">
            <div class="usage-icon">⚽</div>
            <div class="usage-name">매치 참가</div>
            <div class="usage-desc">홈 화면에서 원하는 매치를 선택해 캐시로 참가비를 결제하세요</div>
          </div>
          <div class="usage-card">
            <div class="usage-icon">🏃</div>
            <div class="usage-name">팀 가입</div>
            <div class="usage-desc">팀 코드로 즉시 가입하거나 가입 신청 후 운영자 승인을 받으세요</div>
          </div>
          <div class="usage-card">
            <div class="usage-icon">🏆</div>
            <div class="usage-name">리그 참가</div>
            <div class="usage-desc">팀을 생성하고 리그에 등록해 시즌 챔피언을 노려보세요</div>
          </div>
          <div class="usage-card">
            <div class="usage-icon">💰</div>
            <div class="usage-name">캐시 충전</div>
            <div class="usage-desc">마이페이지에서 캐시를 충전하고 매치 참가비로 사용하세요</div>
          </div>
        </div>
      </section>

      <div class="bottom-space"></div>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component({ name: 'GuidePage' })
export default class GuidePage extends Vue {
  mounted(): void {
    const el = document.querySelector('.auth-layout') as HTMLElement | null;
    if (el) el.style.overflow = 'auto';
  }

  beforeDestroy(): void {
    const el = document.querySelector('.auth-layout') as HTMLElement | null;
    if (el) el.style.overflow = '';
  }
}
</script>

<style scoped>
.guide-page {
  background: #f4f6fb;
  max-width: 480px;
  margin: 0 auto;
}

/* Hero */
.hero-section {
  background: linear-gradient(145deg, #061da1 0%, #1a3fd1 60%, #2d54e8 100%);
  padding: 80px 20px 36px;
  text-align: center;
  color: #fff;
}

.hero-badge {
  display: inline-block;
  background: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 20px;
  padding: 4px 14px;
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 1.5px;
  margin-bottom: 16px;
}

.hero-title {
  font-size: 28px;
  font-weight: 900;
  line-height: 1.3;
  margin: 0 0 10px;
  letter-spacing: -0.5px;
}

.hero-sub {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.6;
  margin-bottom: 24px;
}

.score-demo {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: rgba(255, 255, 255, 0.12);
  border-radius: 24px;
  padding: 10px 18px;
}

.score-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.25);
  transition: background 0.2s;
}

.score-dot.active {
  background: #ffd700;
}

.score-demo-val {
  font-size: 18px;
  font-weight: 900;
  color: #ffd700;
  margin-left: 8px;
}

/* Content */
.content-area {
  padding: 16px;
}

.card-section {
  background: #fff;
  border-radius: 16px;
  padding: 22px 18px;
  margin-bottom: 14px;
  box-shadow: 0 2px 8px rgba(6, 29, 161, 0.06);
}

.section-tag {
  font-size: 11px;
  font-weight: 800;
  color: #061da1;
  background: rgba(6, 29, 161, 0.08);
  display: inline-block;
  padding: 2px 8px;
  border-radius: 6px;
  margin-bottom: 8px;
  letter-spacing: 1px;
}

.section-title {
  font-size: 17px;
  font-weight: 800;
  color: #111;
  margin: 0 0 12px;
  letter-spacing: -0.3px;
}

.section-desc {
  font-size: 14px;
  color: #555;
  line-height: 1.7;
  margin-bottom: 14px;
}

.info-row {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.info-chip {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: #f0f3ff;
  color: #061da1;
  font-size: 12px;
  font-weight: 600;
  padding: 5px 10px;
  border-radius: 8px;
}

/* Score Levels */
.score-levels {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.level-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 14px;
  border-radius: 10px;
}

.level-range {
  font-size: 15px;
  font-weight: 900;
  min-width: 46px;
  text-align: center;
}

.level-name {
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 2px;
  text-align: left;
}

.level-desc {
  font-size: 12px;
  opacity: 0.75;
}

.level-excellent { background: #e8f8ef; color: #1a7a40; }
.level-good { background: #edf7ff; color: #1560bd; }
.level-normal { background: #fffbea; color: #856404; }
.level-bad { background: #fff4e0; color: #c05e00; }
.level-danger { background: #fff0f0; color: #c0392b; }

/* Steps */
.step-list {
  display: flex;
  flex-direction: column;
}

.step-item {
  display: flex;
  align-items: flex-start;
  gap: 14px;
}

.step-num {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #061da1;
  color: #fff;
  font-size: 14px;
  font-weight: 800;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.step-title {
  font-size: 14px;
  font-weight: 700;
  color: #111;
  margin-bottom: 4px;
  text-align: left;
}

.step-desc {
  font-size: 12px;
  color: #777;
  line-height: 1.5;
}

.step-connector {
  width: 2px;
  height: 20px;
  background: #e0e4f8;
  margin-left: 15px;
}

/* Tips */
.tip-section {
  background: linear-gradient(135deg, #f0f3ff 0%, #fff 100%);
}

.tip-list {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.tip-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.tip-icon {
  font-size: 22px;
  flex-shrink: 0;
  width: 36px;
  text-align: center;
}

.tip-title {
  font-size: 14px;
  font-weight: 700;
  color: #111;
  margin-bottom: 2px;
  text-align: left;
}

.tip-desc {
  font-size: 12px;
  color: #777;
}

/* Usage Grid */
.usage-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.usage-card {
  background: #f7f8ff;
  border-radius: 12px;
  padding: 14px 12px;
  text-align: center;
}

.usage-icon {
  font-size: 26px;
  margin-bottom: 6px;
}

.usage-name {
  font-size: 13px;
  font-weight: 700;
  color: #111;
  margin-bottom: 4px;
}

.usage-desc {
  font-size: 11px;
  color: #888;
  line-height: 1.5;
}

.bottom-space {
  height: 40px;
}
</style>
