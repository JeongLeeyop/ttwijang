import {
 Module, VuexModule, Mutation, Action,
} from 'vuex-module-decorators';
import {
  getWallet,
  chargeCash,
  useCash,
  getTransactions,
  sponsorTeam,
  getMySponsorships,
  ChargeRequest,
  UseRequest,
  SponsorshipRequest,
  Wallet,
  Transaction,
  Sponsorship,
} from '@/api/cash';

export interface CashState {
  wallet: Wallet | null
  transactions: Transaction[]
  mySponsorships: Sponsorship[]
  loading: boolean
  error: string | null
}

@Module({ namespaced: true, name: 'cash' })
export default class CashModule extends VuexModule implements CashState {
  wallet: Wallet | null = null;

  transactions: Transaction[] = [];

  mySponsorships: Sponsorship[] = [];

  loading = false;

  error: string | null = null;

  get balance(): number {
    return this.wallet?.balance ?? 0;
  }

  get hasWallet(): boolean {
    return this.wallet !== null;
  }

  @Mutation
  SET_WALLET(wallet: Wallet | null): void {
    this.wallet = wallet;
  }

  @Mutation
  SET_TRANSACTIONS(transactions: Transaction[]): void {
    this.transactions = transactions;
  }

  @Mutation
  SET_MY_SPONSORSHIPS(sponsorships: Sponsorship[]): void {
    this.mySponsorships = sponsorships;
  }

  @Mutation
  SET_LOADING(loading: boolean): void {
    this.loading = loading;
  }

  @Mutation
  SET_ERROR(error: string | null): void {
    this.error = error;
  }

  @Action
  async fetchWallet(): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getWallet();
      this.SET_WALLET(response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '지갑 정보를 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async charge(data: ChargeRequest): Promise<Transaction> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await chargeCash(data);
      await this.fetchWallet();
      return response.data;
    } catch (error: any) {
      this.SET_ERROR(error.message || '충전에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async use(data: UseRequest): Promise<Transaction> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await useCash(data);
      await this.fetchWallet();
      return response.data;
    } catch (error: any) {
      this.SET_ERROR(error.message || '캐시 사용에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchTransactions(): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getTransactions();
      this.SET_TRANSACTIONS(response.data.content || response.data);
    } catch (error: any) {
      this.SET_ERROR(error.message || '거래 내역을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async sponsor(data: SponsorshipRequest): Promise<Sponsorship> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await sponsorTeam(data);
      await this.fetchWallet();
      return response.data;
    } catch (error: any) {
      this.SET_ERROR(error.message || '후원에 실패했습니다.');
      throw error;
    } finally {
      this.SET_LOADING(false);
    }
  }

  @Action
  async fetchMySponsorships(): Promise<void> {
    this.SET_LOADING(true);
    this.SET_ERROR(null);
    try {
      const response = await getMySponsorships();
      this.SET_MY_SPONSORSHIPS(response.data || []);
    } catch (error: any) {
      this.SET_ERROR(error.message || '후원 내역을 불러오는데 실패했습니다.');
    } finally {
      this.SET_LOADING(false);
    }
  }
}
