export interface IRole {
  roleCode?: string
  siteUid?: string
  roleName: string
  description: string | null
  joinAccessState: boolean
  createDate?: string
}
