import {
  VuexModule,
  Module,
  Action,
  Mutation,
  getModule,
} from 'vuex-module-decorators';
import store from '@/store';

export interface ILayoutState {
  componentName: {
    header: String
    body: String
    Footer: String
  }

  content: String
}

@Module({ dynamic: true, store, name: 'layout' })
class Layout extends VuexModule implements ILayoutState {
  public componentName = {
    header: 'header_left',
    body: 'body_name_a',
    Footer: 'footer_name_a',
  }

  public content = '';

  @Mutation
  private SET_COMPONENT(data: any) {
    this.componentName = {
      ...data,
    };
  }

  @Mutation
  private SET_CONTENT(data: any) {
    this.content = data;
  }

  // @Action
  // public async newSite(data: any) {
  //   this.S
  // }

  @Action
  public setComponent(data: any) {
    this.SET_COMPONENT(data);
  }

  @Action
  public setContent(data: any) {
    this.SET_CONTENT(data);
  }
}

export const LayoutModule = getModule(Layout);
