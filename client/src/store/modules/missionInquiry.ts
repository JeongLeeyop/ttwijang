import {
  VuexModule,
  Module,
  Action,
  Mutation,
  getModule,
} from 'vuex-module-decorators';
import store from '@/store';

@Module({ dynamic: true, store, name: 'missionInquiry' })
class MissionInquiry extends VuexModule {
  public missionInquiryForm: any = {
    page1: [],
    page2: [],
    page3: [],
    page4: [],
  };

  @Mutation
  private SET_CLEAN() {
    this.missionInquiryForm = {
      page1: [],
      page2: [],
      page3: [],
      page4: [],
    };
  }

  @Mutation
  private SET_PAGE(data: any) {
    if (data.page === 1) this.missionInquiryForm.page1 = data.content;
    if (data.page === 2) this.missionInquiryForm.page2 = data.content;
    if (data.page === 3) this.missionInquiryForm.page3 = data.content;
    if (data.page === 4) this.missionInquiryForm.page4 = data.content;
  }

  @Mutation
  private SET_PAGE_CLEAN(page: number) {
    if (page === 1) this.missionInquiryForm.page1 = [];
    if (page === 2) this.missionInquiryForm.page2 = [];
    if (page === 3) this.missionInquiryForm.page3 = [];
    if (page === 4) this.missionInquiryForm.page4 = [];
  }

  @Action
  public setPage(data: any) {
    this.SET_PAGE(data);
  }

  @Action
  public setClean() {
    this.SET_CLEAN();
  }

  @Action
  public setPageClean(page: number) {
    this.SET_PAGE_CLEAN(page);
  }
}

export const MissionInquiryModule = getModule(MissionInquiry);
