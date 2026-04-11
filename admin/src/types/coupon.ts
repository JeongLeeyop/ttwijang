export interface ICoupon {
  // id: number
  dateStatus: String
  dueDay: number
  startDate: String
  downDate: String
  dueDate: String
  title: String
  subTitle: String
  discountRate: number
  discountType: number
  discountPrice: number
  minPrice: number
  eventStatus: String
  discardStatus: String
  event: Object
}

export interface ICouponAdd {
  dateStatus: String
  dueDay: number
  startDate: String
  downDate: String
  dueDate: String
  title: String
  subTitle: String
  discountType: number
  discountRate: number
  discountPrice: number
  minPrice: number
  eventStatus: String
  discardStatus: String
  event: Object
}
