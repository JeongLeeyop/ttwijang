import request from '@/utils/request';

// 알림 목록 조회
export function getNotifications(params?: {
  page?: number
  size?: number
}) {
  return request({
    url: '/api/notification',
    method: 'get',
    params,
  });
}

// 읽지 않은 알림 개수 조회
export function getUnreadNotificationCount() {
  return request({
    url: '/api/notification/unread-count',
    method: 'get',
  });
}

// 알림 읽음 처리
export function markNotificationAsRead(uid: string) {
  return request({
    url: `/api/notification/${uid}/read`,
    method: 'post',
  });
}

// 전체 알림 읽음 처리
export function markAllNotificationsAsRead() {
  return request({
    url: '/api/notification/read-all',
    method: 'post',
  });
}

// 알림 삭제
export function deleteNotification(uid: string) {
  return request({
    url: `/api/notification/${uid}`,
    method: 'delete',
  });
}

// 타입 정의
export interface Notification {
  uid: string
  userUid: string
  notificationType: 'MATCH' | 'GUEST' | 'TEAM' | 'LEAGUE' | 'CASH' | 'SYSTEM'
  title: string
  message: string
  referenceUid?: string
  referenceType?: string
  isRead: boolean
  createdDate: string
}
