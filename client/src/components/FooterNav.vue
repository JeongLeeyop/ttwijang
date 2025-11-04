<template>
  <div class="footer">
    <div class="footer-item">
      <svg class="footer-icon" viewBox="0 0 24 24" fill="currentColor">
        <circle cx="12" cy="12" r="10" fill="none" stroke="currentColor" stroke-width="1.5"/>
        <path d="M12 2 L12 22 M2 12 L22 12" stroke="currentColor" stroke-width="1"/>
        <path d="M7 5 Q12 8 17 5 M7 19 Q12 16 17 19" fill="none" stroke="currentColor" stroke-width="1"/>
        <path d="M5 7 Q8 12 5 17 M19 7 Q16 12 19 17" fill="none" stroke="currentColor" stroke-width="1"/>
      </svg>
      <span>리그</span>
    </div>
    <div class="footer-item">
      <svg class="footer-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <rect x="3" y="5" width="18" height="14" rx="2"/>
        <path d="M3 10 L21 10"/>
        <circle cx="12" cy="14" r="2" fill="currentColor"/>
      </svg>
      <span>매치</span>
    </div>
    <div class="footer-item footer-home" @click="goHome">
      <i class="el-icon-s-home"></i>
    </div>
    <div class="footer-item">
      <svg class="footer-icon" viewBox="0 0 24 24" fill="currentColor">
        <circle cx="12" cy="7" r="3"/>
        <path d="M12 12 C8 12 5 14 5 17 L19 17 C19 14 16 12 12 12 Z"/>
        <circle cx="6" cy="9" r="2" opacity="0.7"/>
        <path d="M6 12 C3.5 12 2 13.5 2 15.5 L7 15.5" opacity="0.7"/>
        <circle cx="18" cy="9" r="2" opacity="0.7"/>
        <path d="M18 12 C20.5 12 22 13.5 22 15.5 L17 15.5" opacity="0.7"/>
      </svg>
      <span>게스트</span>
    </div>
    <div class="footer-item">
      <i class="el-icon-user-solid"></i>
      <span>MY</span>
    </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';

@Component
export default class FooterNav extends Vue {
  private goHome(): void {
    this.$router.push('/');
  }
}
</script>

<style lang="scss" scoped>
.footer {
  position: fixed;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  max-width: 480px;
  width: 100%;
  z-index: 100;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 12px 20px;
  background: rgba(255, 255, 255, 0.95);
  border-top: solid 3px #c5cad5;
  flex-shrink: 0;

  .footer-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 4px;
    cursor: pointer;
    transition: all 0.3s ease;

    i {
      font-size: 30px;
      color: #666;
    }

    .footer-icon {
      width: 30px;
      height: 30px;
      color: #666;
    }

    span {
      font-size: 13px;
      color: #666;
    }

    &:hover {
      i, span, .footer-icon {
        color: #061da1;
      }
    }

    &.footer-home {
      position: relative;
      top: -40px;

      i {
        font-size: 48px;
        color: #ffffff;
        background: #061da1;
        border-radius: 50%;
        padding: 8px;
        box-shadow: 0 4px 12px rgba(6, 29, 161, 0.3);
      }

      &:hover i {
        background: #0a2bcc;
      }
    }
  }
}
</style>
