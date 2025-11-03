<template>
  <div>
    <BoardHeader :board="board" :newAlarmCount="newAlarmCount"/>
    <router-view v-if="board.skin !== ''" :board="board" :newAlarmCount="newAlarmCount" @child="refreshAlarm" />
    <BoardFooter :board="board" />
  </div>
</template>

<script lang="ts">
import { Vue, Component, Watch } from 'vue-property-decorator';
import BoardHeader from '@/Layout/components/boardHeader.vue';
import BoardFooter from '@/Layout/components/boardFooter.vue';
import { getBoard } from '@/api/board';
import { getNewAlarmCount } from '@/api/newAlarm';

@Component({
  name: 'BoardLayout',
  components: {
    BoardHeader,
    BoardFooter,
  },
})
export default class extends Vue {
  @Watch('$route.name')
  private handleChangeRouteName() {
    this.getBoard();
  }

  mounted() {
    this.getBoard();
    this.getNewAlarmCount();
  }

  private newAlarmCount = 0;

  private board = {
    name: '',
    skin: '',
    headerHtml: '',
    footerHtml: '',
  }

  private refreshAlarm() {
    this.getNewAlarmCount();
  }

  private getNewAlarmCount() {
    getNewAlarmCount().then((res) => {
      this.newAlarmCount = res.data;
    });
  }

  private getBoard() {
    getBoard(this.$route.params.boardUid).then((res) => {
      this.board = res.data;
    });
  }
}
</script>
