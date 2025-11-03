<template>
  <div class="map-wrap" v-loading="loading">
    <div id="map" />
    <el-drawer :with-header="false" :visible.sync="drawerVisible" size="40%" direction="btt">
      <div v-if="selectedShop" class="map-info">
        <table>
          <thead>
            <tr>
              <th colspan="2"> {{ selectedShop.name }} </th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <th> 주소 </th>
              <td>{{ selectedShop.address }} {{ selectedShop.addressDetail }}</td>
            </tr>
            <tr>
              <th> 전화번호 </th>
              <td> {{ selectedShop.tel }} </td>
            </tr>
            <tr>
              <th>영업시간 <br>(월 - 금)</th>
              <td class="nop">
                <p v-for="(item, index) in pickupTimeStr" :key="index">{{ item }} </p>
                <!-- <span v-for="(item, index) in pickupTimes" :key="index">{{ item.time }} </span> -->
              </td>
            </tr>
            <tr>
              <th>휴무일 <br>(월 - 금)</th>
              <td class="nop">
                {{ filterWeekDays() }}
              </td>
            </tr>
          </tbody>
          <div class="map-footer">
            <div class="txt">
              <button class="pickup-btn" @click="handlePickup">이 장소에서 픽업하기</button>
            </div>
          </div>
        </table>
      </div>
    </el-drawer>
  </div>
</template>

<script lang="ts">
/* eslint-disable */
import { Vue, Component, Watch } from 'vue-property-decorator';
import { storageKey } from '@/enums/localStorage';
import { getShopList } from '@/api/shop';
import { updateShop } from '@/api/user';
import { getShopPickupTimes } from '@/api/shop';
import moment from 'moment';
import { UserModule } from '@/store/modules/user';
import { getUserInfo } from '@/api/user';

@Component({
})

export default class extends Vue {
  async mounted() {
    if (UserModule.isLogin) {
      await this.getUserInfo();
    }
    this.initKakao();
  }
  
  private shopList: any = [];

  private loading = true;

  private map: any = null;

  private clusterer: any = null;

  private overlayList: any = [];

  private selectedShop: any = null;

  private pickupTimes = [];
  
  private pickupTimeStr: string[] = [];

  private drawerVisible = false;

  private userInfo = {};

  private lat = "";
  
  private lon = "";

  private initKakao() {
    if ((window as any).kakao && (window as any).kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement('script');
      script.src = '//dapi.kakao.com/v2/maps/sdk.js?libraries=services,clusterer&autoload=false&appkey=6f0711392197c33f62af5bbc9cc83a82';
      script.onload = () => (window as any).kakao.maps.load(this.initMap);
      document.head.appendChild(script);
    }
  }

  private async getUserInfo() {
    await getUserInfo().then((res) => {
      this.userInfo = res.data;
      this.lat = res.data.lat;
      this.lon = res.data.lon;
    });
  }

  private async initMap() {
    const mapContainer = document.getElementById('map');
    if (this.lat === null || this.lat === undefined || this.lat === '' ) this.lat = "36.6227659292899";
    if (this.lon === null || this.lon === undefined || this.lon === '' ) this.lon = "127.460409252296";
    const mapOption = {
      center: new (window as any).kakao.maps.LatLng(this.lat, this.lon),
      // center: new (window as any).kakao.maps.LatLng(this.lat, this.lon),
      level: 6,
    };
    this.map = new (window as any).kakao.maps.Map(mapContainer, mapOption);
    this.clusterer = new (window as any).kakao.maps.MarkerClusterer({
      map: this.map,
      averageCenter: true,
      minLevel: 4,
    });
    this.getShopList();
  }

  private async initMarkers() {
    this.loading = true;
    const overlayList: any = await this.createOverlays();
    const customOverlayList: any = [];
    overlayList.forEach((overlay: any) => {
      customOverlayList.push(overlay.overlay);
    });
    await this.handleAddMarker(customOverlayList);
  }

  private handlePickup() {
    // if(this.selectedShop.maxHoldCnt == 0) {
      // this.$message.info("현재 주문이 불가능한 매장입니다.")
    // } else {
      updateShop(this.selectedShop.idx).then(async (res) => {
        window.localStorage.setItem(storageKey.pickUpPlace, JSON.stringify(this.selectedShop)); 
        window.localStorage.removeItem(storageKey.stationPlace); 
        this.$router.push({ name: 'Order' });
      });
    // }
  }

