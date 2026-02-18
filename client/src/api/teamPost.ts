import request from '@/utils/request';

const PATH = '/client/post';

// 팀 커뮤니티 게시글 목록 조회
export const getTeamPosts = (teamUid: string, params?: {
  page?: number
  size?: number
  searchType?: string
  searchValue?: string
}) => request({
  url: PATH,
  method: 'get',
  params: {
    teamUid,
    page: (params?.page || 1) - 1,
    size: params?.size || 20,
  },
});

// 팀 커뮤니티 게시글 상세
export const getTeamPostDetail = (postUid: string) => request({
  url: `${PATH}/${postUid}/view`,
  method: 'get',
});

// 팀 커뮤니티 게시글 작성
export interface TeamPostAdd {
  teamUid: string
  title: string
  content: string
  noticeStatus?: boolean
  fileList?: { fileUid: string }[]
}

export const addTeamPost = (data: TeamPostAdd) => request({
  url: PATH,
  method: 'post',
  data,
});

// 팀 커뮤니티 게시글 수정
export interface TeamPostUpdate {
  title: string
  content: string
  noticeStatus?: boolean
  fileList?: { fileUid: string }[]
}

export const updateTeamPost = (postUid: string, data: TeamPostUpdate) => request({
  url: `${PATH}/${postUid}`,
  method: 'put',
  data,
});

// 팀 커뮤니티 게시글 삭제
export const deleteTeamPost = (postUid: string) => request({
  url: `${PATH}/${postUid}`,
  method: 'delete',
});

// 좋아요 토글
export const likeTeamPost = (postUid: string) => request({
  url: `/client/post-like/${postUid}`,
  method: 'post',
});
