<template>
	<div>
		<div class="custom join-wrap station">
			<div class="custom__productlist">
				<!-- <div class="unid-tab-menu five">
          <div class="unid-tab-menu-item active">
            <a href="#">전체</a>
          </div>
          <div class="unid-tab-menu-item">
            <a href="#">샐러드</a>
          </div>
          <div class="unid-tab-menu-item">
            <a href="#">도시락</a>
          </div>
          <div class="unid-tab-menu-item">
            <a href="#">반찬</a>
          </div>
          <div class="unid-tab-menu-item">
            <a href="#">간식</a>
          </div>
        </div> -->
				<div class="custom__productlist-list">
					<a v-for="product in products" :key="product.idx" @click="handleChoice(product)" class="custom__productlist-item">
						<div class="custom__productlist-item__img">
							<img v-if="product.thumbUid" :src="`${apiUrl}/attached-file/${product.thumbUid}`" class="img464-1" alt="">
							<img v-else src="~@/assets/images/tmp_custom_productlist_img.svg" alt="">
						</div>
						<div class="custom__productlist-item__ttl">
							{{ product.name }}
						</div>
						<div class="custom__productlist-item__cost">
							{{ product.price | parseKrw }}원
						</div>
					</a>
				</div>
				<Pagination
        :total="totalElements"
        :page.sync="listQuery.page"
        :limit.sync="listQuery.size"
        @pagination="getOrderList()"
      />
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getProductList } from '@/api/product';
import Pagination from '@/components/Pagination/index.vue';
import { storageKey } from '@/enums/localStorage';
import { UserModule } from '@/store/modules/user';

@Component({
	name: 'ProductRotation',
	components: {
		Pagination,
  },
})

export default class extends Vue {
	private loading = true;

	private products: any = [];

	private selectedShop = JSON.parse((window as any).localStorage.getItem(storageKey.pickUpPlace));

	private apiUrl = process.env.VUE_APP_COMMON_API;

	mounted() {
		if (!UserModule.isLogin) {
			this.$message.info('로그인이 필요한 서비스 입니다.');
			this.$router.push({ name: 'Login' });
		}
		this.handleProductList();
	}

	private listQuery = {
		page: 1,
		size: 10,
		searchType: 'name',
		searchValue: '',
		productType: 'STATION',
		extraProductStatus: false,
	}

	private totalElements = 0;

	handleProductList() {
		this.loading = true;
		getProductList(this.listQuery).then((res) => {
			this.totalElements = res.data.totalElements;
			this.products = res.data.content;
			this.loading = false;
		});
	}

	handleChoice(product: any) {
		this.$router.push({
			name: 'CustomProductDetail',
			params: {
				productIdx: product.idx,
			},
		});
	}
}
</script>
