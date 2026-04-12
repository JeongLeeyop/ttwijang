<template>
  <div class="aside">
    <div class="aside-logo">
      <router-link :to="{ name: 'Home' }">
        <div class="aside-title">뛰장 관리자</div>
      </router-link>
    </div>

    <div class="aside-content">
      <el-menu router="true" :default-active="activeMenu()">
        <slot v-for="route in routes">
          <slot v-if="route.meta && !route.meta.hidden">
            <!-- 단일 자식 → 직접 메뉴 아이템 -->
            <el-menu-item
              v-if="getSingleChild(route)"
              :key="route.path"
              :index="getSingleChild(route).name"
              :route="getSingleChild(route)"
            >
              {{ route.meta.title }}
            </el-menu-item>
            <!-- 다중 자식 → 서브메뉴 -->
            <el-submenu v-else :key="'sub-' + route.path" :index="route.path">
              <template slot="title">
                <span>{{ route.meta.title }}</span>
              </template>
              <slot v-for="childRoute in route.children">
                <el-menu-item
                  v-if="childRoute.meta && !childRoute.meta.hidden"
                  :key="childRoute.name"
                  :index="childRoute.name"
                  :route="childRoute"
                >
                  {{ childRoute.meta.title }}<i class="el-icon-arrow-right"></i>
                </el-menu-item>
              </slot>
            </el-submenu>
          </slot>
        </slot>
      </el-menu>

      <div class="aside-btn">
        <button @click="handleLogout()"><img src="@/assets/images/logout.png" alt="">로그아웃</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { PermissionModule } from '@/store/modules/permission';
import { UserModule } from '@/store/modules/user';
import { Vue, Component } from 'vue-property-decorator';
import { RouteConfig } from 'vue-router';

@Component({})
export default class extends Vue {
  get routes() {
    return PermissionModule.routes;
  }

  private getSingleChild(route: RouteConfig): RouteConfig | null {
    const visible = (route.children || []).filter(
      (c) => !c.meta?.hidden,
    );
    return visible.length === 1 ? visible[0] : null;
  }

  private activeMenu(): string {
    return this.$route.name || '';
  }

  private async handleLogout() {
    await UserModule.LogOut();
    this.$router.push({ name: 'Login' });
  }
}
</script>
