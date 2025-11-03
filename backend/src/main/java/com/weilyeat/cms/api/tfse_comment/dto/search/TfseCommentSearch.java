package com.ttwijang.cms.api.tfse_comment.dto.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QTfseComment;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TfseCommentSearch {
    private String searchType;
    private String searchValue;
    private Long tfseIdx;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QTfseComment tfseComment = QTfseComment.tfseComment;

        if (StringUtils.hasText(searchType) && StringUtils.hasText(searchValue)) {
            switch (searchType) {
                case "contents":
                    builder.and(tfseComment.contents.contains(searchValue));
                    break;
                case "writer":
                    builder.and(tfseComment.writer.contains(searchValue));
                default:
                    break;
            }
        }

        if (tfseIdx != null) builder.and(tfseComment.tfseIdx.eq(tfseIdx));
        builder.and(tfseComment.depth.eq(1));
        return builder;
    }
}
