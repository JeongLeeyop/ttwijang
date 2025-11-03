<template>
  <div class="pg_wrap">
    <ul class="pagination">
      <li class="page-item">
        <a href="#app" class="page-link" @click="handleClickPage(1)">
          <i class="fas fa-angle-double-left"></i><span class="sound_only">첫번째페이지</span>
        </a>
      </li>
      <li class="page-item"><a href="#app" class="page-link" @click="handleClickPrev"><i class="fas fa-angle-left"></i><span class="sound_only">이전페이지</span></a></li>
      <li
        v-for="(page, index) in (1, getEndRange())"
        :key="page"
        class="page-item"
        :class="Number(listQuery.page) + 1 === getStartPage() + index ? 'active' : ''"
      >
        <a href="#app" class="page-link" @click="handleClickPage(getStartPage() + index)">
          {{ getStartPage() + index }}
        </a>
      </li>
      <li class="page-item"><a href="#app" class="page-link" @click="handleClickNext"><i class="fas fa-angle-right"></i><span class="sound_only">다음페이지</span></a></li>
      <li class="page-item">
        <a href="#app" class="page-link" @click="handleClickPage(totalPages)">
          <i class="fas fa-angle-double-right"></i><span class="sound_only">마지막페이지</span>
        </a>
      </li>
    </ul>
  </div>
</template>

<script lang="ts">
import { Vue, Component, Prop } from 'vue-property-decorator';

@Component({
  name: 'BoardPagination',
})
export default class extends Vue {
  @Prop({ required: true }) private listQuery!: any

  @Prop({ required: true }) private listTotal!: number

  @Prop({ required: true }) private totalPages!: number

  private getStartPage() {
    return Math.floor(this.listQuery.page / 10) * 10 + 1;
  }

  private getEndPage() {
    if (this.totalPages === 0) return 1;
    if (this.getStartPage() + 9 < this.totalPages) return this.getStartPage() + 9;
    return this.totalPages;
  }

  private getEndRange() {
    if (this.getEndPage() < 11 && this.totalPages > 10) return 10;
    const endRange = this.getEndPage() % 10;
    if (endRange === 0) return 10;
    return endRange;
  }

  private handleClickPage(page: number) {
    this.$emit('change', page - 1);
  }

  private handleClickPrev() {
    if (this.getStartPage() === 1) this.$emit('change', 0);
    else this.$emit('change', this.getStartPage() - 2);
  }

  private handleClickNext() {
    if (this.getEndPage() !== this.totalPages) this.$emit('change', this.getEndPage());
    else this.$emit('change', this.totalPages - 1);
  }
}
</script>
