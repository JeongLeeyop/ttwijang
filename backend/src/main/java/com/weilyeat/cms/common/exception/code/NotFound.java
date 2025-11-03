package com.weilyeat.cms.common.exception.code;

public enum NotFound {
    USER("사용자를 찾을 수 없습니다."),
    SHOP("픽업매장을 찾을 수 없습니다."),
    STATION("거점을 찾을 수 없습니다."),
    SHOP_ITEM("상품을 찾을 수 없습니다."),
    FOOD("음식을 찾을 수 없습니다."),
    DIARY_MEAL("다이어리에 추가된 음식을 찾을 수 없습니다."),
    TFSE("TFSE 일지를 찾을 수 없습니다."),
    SELF_FEEDBACK("TFSE 샐프피드백을 찾을 수 없습니다."),
    MISSION("맞춤미션을 찾을 수 없습니다."),
    CHALLENGE("챌린지를 찾을 수 없습니다."),
    CHALLENGE_RECORD("챌린지 작성 기록을 찾을 수 없습니다."),
    INQUIRY("유저 설문조사 기록을 찾을 수 없습니다."),
    COUPON("쿠폰을 찾을 수 없습니다."),
    REVIEW("리뷰를 찾을 수 없습니다."),
    POST("게시글을 찾을 수 없습니다,"),
    PRODUCT("상품을 찾을 수 없습니다."),
    SETTLE_APPLY("정산 신청을 찾을 수 없습니다."),
    ORDER("주문내역을 찾을 수 없습니다.");

    private final String message;

    NotFound(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
