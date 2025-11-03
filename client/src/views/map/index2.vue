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
          </tbody>
          <div class="map-footer">
            <div class="txt">
              <button class="pickup-btn" @click="handlePickup">이 장소에 배달하기</button>
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
import { getStationList } from '@/api/station';
import { updateStation } from '@/api/user';
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
    updateStation(this.selectedShop.idx).then(async (res) => {
      window.localStorage.removeItem(storageKey.pickUpPlace); 
      window.localStorage.setItem(storageKey.stationPlace, JSON.stringify(this.selectedShop)); 
      this.$router.push({ name: 'CustomProduct' });
      });
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

  private async handleSelectShop(station: any) {
    this.pickupTimeStr = [];
    this.selectedShop = station;
    this.drawerVisible = !this.drawerVisible;
  }

  private getShopList() {
    getStationList().then(async (res) => {
      this.shopList = res.data;
      await this.initMarkers();
    });
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
