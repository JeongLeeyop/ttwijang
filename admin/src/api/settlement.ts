import request from '@/utils/request';

export function getSettlementConfig() {
  return request({ url: '/admin/settlement/config', method: 'get' });
}

export function updateSettlementConfig(data: { settlementDays?: number, description?: string }) {
  return request({ url: '/admin/settlement/config', method: 'put', data });
}

export function getMonthlySummary(period: string) {
  return request({ url: '/admin/settlement/monthly', method: 'get', params: { period } });
}

export function getModalItems(teamUid: string, period: string) {
  return request({ url: `/admin/settlement/monthly/${teamUid}/items`, method: 'get', params: { period } });
}

export function getDetailItems(params: {
  teamName?: string
  startDate: string
  endDate: string
  page?: number
  size?: number
}) {
  return request({ url: '/admin/settlement/items', method: 'get', params });
}

export function getSettlementHistory(params: { page?: number, size?: number }) {
  return request({ url: '/admin/settlement/history', method: 'get', params });
}

export function createSettlement(data: { teamUid: string, period: string }) {
  return request({ url: '/admin/settlement', method: 'post', data });
}

export function completeSettlement(uid: string, data: { adminNote?: string }) {
  return request({ url: `/admin/settlement/${uid}/complete`, method: 'put', data });
}
