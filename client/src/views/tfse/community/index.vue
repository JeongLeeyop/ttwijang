<template>
  <div v-loading="loading">

		<div id="sub__container">
			<div class="community">
				<div class="community__list">
					<router-link v-for="tfse in tfseList" :key="tfse.idx"
						:to="{ name: 'CommunityDetail', params: { idx: tfse.idx } }" class="community__item">
						<div class="community__top">
							<span class="community__top-time">{{ tfse.writer }} 고객님</span>
							<span class="community__top-time">{{ tfse.tfseDate | parseTimeAt }}</span>
							<div class="community__top-side">
								포만감
								<span class="community__top-side__num">{{ tfse.satietyScore }}</span>
							</div>
						</div>
						<div class="community__content">
							<div class="community__content-ttl sft">{{ tfse.foodText }}</div>
							<div class="community__content-txt">{{ tfse.emotionText }}</div>
							<div class="community__content-add">
								<span class="community__content-add__heart">
									<img src="~@/assets/images/community_add_heart.svg" alt="">
									<!-- <img src="@/assets/images/heart-full.png" alt="좋아요"> -->
									<span class="num">{{ tfse.likeCount }}</span>
								</span>
								<span class="community__content-add__comment">
									<img src="~@/assets/images/community_add_comment_img.svg" alt="">
									<span class="num">{{ tfse.commentCount }}</span>
								</span>
							</div>
						</div>
					</router-link>
				</div>
				<Pagination :total="totalElements" :page.sync="listQuery.page" :limit.sync="listQuery.size"
					@pagination="getTfseCommunityList" />
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getTfseCommunityList } from '@/api/tfse';
import Pagination from '@/components/Pagination/index.vue';

@Component({
	components: {
		Pagination,
	},
})
export default class extends Vue {
	mounted() {
		this.getTfseCommunityList();
	}

	private listQuery = {
		communitySearch: true,
		page: 0,
		size: 5,
	};

	private loading = true;

	private totalElements = 0;

	private tfseList: any = [];

	private getTfseCommunityList() {
		this.loading = true;
		getTfseCommunityList(this.listQuery).then((res) => {
			this.loading = false;
			this.tfseList = res.data.content;
			this.totalElements = res.data.totalElements;
		});
	}
}
</script>
