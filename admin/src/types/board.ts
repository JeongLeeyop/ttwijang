export interface IField {
  uid: string
  fieldName: string
  fieldTypeCode: string
  inputLimit: number
  fileSizeLimit: number
  requiredState: boolean
  searchState: boolean
}

export interface IFieldType {
  typeCode: string
  typeName: string
  viewOrder: Number
}

export interface ICategory {
  uid: string
  name: string
  descript: string
  createDate: string
  depth: number
  children: ICategory[]
}

export interface IBoardSkin {
  code: string
  name: string
}

export interface IBoard {
  uid: string
  skin: string
  name: string
  fileUseState: boolean
  fileCountLimit: number
  fileSizeLimit: number
  listSize: number
  authRead: string
  authWrite: string
  authReply: string
  authComment: string
  privateState: boolean
  replyState: boolean
  commentState: boolean
  secretState: boolean
  noticeState: boolean
  createDate: string
  fieldList: IField[]
  categoryList: []
  roleList: []
  boardSkin?: IBoardSkin
}

export interface IComment {
  uid: string
  userUid: string
  writer: string
  contents: string | null
  depth: number
  viewOrder: number
  postTitle: string | null
  hide: boolean
  createDate: string
}
