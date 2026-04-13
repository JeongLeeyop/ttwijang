<template>
  <div class="header">
      <div class="header-left">
        <i v-if="showBack" @click="goBack" class="el-icon-s-fold"></i>
        <!-- Region Filter (BR-04) -->
        <el-select
          :popper-append-to-body="false"
          v-model="selectedRegion"
          placeholder="지역 선택"
          size="small"
          clearable
          @change="onRegionChange"
        >
          <el-option
            v-for="region in regionOptions"
            :key="region.value"
            :label="region.label"
            :value="region.value"
          ></el-option>
        </el-select>
      </div>
      <div class="header-center">
      </div>
      <div class="header-right">
        <i class="el-icon-date" @click="goToCalendar"></i>
        <el-popover v-model="showPopover" width="450" trigger="click" popper-class="alarm"
          :title="alarmList.length > 0 ? '띵동! 알림이 도착했어요 🎶' : ''"
          @show="onPopoverShow">
          <div @click="showPopover = false" class="alarm-close">
            <i class="el-icon-close"></i>
          </div>
          <div v-if="alarmList2.length > 0">
            <div class="alarm-item-day-wr" v-for="(item, index) in alarmList2" :key="index">
              <div class="alarm-item-day">{{ item[0].createDate | parseDate('YYYY-MM-DD') }}</div>
              <div v-if="item.length > 0">
                <a :href="item2.link" class="alarm-item-wr" v-for="item2 in item" :key="item2.id">
                  <div class="alarm-info">
                    <div class="title">{{ item2.title }}</div>
                    <div class="date">{{ item2.createDate | parseDate('YYYY-MM-DD HH:mm') }}</div>
                  </div>
                  <div class="alarm-content">{{ item2.content }}</div>
                </a>
              </div>
            </div>
          </div>
          <div class="empty" v-else>
            <!-- <div class="logo"><img src="@/assets/images/logo2.png" /></div> -->
            <div class="txt">도착한 알림이 없어요</div>
          </div>
          <el-button type="text" slot="reference" class="el-icon-bell header__bell alarm" :class="{'alert': newAlarmCount >= 1}"></el-button>
        </el-popover>
      </div>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';
import { getAllSigunguList, Region } from '@/api/region';
import {
  getNotifications, getUnreadNotificationCount, markAllNotificationsAsRead,
} from '@/api/notification';
import { storageKey } from '@/enums/localStorage';

@Component({
  name: 'MainHeader',
})
export default class extends Vue {
  @Prop({ default: '' }) private title!: string;

  @Prop({ default: false }) private showNotification!: boolean;

  @Prop({ default: true }) private showBack!: boolean;

  private selectedRegion = ''

  private regionOptions: Array<{ label: string, value: string, code: string }> = []

  private showPopover = false;

  private alarmCount = 0;

  private newAlarmCount = 0;

  private alarmList: any = [];

  private alarmList2: any[][] = [];

  private totalElements = 0;

  private totalPages = 0;

  private listQuery = {
    page: 0,
    size: 5,
  };

  mounted() {
    this.loadNotifications();
    this.loadUnreadCount();
    this.loadRegionsAndInitialize();
  }

  private goBack() {
    this.$router.go(-1);
  }

  private handleNotification() {
    this.$emit('notification-click');
  }

  private goToCalendar(): void {
    this.$router.push('/calendar');
  }

  private onRegionChange(): void {
    // BR-04: 지역 필터 변경 시 이벤트 발생
    // localStorage에 선택된 지역 저장
    if (this.selectedRegion) {
      localStorage.setItem(storageKey.selectedRegion, this.selectedRegion);
    } else {
      localStorage.removeItem(storageKey.selectedRegion);
    }
    this.$emit('region-change', this.selectedRegion);
  }

  private async loadNotifications(): Promise<void> {
    try {
      const response = await getNotifications({
        page: this.listQuery.page,
        size: this.listQuery.size,
      });
      const pageData = response.data;
      this.alarmList = pageData.content || [];
      this.totalElements = pageData.totalElements || 0;
      this.totalPages = pageData.totalPages || 0;

      // 날짜별로 그룹화
      const itemMap: Map<string, any[]> = new Map();
      this.alarmList.forEach((item: any) => {
        const dateStr: string = item.createdDate
          ? item.createdDate.substring(0, 10)
          : '';
        if (!dateStr) return;
        if (!itemMap.has(dateStr)) {
          itemMap.set(dateStr, []);
        }
        const itemList: any[] | undefined = itemMap.get(dateStr);
        if (itemList) {
          // API 응답을 popover 형식에 맞게 변환
          itemList.push({
            id: item.uid,
            title: item.title,
            content: item.content,
            createDate: item.createdDate,
            link: item.actionUrl || '#',
            isRead: item.isRead,
          });
        }
      });
      this.alarmList2 = Array.from(itemMap.values());
    } catch (error) {
      console.error('알림 목록 로드 실패:', error);
    }
  }

  private async loadUnreadCount(): Promise<void> {
    try {
      const response = await getUnreadNotificationCount();
      this.newAlarmCount = response.data?.count || 0;
    } catch (error) {
      console.error('읽지 않은 알림 개수 로드 실패:', error);
    }
  }

  private async onPopoverShow(): Promise<void> {
    // 팝오버 열릴 때 읽음 처리
    if (this.newAlarmCount > 0) {
      try {
        await markAllNotificationsAsRead();
        this.newAlarmCount = 0;
      } catch (error) {
        console.error('알림 읽음 처리 실패:', error);
      }
    }
  }

  private handleChangePage(page: number) {
    this.listQuery.page = page;
    this.loadNotifications();
  }

  private async loadRegionsAndInitialize(): Promise<void> {
    try {
      const response = await getAllSigunguList();
      if (response && response.data) {
        this.regionOptions = response.data.map((region: Region) => ({
          label: region.name,
          value: region.code, // 코드값으로 검색하도록 변경
          code: region.code,
        }));
      }

      // 지역 목록 로드 완료 후 선택된 지역 초기화
      this.initializeSelectedRegion();
    } catch (error) {
      console.error('Failed to load regions:', error);
      // 에러 발생 시에도 기본값 설정
      this.initializeSelectedRegion();
    }
  }

  private initializeSelectedRegion(): void {
    // localStorage에서 선택된 지역 코드 가져오기
    const savedRegion = localStorage.getItem(storageKey.selectedRegion);
    if (savedRegion) {
      // 저장된 지역 코드가 옵션 목록에 있는지 확인
      const isValid = this.regionOptions.some((opt) => opt.value === savedRegion);
      if (isValid) {
        this.selectedRegion = savedRegion;
      } else {
        // 기존 한글 이름이 저장되어 있는 경우 코드로 변환 시도
        const matchByName = this.regionOptions.find((opt) => opt.label === savedRegion);
        this.selectedRegion = matchByName
          ? matchByName.value
          : this.getDefaultRegionCode();
      }
    } else {
      // 저장된 지역이 없으면 기본값으로 진주시 코드 설정
      this.selectedRegion = this.getDefaultRegionCode();
    }
    localStorage.setItem(storageKey.selectedRegion, this.selectedRegion);
    // 초기 로드 시에도 region-change 이벤트 발생
    this.$emit('region-change', this.selectedRegion);
  }

  private getDefaultRegionCode(): string {
    // 진주시 코드를 옵션에서 찾기
    const jinju = this.regionOptions.find((opt) => opt.label === '진주시');
    if (jinju) {
      return jinju.value;
    }
    if (this.regionOptions.length > 0) {
      return this.regionOptions[0].value;
    }
    return '';
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
