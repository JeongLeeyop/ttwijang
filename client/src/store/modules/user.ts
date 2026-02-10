import {
  VuexModule,
  Module,
  Action,
  Mutation,
  getModule,
} from 'vuex-module-decorators';
import {
  getToken, getTokenDecode, setToken, removeToken,
} from '@/utils/cookies';
import {
  getNaverLogin,
  getNaverMe,
  getKakaoLogin,
  getAccessToken,
  getAppleLogin,
  getNaverAccess,
  emailLogin,
} from '@/api/oauth';
import store from '@/store';
import router from '@/router';
import { storageKey } from '@/enums/localStorage';

export interface IUserState {
  token: string
  userId: string
  roles: string[]
  errorMessage: string
}

export interface EmailLoginPayload {
  email: string
  password: string
}

@Module({ dynamic: true, store, name: 'user' })
class User extends VuexModule implements IUserState {
  public isLogin = false;

  public token = getToken() || ''

  public userId = ''

  public infoEmail = ''

  public infoName = ''

  public infoTel = ''

  public roles: string[] = []

  public errorMessage = ''

  @Mutation
  private SET_TOKEN(token: string) {
    this.token = token;
    if ((window as any).jwtSave != null) {
      // (window as any).jwtSave.postMessage(token);
    }
  }

  @Mutation
  private SET_EMAIL(email: string) {
    this.infoEmail = email;
  }

  @Mutation
  private SET_TEL(tel: string) {
    this.infoTel = tel;
  }

  @Mutation
  private SET_NAME(name: string) {
    this.infoName = name;
  }

  @Mutation
  private SET_USER_ID(userId: string) {
    this.userId = userId;
  }

  @Mutation
  private SET_IS_LOGIN(status: boolean) {
    this.isLogin = status;
  }

  @Mutation
  private SET_ROLES(roles: string[]) {
    this.roles = roles;
  }

  @Mutation
  private SET_ERROR_MESSAGE(errorMessage: string) {
    this.errorMessage = errorMessage;
  }

  /**
   * 이메일/비밀번호 로그인
   */
  @Action
  public async EmailLogin(payload: EmailLoginPayload): Promise<void> {
    try {
      const response = await emailLogin(payload.email, payload.password);
      const accessToken = response.data.access_token;

      setToken(accessToken);
      this.SET_TOKEN(accessToken);
      window.localStorage.setItem('jwttoken', accessToken);
      await this.GetUserInfo();
    } catch (error: any) {
      const errorMsg = error.response?.data?.error_description || '로그인에 실패했습니다.';
      this.SET_ERROR_MESSAGE(errorMsg);
      throw new Error(errorMsg);
    }
  }

  @Action
  public async KakaoLogin(code: string) {
    return new Promise((resolve, reject) => {
      getAccessToken(code).then((res) => {
        getKakaoLogin(res.data.access_token).then((jwt) => {
          setToken(jwt.data.access_token);
          this.SET_TOKEN(jwt.data.access_token);
          window.localStorage.setItem('jwttoken', jwt.data.access_token);
          this.GetUserInfo();
          resolve('');
        });
      }).catch((error) => {
        reject(error);
      });
    });
  }

  @Action
  public async NaverLogin(code: string) {
    return new Promise((resolve, reject) => {
      getNaverLogin(code).then((jwt) => {
        // setToken(jwt.data.body.access_token);
        this.SET_TOKEN(jwt.data.body.access_token);
        // this.GetUserInfo();
        resolve('');
      });
    });
  }

  @Action
  public async NaverAccess() {
    return new Promise((resolve, reject) => {
      getNaverAccess(this.token).then((jwt) => {
        setToken(jwt.data.body.access_token);
        this.SET_TOKEN(jwt.data.body.access_token);
        window.localStorage.setItem('jwttoken', jwt.data.body.access_token);
        this.GetUserInfo();
        resolve('');
      });
    });
  }

  @Action
  public async NaverMe() {
      getNaverMe(this.token).then((res) => {
        this.SET_NAME(res.data.body.response.name);
        this.SET_TEL(res.data.body.response.mobile);
        this.SET_EMAIL(res.data.body.response.email);
      });
  }

  @Action
  public async AppleLogin(params: any) {
    return new Promise((resolve, reject) => {
      getAppleLogin(params).then((jwt) => {
        setToken(jwt.data.body.access_token);
        this.SET_TOKEN(jwt.data.body.access_token);
        window.localStorage.setItem('jwttoken', jwt.data.body.access_token);
        this.GetUserInfo();
        resolve('');
      });
    });
  }

  @Action
  public async LoadJwt(jwttoken: any) {
    this.LogOut();
    window.localStorage.setItem('jwttoken', jwttoken);
    setToken(jwttoken);
    this.SET_TOKEN(jwttoken);
    this.GetUserInfo();
  }

  @Action
  public async GetUserInfo() {
    if (this.token) {
      const data: any = getTokenDecode();
      this.SET_ROLES(data.authorities);
      this.SET_USER_ID(data.user_name);
      this.SET_IS_LOGIN(true);
    }
  }

  @Action
  public async LogOut() {
    removeToken();
    window.localStorage.removeItem('jwttoken');
    window.localStorage.removeItem('isRegister');
    window.localStorage.removeItem(storageKey.pickUpPlace);
    window.localStorage.removeItem(storageKey.stationPlace);
    // Reset visited views and cached views
    this.SET_IS_LOGIN(false);
    this.SET_TOKEN('');
    this.SET_ROLES([]);
    this.SET_USER_ID('');
  }

  @Action
  public ResetErrorMessage() {
    this.SET_ERROR_MESSAGE('');
  }
}

export const UserModule = getModule(User);