  private async createOverlays() {
    /* eslint-disable */
    return new Promise((resolve: any) => {
      if (this.clusterer) this.clusterer.clear();
      const overlayList: any = [];
      this.shopList.forEach((shop: any) => {
        if (shop.lat && shop.lon) {
          let content = this.createOverlayContent(shop);
          const overlay = new (window as any).kakao.maps.CustomOverlay({
            map: this.map,
            content: content,
            yAnchor: 1.2,
            position: new (window as any).kakao.maps.LatLng(Number(shop.lat), Number(shop.lon)),
            clickable: true,
          });
          overlayList.push({
            id: shop.id,
            overlay: overlay,
          });
          content.addEventListener('mouseup', () => { this.handleSelectShop(shop) });
        }
      });
      this.overlayList = overlayList;
      resolve(overlayList);
    });
    /* eslint-enable */
  }

  private createOverlayContent(shop: any) {
    const content = document.createElement('div');
    content.innerHTML = `
    <div style="position: relative;">
      <button>
        <div class="custom">
          ${shop.name}
        </div>
      </button>
    </div>
    `;
    return content;
  }

  private async handleAddMarker(overlayList: any[]) {
    this.clusterer.addMarkers(overlayList);
    this.loading = false;
  }

  private async handleSelectShop(shop: any) {
    this.pickupTimeStr = [];
    this.selectedShop = shop;
    // 영업시간 표시
    await getShopPickupTimes(this.selectedShop.idx).then((res) => {
      this.pickupTimes = res.data;

      // 이어지는 시간대를 묶어서 저장해주는 작업
      let firstTime: any = null;
      let endTime: any = null;
      let currentTime: any = null;
      let prevTime: any = null;

      this.pickupTimes.forEach((item: any, index: number) => {
        currentTime = moment(item.time, 'HH:mm');
        // 첫번째 반복문 라운드일때 로직을 수행하지 않고 시작시간만 저장한다.
        if (index === 0) firstTime = currentTime;
        // 전 시간대와 이어지지 않는 경우 시작과 끝시간을 저장한다. (+30분 차이가 아닌경우)
        else if (index !== 0 && currentTime.diff(prevTime, 'minutes') !== 30) {
            endTime = prevTime;
            this.pickupTimeStr.push(`${firstTime.format('HH:mm')} ~ ${endTime.add(30, 'minutes').format('HH:mm')}`);
            firstTime = currentTime;
        }
        // 반복문의 마지막 요소일때 저장 처리
        if (this.pickupTimes.length === index + 1) this.pickupTimeStr.push(`${firstTime.format('HH:mm')} ~ ${currentTime.add(30, 'minutes').format('HH:mm')}`);
        // 픽업가능시간이 하나만 존재할 때 처리
        if (this.pickupTimes.length === 1) this.pickupTimeStr.push(`${firstTime.format('HH:mm')} ~ ${firstTime.add(30, 'minutes').format('HH:mm')}`);
        // 다음 비교를 위해 현재시간을 prevTime에 저장한다.
        prevTime = currentTime;
      });
    });
    this.drawerVisible = !this.drawerVisible;
  }

  private getShopList() {
    getShopList().then(async (res) => {
      this.shopList = res.data;
      await this.initMarkers();
    });
  }

  private filterWeekDays() {
    let str = '';
    if (this.selectedShop.holidays.mon) str += '월, ';
    if (this.selectedShop.holidays.tue) str += '화, ';
    if (this.selectedShop.holidays.wed) str += '수, ';
    if (this.selectedShop.holidays.thu) str += '목, ';
    if (this.selectedShop.holidays.fri) str += '금, ';
    if (this.selectedShop.holidays.sat) str += '토, ';
    if (this.selectedShop.holidays.sun) str += '일, ';
    return str.slice(0, str.length - 2);
  }
}
</script>

<style>
#map {
  width: 100%;
  height: 100vh;
  z-index: 0
}

.custom {
  position: relative;
  width: auto;
  padding: 5px 10px;
  background: #fff;
  border-radius: 10px;
  border: 2px solid #58C0D6;
  font-size: 16px;
}

.location {
  background: #fff;
  width: 100%;
  padding: 10px;
  border-radius: 20px;
  border: 2px solid #58C0D6;
}

.custom:after {
  content: '';
  position: absolute;
  border-top: 12px solid #fff;
  border-right: 7px solid transparent;
  border-left: 7px solid transparent;
  bottom: -10px;
  left: 50%;
  transform: translate(-50%, 0%);
}

.custom:before {
  content: '';
  position: absolute;
  border-top: 12px solid #58C0D6;
  border-right: 9px solid transparent;
  border-left: 9px solid transparent;
  bottom: -13px;
  left: 50%;
  transform: translate(-50%, 0%);
}

button {
  border-radius: 20px;
  padding: 0;
  border: none;
  color: #000;
}
</style>
