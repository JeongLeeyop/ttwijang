import {
  VuexModule,
  Module,
  Action,
  Mutation,
  getModule,
} from 'vuex-module-decorators';
import store from '@/store';

interface IOrderForm {
  week: any
  count: number
  amount: number
  usePoint: number
  useCouponIdx: null | number
  pickupTime: string
}

@Module({ dynamic: true, store, name: 'order' })
class Order extends VuexModule {
  public orderForm: IOrderForm = {
    week: {},
    count: 0,
    amount: 0,
    usePoint: 0,
    useCouponIdx: null,
    pickupTime: '',
  }

  public weekflag = 1;

  public weekNum = 1;

  public dayNum = 1;

  public startDate = '';

  public endDate = '';

  public orderInfo: any = {
    amount: 0,
    count: 0,
  }

  @Mutation
  private SET_ORDER_CLEAN() {
    this.orderForm = {
      week: {},
      count: 0,
      amount: 0,
      usePoint: 0,
      useCouponIdx: null,
      pickupTime: '',
    };
  }

  @Mutation
  private SET_ORDER_FORM(item: any) {
    this.orderForm.week[this.weekNum][this.dayNum].push(item);
  }

  @Mutation
  private SET_WEEKNUM(weekNum: number) {
    this.weekNum = weekNum;
  }

  @Mutation
  private SET_WEEKFLAG(weekflag: number) {
    this.weekflag = weekflag;
  }

  @Mutation
  private SET_DAYNUM(num: number) {
    this.dayNum = num;
  }

  @Mutation
  private SET_ORDER_INFO(data: any) {
    this.orderInfo = data;
  }

  @Mutation
  private SET_START_DATE(data: any) {
    this.startDate = data;
  }

  @Mutation
  private SET_END_DATE(data: any) {
    this.endDate = data;
  }

  @Mutation
  private ADD_PRODUCT(data: any) {
    if (!this.orderForm.week[this.weekNum]) {
      this.orderForm.week[this.weekNum] = {
        day: {
          [this.dayNum]: {
            products: {
              [data.product.idx]: {
                product: { ...data.product },
                count: data.count,
              },
            },
            count: 0,
          },
        },
        startDate: this.startDate,
        endDate: this.endDate,
      };
    } else if (!this.orderForm.week[this.weekNum].day[this.dayNum]) {
      this.orderForm.week[this.weekNum].day[this.dayNum] = {
        products: {
          [data.product.idx]: {
            product: { ...data.product },
            count: data.count,
          },
        },
        count: 0,
      };
    } else {
      this.orderForm.week[this.weekNum].day[this.dayNum] = {
        ...this.orderForm.week[this.weekNum][this.dayNum],
        products: {
          ...this.orderForm.week[this.weekNum].day[this.dayNum].products,
          [data.product.idx]: {
            product: { ...data.product },
            count: data.count,
          },
        },
      };
    }
    this.orderForm.week[this.weekNum].day[this.dayNum].count = 0;
    Object.keys(this.orderForm.week[this.weekNum].day[this.dayNum].products).forEach((x) => {
      this.orderForm.week[this.weekNum].day[this.dayNum].count += this.orderForm.week[this.weekNum].day[this.dayNum].products[x].count;
    });
    this.orderForm.amount = 0;
    this.orderForm.count = 0;
    Object.keys(this.orderForm.week).forEach((x) => {
      Object.keys(this.orderForm.week[x].day).forEach((y) => {
        this.orderForm.count += this.orderForm.week[x].day[y].count;
        Object.keys(this.orderForm.week[x].day[y].products).forEach((i) => {
          this.orderForm.amount += this.orderForm.week[x].day[y].products[i].product.price * this.orderForm.week[x].day[y].products[i].count;
        });
      });
    });
  }

  @Mutation
  private SET_PRODUCT(data: any) {
    this.orderForm.week[data.weekNum].day[data.dayNum] = {
      ...this.orderForm.week[data.weekNum][data.dayNum],
      products: {
        ...this.orderForm.week[data.weekNum].day[data.dayNum].products,
        [data.product.idx]: {
          product: { ...data.product },
          count: data.count,
        },
      },
    };

    this.orderForm.week[data.weekNum].day[data.dayNum].count = 0;
    Object.keys(this.orderForm.week[data.weekNum].day[data.dayNum].products).forEach((x) => {
      this.orderForm.week[data.weekNum].day[data.dayNum].count += this.orderForm.week[data.weekNum].day[data.dayNum].products[x].count;
    });
    this.orderForm.amount = 0;
    this.orderForm.count = 0;
    Object.keys(this.orderForm.week).forEach((x) => {
      Object.keys(this.orderForm.week[x].day).forEach((y) => {
        this.orderForm.count += this.orderForm.week[x].day[y].count;
        Object.keys(this.orderForm.week[x].day[y].products).forEach((i) => {
          this.orderForm.amount += this.orderForm.week[x].day[y].products[i].product.price * this.orderForm.week[x].day[y].products[i].count;
        });
      });
    });
  }

  @Mutation
  private CANCEL_PRODUCT(data: any) {
    delete this.orderForm.week[data.weekNum].day[data.dayNum].products[data.product.idx];
    this.orderForm.week[data.weekNum].day[data.dayNum].count = 0;
    Object.keys(this.orderForm.week[data.weekNum].day[data.dayNum].products).forEach((x) => {
      this.orderForm.week[data.weekNum].day[data.dayNum].count += this.orderForm.week[data.weekNum].day[data.dayNum].products[x].count;
    });

    if (this.orderForm.week[data.weekNum].day[data.dayNum].count === 0) {
      delete this.orderForm.week[data.weekNum].day[data.dayNum];
    }

    if (Object.keys(this.orderForm.week[data.weekNum].day).length === 0 && this.orderForm.week[data.weekNum].day.constructor === Object) {
      delete this.orderForm.week[data.weekNum];
    }

    this.orderForm.amount = 0;
    this.orderForm.count = 0;
    Object.keys(this.orderForm.week).forEach((x) => {
      Object.keys(this.orderForm.week[x].day).forEach((y) => {
        this.orderForm.count += this.orderForm.week[x].day[y].count;
        Object.keys(this.orderForm.week[x].day[y].products).forEach((i) => {
          this.orderForm.amount += this.orderForm.week[x].day[y].products[i].product.price * this.orderForm.week[x].day[y].products[i].count;
        });
      });
    });
  }

  @Action
  public setOrderForm(item: any) {
    this.SET_ORDER_FORM(item);
  }

  @Action
  public setWeekNum(weekNum: number) {
    this.SET_WEEKNUM(weekNum);
  }

  @Action
  public setDayNum(num: number) {
    this.SET_DAYNUM(num);
  }

  @Action
  public addProduct(data: any) {
    this.ADD_PRODUCT(data);
  }

  @Action
  public cancelProduct(data: any) {
    this.CANCEL_PRODUCT(data);
  }

  @Action
  public setProduct(data: any) {
    this.SET_PRODUCT(data);
  }

  @Action
  public setOrderInfo(data: any) {
    this.SET_ORDER_INFO(data);
  }

  @Action
  public setStartDate(data: any) {
    this.SET_START_DATE(data);
  }

  @Action
  public setEndDate(data: any) {
    this.SET_END_DATE(data);
  }

  @Action
  public setOrderClean() {
    this.SET_ORDER_CLEAN();
  }
}

export const OrderModule = getModule(Order);
