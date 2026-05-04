<template>
  <div class="header">
      <div class="header-left">
        <i v-if="showBack" @click="goBack" class="el-icon-s-fold"></i>
        <!-- Region Filter (BR-04) -->
        <el-select
          v-if="showRegionSelector"
          :popper-append-to-body="false"
          v-model="selectedRegion"
          placeholder="지역 선택"
          size="small"
          @change="onRegionChange"
        >
          <el-option-group
            v-for="sido in sidoGroups"
            :key="sido.code"
            :label="sido.label"
          >
            <el-option
              v-for="sg in sido.options"
              :key="sg.value"
              :label="sg.label"
              :value="sg.value"
            />
          </el-option-group>
        </el-select>
      </div>
      <div class="header-center">
      </div>
      <div class="header-right">
        <i class="el-icon-date" @click="goToCalendar"></i>
        <el-popover v-model="showPopover" width="450" trigger="click" popper-class="alarm"
          placement="bottom-end"
          :append-to-body="false"
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
import { getSidoList, getAllSigunguList, Region } from '@/api/region';
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

  private sidoGroups: Array<{ label: string, code: string, options: Array<{ label: string, value: string }> }> = []

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

  get showRegionSelector(): boolean {
    const hiddenRoutes = ['Match', 'MyPage', 'TeamPage'];
    return !hiddenRoutes.includes(String(this.$route.name));
  }

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

  private onRegionChange(value: string): void {
    if (!value) {
      // clearable 없이는 발생하지 않지만, 방어 처리
      this.selectedRegion = this.getDefaultRegionCode();
      localStorage.setItem(storageKey.selectedRegion, this.selectedRegion);
      this.$emit('region-change', this.selectedRegion);
      return;
    }
    localStorage.setItem(storageKey.selectedRegion, value);
    this.$emit('region-change', value);
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
      const [sidoRes, sigunguRes] = await Promise.all([getSidoList(), getAllSigunguList()]);
      const sidoList: Region[] = sidoRes?.data || [];
      const sigunguList: Region[] = sigunguRes?.data || [];

      // 시/도별로 시/군/구 그루핑
      this.sidoGroups = sidoList.map((sido) => ({
        label: sido.name,
        code: sido.code,
        options: sigunguList
          .filter((sg) => sg.parentCode === sido.code)
          .map((sg) => ({ label: sg.name, value: sg.code })),
      })).filter((g) => g.options.length > 0);
    } catch (error) {
      console.error('Failed to load regions:', error);
    }
    this.initializeSelectedRegion();
  }

  private initializeSelectedRegion(): void {
    const allOptions = this.sidoGroups.flatMap((g) => g.options);
    const savedRegion = localStorage.getItem(storageKey.selectedRegion);

    if (savedRegion) {
      const isValid = allOptions.some((opt) => opt.value === savedRegion);
      if (isValid) {
        this.selectedRegion = savedRegion;
      } else {
        const matchByName = allOptions.find((opt) => opt.label === savedRegion);
        this.selectedRegion = matchByName ? matchByName.value : this.getDefaultRegionCode();
      }
    } else {
      this.selectedRegion = this.getDefaultRegionCode();
    }

    // 항상 하나는 선택된 상태를 보장
    if (!this.selectedRegion && allOptions.length > 0) {
      this.selectedRegion = allOptions[0].value;
    }

    localStorage.setItem(storageKey.selectedRegion, this.selectedRegion);
    this.$emit('region-change', this.selectedRegion);
  }

  private getDefaultRegionCode(): string {
    const allOptions = this.sidoGroups.flatMap((g) => g.options);
    const jinju = allOptions.find((opt) => opt.label === '진주시');
    return jinju ? jinju.value : (allOptions[0]?.value || '');
  }
}
</script>

<style scoped>
/* Styles are in style.css */
</style>
