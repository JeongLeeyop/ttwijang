import request from '@/utils/request';

export interface Faq {
  uid: string
  question: string
  answer: string
  category: string | null
  displayOrder: number
  createdDate: string
}

export function getFaqList() {
  return request({ url: '/faq', method: 'get' });
}

// 관리자 전용
export function createFaq(data: { question: string, answer?: string, category?: string, displayOrder?: number }) {
  return request({ url: '/admin/faq', method: 'post', data });
}

export function updateFaq(uid: string, data: { question?: string, answer?: string, category?: string, displayOrder?: number }) {
  return request({ url: `/admin/faq/${uid}`, method: 'put', data });
}

export function deleteFaq(uid: string) {
  return request({ url: `/admin/faq/${uid}`, method: 'delete' });
}
