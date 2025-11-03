<template>
  <div v-loading="loading">
    <BoardDefaultDetail
      v-if="board && board.skin === 'default'"
      :post="post"
      :board="board"
    />
    <BoardConsultDetail
      v-if="board && board.skin === 'consult'"
      :post="post"
      :board="board"
    />
    <BoardQnaDetail
      v-if="board && board.skin === 'qna'"
      :post="post"
      :board="board"
    />
  </div>
</template>

<script lang="ts">
import {
  Vue,
  Component,
Prop,
} from 'vue-property-decorator';
import { getPostView } from '@/api/post';
import { IBoard, IPost } from '@/types';
import BoardDefaultDetail from '@/components/board/default/detail.vue';
import BoardConsultDetail from '@/components/board/consult/detail.vue';
import BoardQnaDetail from '@/components/board/qna/detail.vue';
import { getNewAlarmCount } from '@/api/newAlarm';

@Component({
  name: 'BoardDetail',
  components: {
    BoardDefaultDetail,
    BoardConsultDetail,
    BoardQnaDetail,
  },
})
export default class extends Vue {
  @Prop({ required: true }) private board!: any;

  @Prop({ required: true }) private newAlarmCount!: any;

  mounted() {
    this.loading = true;
    getPostView(this.$route.params.postUid).then((res: any) => {
      this.post = res.data;
      this.loading = false;
      this.$emit('child');
    }).catch(() => {
      this.$router.push({ name: 'BoardIndex', params: { boardUid: this.$route.params.boardUid } });
    });
  }

  private post: any = {
    title: '',
    content: '',
    categoryList: [],
    dataList: [],
    fileList: [],
    createDate: '',
    viewCount: 0,
    likeCount: 0,
    children: [],
    likeStatus: false,
  };

  private loading = true;
}
</script>
