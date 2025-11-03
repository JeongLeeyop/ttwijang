<template>
  <div>
    <div v-if="categoryList.length < 1" id="sch_all">
      <fieldset id="sch_details">
        <legend>통합검색</legend>
        <!-- 분류 선택 -->
        <div class="sort_box">
          <label>분류선택</label>
          <select v-model="search.searchType" class="select">
            <option value="all">전체</option>
            <option value="titleOrContents">제목 + 내용</option>
            <option
              v-for="field in searchFieldList"
              :key="field.uid"
              :value="field.uid"
            >
              {{ field.fieldName }}
            </option>
          </select>
        </div>
        <div class="sch_wr">
          <label for="" class="sound_only">검색어<strong class="sound_only"> 필수</strong></label>
          <input
            type="text"
            v-model="search.searchValue"
            class="sch_input frm_input"
            placeholder="검색어를 입력하세요."
            @keyup.enter="handleSearch()"
          />
          <button class="sch_submit" @click="handleSearch()" >
            <img src="@/assets/img/search_w.png" alt="검색">
            <span class="sound_only">검색</span>
          </button>
        </div>
      </fieldset>
    </div>

    <div v-else id="sgr_all">
      <h3 class="sgr_ch_tt">분야 선택</h3>
      <div class="home_tag_box dragscroll" id="scroller" v-dragscroll>
        <ul>
          <li>
            <input v-model="search.categoryList" type="checkbox" id="check_test1" value="all" @click="handleClickAll()">
            <label for="check_test1"><div class="chk_img">전체</div></label>
          </li>
          <li
            v-for="(category, index) in categoryList[0].boardUseCategoryPk.category.children"
            :key="index"
          >
            <input
              v-model="search.categoryList"
              type="checkbox"
              :id="`category-${index}`"
              :value="category.uid"
              @click="handleClickCategory();"
            />
            <label :for="`category-${index}`"><div class="chk_img">{{ category.categoryName }}</div></label>
          </li>
        </ul>
      </div>
      <div class="btm_wr" v-if="filterSearchType('DATE')">
        <!-- 접수기간 -->
        <div class="date_box">
          <label for="sfl">접수기간</label>
          <div class="dt_wr">
            <p><input type="date" v-model="search.startDate"></p>
            ~
            <p><input type="date" v-model="search.endDate"></p>
          </div>
        </div>
        <!-- 검색 -->
        <div class="search_box">
          <fieldset id="sgr_details">
            <!-- 검색 -->
            <div class="sgr_wr">
              <label for="" class="sound_only">검색어<strong class="sound_only"> 필수</strong></label>
              <input
                type="text"
                v-model="search.searchValue"
                class="sgr_input"
                placeholder="검색어를 입력하세요."
                @keyup.enter="handleSearch()"
              />
              <button type="submit" class="sgr_submit" @click="handleSearch()">
                <img src="@/assets/img/search_w.png" alt="검색"><span class="sound_only">검색</span>
              </button>
            </div>
          </fieldset>
        </div>
      </div>
      <div class="btm_wr" v-else>
        <!-- 검색 -->
        <div class="search_box2">
          <fieldset id="sgr_details">
            <!-- 검색 -->
            <div class="sgr_wr">
              <label for="" class="sound_only">검색어<strong class="sound_only"> 필수</strong></label>
              <input
                type="text"
                v-model="search.searchValue"
                class="sgr_input"
                placeholder="검색어를 입력하세요."
                @keyup.enter="handleSearch()"
              />
              <button type="submit" class="sgr_submit" @click="handleSearch()">
                <img src="@/assets/img/search_w.png" alt="검색"><span class="sound_only">검색</span>
              </button>
            </div>
          </fieldset>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { IBoard } from '@/types';
import { IBoardCategory } from '@/types/boardCategory';
import {
  Vue,
  Component,
  Prop,
  Watch,
} from 'vue-property-decorator';

@Component({
  name: 'BoardSearch',
})
export default class extends Vue {
  @Prop({ required: true }) private fieldList!: any[]

  @Prop({ required: true }) private categoryList!: any[]

  private board: IBoard | null = null;

  private searchFieldList: any[] = [];

  private boardDate = false;

  private search = {
    searchType: 'all',
    searchValue: '',
    startDate: '',
    endDate: '',
    categoryList: [],
    startField: '',
    endField: '',
  };

  @Watch('fieldList')
  private handleChangeMenu() {
    this.init();
    this.search = {
      searchType: 'all',
      searchValue: '',
      startDate: '',
      endDate: '',
      categoryList: [],
      startField: '',
      endField: '',
    };
  }

  mounted() {
    this.init();
  }

  private init() {
    if (this.$route.query) {
      this.search = {
        ...this.search,
        ...this.$route.query,
      };
      if (this.$route.query.categoryList && typeof (this.$route.query.categoryList) === 'string') {
        this.search.categoryList = (this.$route.query.categoryList as any).split(',');
      }
    }
    if (this.fieldList) {
      this.searchFieldList = this.fieldList.filter((field: any) => {
        const searchField = field.searchState;
        return searchField;
      });
    }
  }

  private filterSearchType(type: string) {
    let rt = false;
    this.fieldList.forEach((flist: any) => {
      if (flist.fieldType.typeCode === type) rt = true;
    });
    return rt;
  }

  private handleSearch() {
    if (this.fieldList.length > 4) {
      this.search = {
        ...this.search,
        startField: (this.fieldList[3] as any).uid,
        endField: (this.fieldList[4] as any).uid,
      };
    }
    this.$emit('search', this.search);
  }

  private handleClickAll() {
    setTimeout(() => {
      if ((this.search.categoryList as string[]).indexOf('all') < 0) {
        this.search.categoryList = [];
        this.handleSearch();
      } else {
        this.categoryList.forEach((category: any) => {
          category.boardUseCategoryPk.category.children.forEach((c: IBoardCategory) => {
            (this.search.categoryList as string[]).push(c.uid);
          });
        });
        setTimeout(() => {
          this.handleSearch();
        }, 300);
      }
    }, 100);
  }

  private handleClickCategory() {
    setTimeout(() => {
      if (this.categoryList.length <= this.search.categoryList.length && (this.search.categoryList as string[]).indexOf('all') > -1) {
        const allIndex = (this.search.categoryList as string[]).indexOf('all');
        this.search.categoryList.splice(allIndex, 1);
      }
      this.handleSearch();
    }, 300);
  }
}
</script>
