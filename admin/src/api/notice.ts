import request from '@/utils/request';

export function getNoticeList() {
  return request({ url: '/notice', method: 'get' });
}

export function createNotice(data: { title: string, content?: string, pinned?: boolean }) {
  return request({ url: '/admin/notice', method: 'post', data });
}

export function updateNotice(uid: string, data: { title?: string, content?: string, pinned?: boolean }) {
  return request({ url: `/admin/notice/${uid}`, method: 'put', data });
}

export function deleteNotice(uid: string) {
  return request({ url: `/admin/notice/${uid}`, method: 'delete' });
}
