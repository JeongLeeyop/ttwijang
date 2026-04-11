import {
  VuexModule,
  Module,
  Action,
  Mutation,
  getModule,
} from 'vuex-module-decorators';
import { IMenu, IPage } from '@/types/menu';
import store from '@/store';

// export interface IMenu {
//   name: String
//   children?: IMenu[]
// }

export interface IMenuState {
  menuList: IPage[]
}

@Module({ dynamic: true, store, name: 'menu' })
class Menu extends VuexModule implements IMenuState {
  public menuList = [];

  public defaultMenu = {};

  @Mutation
  private SET_MENU(data: any) {
    this.menuList = data;
  }

  @Mutation
  private SET_DEFAULTMENU(data: any) {
    this.defaultMenu = data;
  }

  // @Mutation
  // private ADD_MENU(menuName: any) {
  //   this.menuList.push({
  //     name: menuName,
  //   });
  // }

  @Mutation
  // private ADD_CHILD_MENU(data: any) {
  //   /* eslint no-param-reassign: ["error", { "props": false }] */
  //   if (!data.parent.children) data.parent.children = [];
  //   data.parent.children.push({
  //     name: data.menuName,
  //   });
  //   this.menuList = [...this.menuList];
  // }

  @Mutation
  private DELETE_MENU(data: any) {
    data.parent.splice(data.i, data.i + 1);
  }

  @Action
  public setMenu(data: any) {
    this.SET_MENU(data);
  }

  @Action
  public setDefaultMenu(data: any) {
    this.SET_DEFAULTMENU(data);
  }

  // @Action({ commit: 'ADD_MENU', rawError: true })
  // public addMenu(menuName: string) {
  //   return menuName;
  // }

  @Action({ commit: 'ADD_CHILD_MENU', rawError: true })
  public addChildMenu(data: any) {
    return data;
  }

  @Action({ commit: 'DELETE_MENU', rawError: true })
  public deleteMenu(data: any) {
    return data;
  }
}

export const MenuModule = getModule(Menu);
