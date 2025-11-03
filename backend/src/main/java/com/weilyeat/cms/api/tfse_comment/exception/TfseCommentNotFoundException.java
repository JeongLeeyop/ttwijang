package com.weilyeat.cms.api.tfse_comment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TfseCommentNotFoundException extends RuntimeException {
    public TfseCommentNotFoundException() {
        super("댓글을 찾을 수 없습니다.");
    }
}
