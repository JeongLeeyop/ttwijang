import {
  VuexModule,
  Module,
  Action,
  Mutation,
  getModule,
} from 'vuex-module-decorators';
// import { getAuthorizationCode, REDIRECT_URL, SERVER_URL } from '@/api/login';
import {
  getToken,
  getTokenDecode,
  setToken,
  removeToken,
} from '@/utils/cookies';
import store from '@/store';
import { login } from '@/api/user';
import { Message } from 'element-ui';
import router from '@/router';

export interface IUserState {
  token: string
  userId: string
  roles: string[]
}

@Module({ dynamic: true, store, name: 'user' })
class User extends VuexModule implements IUserState {
  public token = getToken() || ''

  public userId = ''

  public roles: string[] = []

  @Mutation
  private SET_TOKEN(token: string) {
    this.token = token;
  }

  @Mutation
  private SET_USER_ID(userId: string) {
    this.userId = userId;
  }

  @Mutation
  private SET_ROLES(roles: string[]) {
    console.log(roles);
    this.roles = roles;
  }

  @Action
  public async Login(userInfo: { username: string, password: string}) {
    const { username, password } = userInfo;
    await login({ username, password }).then((res) => {
      setToken(res.data.access_token);
      this.context.commit('SET_TOKEN', res.data.access_token);
    }).catch((error : any) => {
      /* eslint-disable */
      const data = error.response.data;
      Message.error(data.error_description || data.message);
      /* eslint-enable */
    });
  }

  @Action
  public ResetToken() {
    removeToken();
    this.context.commit('SET_TOKEN', '');
    this.context.commit('SET_USER_ID', '');
    this.context.commit('SET_ROLES', []);
  }

  @Action
  public async GetUserInfo() {
    if (this.token === '') {
      this.ResetToken();
      if (router.currentRoute.name !== 'Login') {
        router.push({ name: 'Login' }).catch(() => {});
      }
      throw Error('Token is empty');
    }
    // tokenCheck(this.token).catch(() => {
    //   throw Error('Verification failed, please Login again.');
    // });
    const data: any = getTokenDecode();
    if (!data) {
      this.ResetToken();
      if (router.currentRoute.name !== 'Login') {
        router.push({ name: 'Login' }).catch(() => {});
      }
      throw Error('Invalid token');
    }
    const whiteRoles = ['ROLE_ADMIN'];
    let hasRole = false;
    hasRole = data.authorities?.some((role: any) => whiteRoles.indexOf(role) > -1) || false;
    if (!data.authorities || data.authorities.length <= 0 || !hasRole) {
      this.ResetToken();
      if (router.currentRoute.name !== 'Login') {
        router.push({ name: 'Login' }).catch(() => {});
      }
      throw Error('No valid role');
    }
    this.context.commit('SET_ROLES', data.authorities);
    this.context.commit('SET_USER_ID', data.user_name);
  }

  @Action
  public async LogOut() {
    if (this.token === '') {
      throw Error('LogOut: token is undefined!');
    }
    removeToken();

    // Reset visited views and cached views
    this.context.commit('SET_TOKEN', '');
    this.context.commit('SET_ROLES', []);
    this.context.commit('SET_USER_ID', '');
  }
}

export const UserModule = getModule(User);
