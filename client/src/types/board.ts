import { IField } from './field';
import { IBoardCategory } from './boardCategory';

export interface IBoard {
  uid: string
  boardSkin: string
  boardName: string
  headerHtml: string
  footerHtml: string
  fileCountLimit: number
  fileSizeLimit: number
  fileUseState: boolean
  listSize: number
  authRead: string
  authWrite: string
  authRemove: string
  authReply: string
  authComment: string
  privateState: boolean
  replyState: boolean
  commentState: boolean
  secretState: boolean
  noticeStatus: boolean
  applyState: boolean
  favoriteState: boolean
  createDate: string
  fieldList: IBoardField[]
  categoryList: IBoardUseCategory[]
  roleList: string[]
  writeRoles: string[]
}

export interface IBoardField {
  uid: string
  fieldName: string
  fieldType: IField
  inputLimit: number
  fileSizeLimit: number
  requiredState: boolean
  searchState: boolean
  listViewState: boolean
  viewOrder: number
}

export interface IBoardUseCategory {
  boardUseCategoryPk: IBoardUseCategoryPk
}

export interface IBoardUseCategoryPk {
  boardUid: string | null
  category: IBoardCategory
}

export interface IBoardRole {
  boardRolePk: IBoardRolePk
}

export interface IBoardRolePk {
  boardUid: string
  roleCode: string
  action: string
}
