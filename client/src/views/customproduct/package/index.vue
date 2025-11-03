<template>
	<div>
		<div class="custom join-wrap station">
			<div class="custom__productlist">
				<div class="custom__productlist-list">
					<a v-for="packageItem in packages" :key="packageItem.idx" @click="handleChoice(packageItem)" class="custom__productlist-item">
						<div class="custom__productlist-item__img">
							<img v-if="packageItem.thumbUid" :src="`${apiUrl}/attached-file/${packageItem.thumbUid}`" class="img464-1" alt="">
							<img v-else src="~@/assets/images/tmp_custom_productlist_img.svg" alt="">
						</div>
						<div class="custom__productlist-item__ttl">
							{{ packageItem.name }}
						</div>
						<div class="custom__productlist-item__desc">
							최대 {{ packageItem.maxWeekCount }}주 / 주당 {{ packageItem.deliveryDaysPerWeek }}일
						</div>
						<div class="custom__productlist-item__cost">
							상품 {{ packageItem.selectableProductCount }}개 선택 가능
						</div>
					</a>
				</div>
				<Pagination
					:total="totalElements"
					:page.sync="listQuery.page"
					:limit.sync="listQuery.size"
					@pagination="handlePackageList()"
				/>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { getPackageList } from '@/api/product';
import Pagination from '@/components/Pagination/index.vue';
import { storageKey } from '@/enums/localStorage';
import { UserModule } from '@/store/modules/user';

@Component({
	name: 'PackageList',
	components: {
		Pagination,
	},
})

export default class extends Vue {
	private loading = true;

	private packages: any = [];

	private apiUrl = process.env.VUE_APP_COMMON_API;

	mounted() {
		if (!UserModule.isLogin) {
			this.$message.info('로그인이 필요한 서비스 입니다.');
			this.$router.push({ name: 'Login' });
		}
		this.handlePackageList();
	}

	private listQuery = {
		page: 1,
		size: 10,
		searchType: 'name',
		searchValue: '',
		productType: 'STATION',
	}

	private totalElements = 0;

	handlePackageList() {
		this.loading = true;
		getPackageList(this.listQuery).then((res) => {
			this.totalElements = res.data.totalElements;
			this.packages = res.data.content;
			this.loading = false;
		});
	}

	handleChoice(packageItem: any) {
		this.$router.push({
			name: 'CustomPackageDetail',
			params: {
				packageIdx: packageItem.idx,
			},
		});
	}
}
</script>
