import request from '@/utils/request';

const PATH = '/sms';

export interface SmsRequest {
  phoneNumber: string
}

export interface SmsVerifyRequest {
  phoneNumber: string
  verificationCode: string
}

export interface SmsResponse {
  resultCode: number
  message: string
  msgId?: number
  successCnt?: number
  errorCnt?: number
  msgType?: string
}

/**
 * SMS 인증번호 전송
 */
export const sendVerificationCode = (data: SmsRequest) => request({
  url: `${PATH}/send`,
  method: 'post',
  data,
});

/**
 * SMS 인증번호 검증
 */
export const verifyCode = (data: SmsVerifyRequest) => request({
  url: `${PATH}/verify`,
  method: 'post',
  data,
});
