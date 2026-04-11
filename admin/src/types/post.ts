export interface IFile {
  fileUid: string | null
  fieldUid: string | null
  viewOrder: number
  file?: {
    byteSize: number
    createDate: string
    fileType: string
    originalName: string
    path: string
    useState: boolean
  }
}

export interface IData {
  uid: string
  fieldUid: string
  inputValue: string
  viewOrder: number
}

export interface IPost {
  uid: string
  boardUid: string
  title: string
  content: string
  parentUid: string
  writer: string
  viewCount?: number
  createDate?: string
  categoryList: []
  fileList: IFile[]
  dataList: IData[]
}
