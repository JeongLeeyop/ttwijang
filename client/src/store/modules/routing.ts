import {
  VuexModule,
  Module,
  Action,
  Mutation,
  getModule,
} from 'vuex-module-decorators';
import store from '@/store';

@Module({ dynamic: true, store, name: 'routing' })
class Routing extends VuexModule {
  public Point: any = {};

  @Mutation
  private SET_POINT(data: any) {
    this.Point = data;
  }

  @Action
  public setPoint(data: any) {
    this.SET_POINT(data);
  }
}

export const RoutingModule = getModule(Routing);
