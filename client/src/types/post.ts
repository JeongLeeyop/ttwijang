import { IBoardCategory } from './boardCategory';
import { IBoardField } from './board';
// import { IAttachedFile } from './attachedFile';

export interface IPost {
  uid: string
  boardUid: string
  parentUid: string | null
  userUid: string
  writer: string
  title: string
  content: string
  viewCount: number
  depth: number
  categoryList: IPostCategory[]
  fileList: IPostFile[]
  dataList: IPostData[]
  createDate: string
}

export interface IPostAdd {
  boardUid: string
  title: string
  content: string
  parentUid: string | null
  writer: string | null
  password: string
  categoryList: IPostCategory[]
  fileList: IPostFile[]
  dataList: IPostData[]
  noticeStatus: boolean
}

export interface IPostUpdate {
  boardUid: string
  title: string
  content: string
  parentUid: string | null
  writer: string | null
  password: string
  categoryList: IPostCategory[]
  fileList: IPostFile[]
  dataList: IPostData[]
  noticeStatus: boolean
}

export interface IPostCategory {
  postCategoryPk: IPostCategoryPk
  topUid: string
}

export interface IPostCategoryPk {
  postUid: string
  category: IBoardCategory
}

export interface IPostData {
  postDataPk: IPostDataPk
  inputValue: string | null
}

export interface IPostDataPk {
  postUid: string
  field: IBoardField
}

export interface IPostFile {
  postFilePk: IPostFilePk
  fieldUid: string | null
  viewOrder: number
}

export interface IPostFilePk {
  postUid: string
  // file: IAttachedFile
}
