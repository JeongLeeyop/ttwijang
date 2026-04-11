import {
  VuexModule,
  Module,
  Action,
  Mutation,
  getModule,
} from 'vuex-module-decorators';
import store from '@/store';

export interface IEditState {
  menuChoice: object
  editFormName: String
  layoutEditVisible: Boolean
}

@Module({ dynamic: true, store, name: 'edit' })
class Edit extends VuexModule implements IEditState {
  public menuChoice = {}

  public editFormName = ''

  public layoutEditVisible = false

  @Mutation
  private SET_NEW(data: any) {
    this.menuChoice = data;
  }

  @Mutation
  private SET_EDIT_NEW(data: any) {
    this.editFormName = data;
  }

  @Mutation
  private SET_EDIT_VISIBLE(data: any) {
    this.layoutEditVisible = data;
  }

  @Action
  public setNew(data: any) {
    this.SET_NEW(data);
  }

  @Action
  public setEditNew(data: any) {
    this.SET_EDIT_NEW(data);
  }

  @Action
  public setEditVisible(data: any) {
    this.SET_EDIT_VISIBLE(data);
  }
}

export const EditModule = getModule(Edit);
