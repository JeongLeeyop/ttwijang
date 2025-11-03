export interface IBoardCategory {
  uid: string
  categoryName: string
  categoryDescript: string
  children: IBoardCategory[]
  useState: boolean
  createDate: string
  depth: number
  viewOrder: number
}
