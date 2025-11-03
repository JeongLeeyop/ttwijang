<template>
	<div v-loading="loading">
		<div id="sub__container">
			<div class="community__detail">
				<div class="community__list">
					<div class="community__item">
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
									<img v-if="tfse.likeStatus" src="@/assets/images/heart-full.png" alt="좋아요" @click="handleLikeTfse(tfse)">
									<img v-else src="@/assets/images/heart.png" alt='좋아요' @click="handleLikeTfse(tfse)">
									<!-- <img src="~@/assets/images/community_add_heart.svg" alt='' /> -->
									<span class="num">{{ tfse.likeCount }}</span>
								</span>
								<span class="community__content-add__comment">
									<img src="~@/assets/images/community_add_comment_img.svg" alt="" />
									<span class="num">{{ tfse.commentCount }}</span>
								</span>
							</div>
						</div>
						<div v-if="completeFlag" class="community__comment">
							<div class="community__comment-count bd7">댓글 <span class="num">{{ tfse.commentCount }}</span>
							</div>
							<div v-for="comment in commentList" :key="comment.uid" class="community__comment-list">
								<div class="community__comment-item">
									<!-- <div class="community__comment-item__profile"></div> -->
									<div class="community__comment-item__content">
										<!-- <p class="community__comment-item__best">베스트리뷰</p> -->
										<p class="community__comment-item__nickname bd6">{{ comment.writer }} 고객님</p>
										<p class="community__comment-item__txt">{{ comment.contents }}</p>
										<p class="community__comment-item__date bd3">{{ comment.createDate | parseDate }}</p>
										<button @click="replyHandler(comment.uid, $event)" class="community__comment-item__button reply">+
											대댓글 달기</button>
											<button @click="replyCancelHandler($event)" class="community__comment-item__button cancel none">
												대댓글 취소</button>
										<button v-if="comment.hasAuthority" @click="deleteHandler(comment.uid)" class="community__comment-item__button delete">삭제</button>
									</div>
								</div>
								<div v-for="reply in comment.children" :key="reply.uid"
									class="community__comment-item community__comment-item--v2">
									<!-- <div class="community__comment-item__profile"></div> -->
									<div class="community__comment-item__content">
										<!-- <p class="community__comment-item__best">베스트리뷰</p> -->
										<p class="community__comment-item__nickname bd6">{{ reply.writer }} 고객님</p>
										<p class="community__comment-item__txt">{{ reply.contents }}</p>
										<p class="community__comment-item__date bd3">{{ reply.createDate | parseDate }}</p>
										<button v-if="reply.hasAuthority" @click="deleteHandler(reply.uid)" class="community__comment-item__date delete">삭제</button>
									</div>
								</div>
							</div>
						</div>
						<Pagination :total="totalElements" :page.sync="listQuery.page" :limit.sync="listQuery.size"
							@pagination="getCommentList" />
					</div>
				</div>
				<div class="community__comment-input">
					<input ref="contents" v-model="inputComment.contents" type="text" placeholder="댓글을 입력해주세요">
					<button @click="addComment" class="community__comment-input__send"></button>
				</div>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { UserModule } from '@/store/modules/user';
import { Vue, Component } from 'vue-property-decorator';
import { getTfse } from '@/api/tfse';
import { getCommentList, addComment, deleteComment } from '@/api/tfseComment';
import { likeTfse } from '@/api/tfseLike';
import Pagination from '@/components/Pagination/index.vue';

@Component({
	components: {
		Pagination,
	},
})
export default class extends Vue {
	mounted() {
		this.getTfse();
		this.getCommentList();
	}

	private tfse: any = {
		idx: '',
		userUid: '',
		tfseDate: '',
		foodText: '',
		emotionText: '',
		satietyScore: '',
		secretStatus: '',
		likeCount: '',
		createDate: '',
		commentCount: '',
	};

	private commentList: any = {
		uid: '',
		tfseIdx: '',
		userUid: '',
		writer: '',
		contents: '',
		depth: '',
		viewOrder: '',
		hide: '',
		createDate: '',
		children: {},
	}

	private inputComment: any = {
		tfseIdx: this.$route.params.idx,
		contents: '',
		parentUid: '',
	}

	private listQuery = {
		tfseIdx: this.$route.params.idx,
		page: 0,
		size: 10,
	};

	private totalElements = 0;

	private loading = true;

	private completeFlag = false;

	private getTfse() {
		this.loading = true;
		getTfse(this.$route.params.idx).then((res) => {
			this.loading = false;
			this.tfse = res.data;
		});
	}

	private getCommentList() {
		this.loading = true;
		getCommentList(this.listQuery).then((res) => {
			this.commentList = res.data.content;
			this.totalElements = res.data.totalElements;
			this.loading = false;
			this.completeFlag = true;
		});
	}

	private replyHandler(parentUid: String, event: any) {
		if (this.$refs.contents) {
			(this.$refs.contents as HTMLElement).focus();
			event.target.classList.add('none');
			event.target.nextSibling.classList.remove('none');
			if (event.target.nextSibling.nextSibling.classList) {
				event.target.nextSibling.nextSibling.classList.add('none');
			}
			this.inputComment.parentUid = parentUid;
		}
	}

	private replyCancelHandler(event:any) {
		if (this.$refs.contents) {
			event.target.previousSibling.classList.remove('none');
			event.target.classList.add('none');
			if (event.target.nextSibling.classList) {
				event.target.nextSibling.classList.remove('none');
			}
			this.inputComment.parentUid = '';
		}
	}

	private deleteHandler(uid: string) {
		this.$confirm('정말 댓글을 삭제하시겠습니까?').then(() => {
			this.loading = true;
			deleteComment(uid).then(() => {
				this.$message.success('댓글이 삭제되었습니다.');
				this.getTfse();
				this.getCommentList();
				this.inputComment.contents = '';
				this.inputComment.parentUid = '';
				this.loading = false;
				this.resetBtn();
			}).catch((error) => {
				this.$message.error(error.response.data.message || '게시글을 삭제하는데 실패했습니다.');
			});
		});
	}

	private handleLikeTfse(tfse: any) {
		if (UserModule.isLogin) {
			likeTfse(tfse.idx).then(() => {
			tfse.likeStatus = !tfse.likeStatus;
			if (tfse.likeStatus) tfse.likeCount += 1;
			else tfse.likeCount -= 1;
		});
		} else {
			this.$message.info('로그인이 필요한 기능입니다');
			this.$router.push({ name: 'Login' });
		}
	}

	private addComment() {
		if (UserModule.isLogin) {
			this.loading = true;
			addComment(this.inputComment).then(() => {
				this.getTfse();
				this.getCommentList();
				this.inputComment.contents = '';
				this.inputComment.parentUid = '';
				this.loading = false;
				this.resetBtn();
			});
		} else {
			this.$message.info('로그인이 필요한 기능입니다');
			this.$router.push({ name: 'Login' });
		}
	}

	private resetBtn() {
		const cancelElements = document.querySelectorAll('.community__comment-item__button.cancel');
			cancelElements.forEach((element) => {
				element.classList.add('none');
			});
			const replyElements = document.querySelectorAll('.community__comment-item__button.reply');
			replyElements.forEach((element) => {
				element.classList.remove('none');
			});
			const deleteElements = document.querySelectorAll('.community__comment-item__button.delete');
			deleteElements.forEach((element) => {
				element.classList.remove('none');
			});
	}
}
</script>
