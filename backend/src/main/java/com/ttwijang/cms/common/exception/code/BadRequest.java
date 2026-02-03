package com.ttwijang.cms.common.exception.code;

public enum BadRequest {
    PRODUCT_ORDER_LIMIT("최대 4주치까지만 구매 가능합니다."),
    NOT_MATCH_PRODUCT("일치하는 상품이 없습니다."),
    EXPIRED_COUPON("만료된 쿠폰입니다."),
    ALREADY_JOIN_USER("이미 가입된 유저입니다."),
    NOT_MATCH_PRICE("잘못된 요청입니다."),
    NOT_MINE("잘못된 접근입니다"),
    NOT_VALID_COUPON("유효하지 않은 쿠폰입니다."),
    NOT_ENOUGH_POINT("포인트가 충분하지 않습니다."),
    CANCEL_SETTLE_APPLY("취소된 정산신청입니다."),
    ALREADY_APPLY_SETTLE("이미 정산신청된 주문내역입니다."),
    ALREADY_APPROVAL_SETTLE_APPLY("이미 승인된 정산신청입니다."),
    NOT_PICKUP_ORDER("픽업이 완료되지 않은 주문입니다."),
    NOT_SET_SETTLE_AMOUNT("정산금액 설정이 되어있지 않습니다."),
    EXPIRED_REVIEW_DATE("후기 작성가능일이 지났습니다."),
    ALREADY_POST_REVIEW("이미 후기를 등록했습니다."),
    NOT_VALID_PICKUP_TIME("픽업시간을 선택해주세요."),
    REMAIN_ORDER("픽업하지 않은 주문이 존재합니다."),
    EXPIRED_ID_TOKEN("만료된 ID TOKEN입니다."),
    NOT_VALID_ID_TOKEN("검증되지 않은 ID TOKEN입니다."),
    ALREADY_CANCEL_ORDER("이미 취소된 주문입니다.");
    

    private final String message;

    BadRequest(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
