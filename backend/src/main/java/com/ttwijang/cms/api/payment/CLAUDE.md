# Payment 도메인

## 현재 상태: 대부분 미구현

| 서비스 | 상태 |
|--------|------|
| `PaymentService` | 빈 인터페이스 + 빈 구현체 (완전 미구현) |
| `TossPaymentService` | `payment()`, `refund()` 모두 빈 메서드 |
| `TossPaymentShopService` | 별도 파일 존재 |

## Toss Payments 연동 구조

```
TossPaymentService
├── payment(approval dto) → 카드/가상계좌 결제 승인
│   └── cardPayment() 내부 메서드 존재 (WebClient로 Toss API 호출 코드 있음)
└── refund(orderGroupId, refund dto) → 환불
```

API 엔드포인트:
- 결제 승인: `https://api.tosspayments.com/v1/payments/{paymentKey}`
- 가상계좌 확인: `https://api.tosspayments.com/v1/payments/confirm`

## 관련 엔티티/레포지토리

- `PaymentRequest` — 결제 요청 정보
- `PaymentResultRepository` — 결제 결과 저장
- `TossPaymentShopRepository` — 샵별 결제 정보
- `PointHistoryService` / `PointHistoryRepository` — 결제 후 포인트 적립 연계 예정

## 미완성 기능 (TODO)

- [ ] `PaymentService.payment()` — 결제 승인 로직 구현
- [ ] `PaymentService.refund()` — 환불 로직 구현
- [ ] `TossPaymentService.payment()` — Toss API 실제 호출 (`cardPayment()` 메서드 연결)
- [ ] `TossPaymentService.refund()` — Toss 환불 API 연동
- [ ] 결제 완료 후 `PointHistoryService`로 포인트 적립 처리
- [ ] `GuestApplication.paymentCompleted` 플래그 업데이트 (guest 도메인과 연계)

## 주의사항

- `WebClient` 사용 (Spring WebFlux) — blocking `.block()` 호출 중
- Toss API 인증: `Basic {Base64(secretKey:)}` 헤더
