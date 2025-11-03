import request from '@/utils/request';

const PATH = '/board';

export const getBoard = (uid: string) => request({
  url: `${PATH}/${uid}`,
  method: 'get',
});

export const getInterestBoardUid = () => request({
  url: `${PATH}/interest`,
  method: 'get',
});

export const getBookmarkBoardList = () => request({
  url: `${PATH}/bookmark`,
  method: 'get',
});
