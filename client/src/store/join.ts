import {
  VuexModule,
  Module,
  Action,
  Mutation,
  getModule,
} from 'vuex-module-decorators';
import store from '@/store';

export interface IJoinState {
  form: any
}

@Module({ dynamic: true, store, name: 'join' })
class Join extends VuexModule implements IJoinState {
  public form: any = {};

  @Mutation
  private SET_FORM(form: any) {
    this.form = form;
  }

  @Action
  public setForm(form: any) {
    this.SET_FORM(form);
  }
}

export const JoinModule = getModule(Join);
