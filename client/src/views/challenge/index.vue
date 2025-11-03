<template>
  <div v-loading="loading" class="challenge">
		<div class="unid-tab-menu four">
		<li @click="searchChallengeList('')" class="unid-tab-menu-item" :class="{'active': listQuery.categoryUid === '' }">
			<a class="bd7" href="#">전체</a>
		</li>
		<li @click="searchChallengeList(category.uid)" v-for="category in categoryList" :key="category.uid" class="unid-tab-menu-item" :class="{'active': listQuery.categoryUid === category.uid }">
			<a class="bd7" href="#"> {{ category.name }}</a>
		</li>
		</div>
		<div class="challenge__list">
			<div v-for="challenge in challengeList" :key="challenge.uid" class="challenge__item">
				<div class="challenge__item-img">
					<img v-if="challenge.mainImageList[0]" :src="`${apiUrl}/attached-file/${challenge.mainImageList[0].fileUid}`" class="img464-1" alt="">
					<img v-else src="~@/assets/images/tmp_challenge_img.jpg" alt="">
				</div>
				<div class="challenge__item-content">
					<div class="challenge__item-tag">
						<span class="tag tag01">1회 인증시 {{ challenge.dailyReward  }}원</span>
						<span class="tag tag02">{{ challenge.totalDay }}일</span>
					</div>
					<div class="challenge__item-ttl sft">{{ challenge.title }}</div>
					<router-link v-if="!challenge.userJoinStatus" :to="{ name: 'ChallengeDetail', params: { challengeUid : challenge.uid } }" class="challenge__item-button sft">
					<span>챌린지 참여하기</span>
					</router-link>
					<router-link  v-else-if="now >= new Date(challenge.challengeUser.startDate) && new Date(challenge.challengeUser.dueDate) >= now" :to="{ name: 'ChallengeDetail', params: { challengeUid : challenge.uid } }" class="challenge__item-button sft">
					<span>챌린지 참여중</span>
					</router-link>
					<a v-else href="#" class="challenge__item-button sft">
					<span>완료한 챌린지</span>
					</a>
					<div class="challenge__item-participate">
						<img src="~@/assets/images/challenge_person_icon.svg" alt="">
						<p class="challenge__item-participate-txt">{{ challenge.participantCnt | parseKrw }}명 챌린지 참여 중</p>
					</div>
				</div>
			</div>
		</div>
		<Pagination :total="totalElements" :page.sync="listQuery.page" :limit.sync="listQuery.size"
					@pagination="getChallengeList" />
  </div>
</template>

<script lang="ts">
import { Vue, Component } from 'vue-property-decorator';
import { getChallengeList, getChallengeCategory } from '@/api/challenge';
import Pagination from '@/components/Pagination/index.vue';

@Component({
	components: {
		Pagination,
	},
})
export default class extends Vue {
	mounted() {
		this.getChallengeList();
		this.getChallengeCategory();
	}

	private now = new Date();

	private loading = true;

	private apiUrl = process.env.VUE_APP_COMMON_API;

	private totalElements = 0;

	private listQuery = {
		categoryUid: '',
		myFlag: false,
		page: 0,
		size: 5,
	}

	private challengeList: any = [];

	private categoryList: any = [];

	private getChallengeList() {
		if (this.$route.params.myFlag) {
			this.listQuery.myFlag = true;
		}
		getChallengeList(this.listQuery).then((res) => {
			this.challengeList = res.data;
		}).catch(() => {
			this.$message.warning('챌린지 리스트를 조회하는데 실패했습니다.');
		});
		this.loading = false;
	}

	private getChallengeCategory() {
		getChallengeCategory().then((res) => {
			this.categoryList = res.data;
		}).catch(() => {
			this.$message.warning('챌린지 리스트를 조회하는데 실패했습니다.');
		});
		this.loading = false;
	}

	private searchChallengeList(categoryUid: string) {
		this.listQuery.categoryUid = categoryUid;
		this.getChallengeList();
	}
}

</script>
