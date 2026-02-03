package com.ttwijang.cms.api.comment.dto.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.QComment;

import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentSearch {
    private String searchType;
    private String searchValue;
    private String postUid;

    public Predicate search() {
        BooleanBuilder builder = new BooleanBuilder();
        QComment comment = QComment.comment;

        if (StringUtils.hasText(searchType) && StringUtils.hasText(searchValue)) {
            switch (searchType) {
                case "contents":
                    builder.and(comment.contents.contains(searchValue));
                    break;
                case "writer":
                    builder.and(comment.writer.contains(searchValue));
                default:
                    break;
            }
        }

        if (StringUtils.hasText(postUid)) builder.and(comment.postUid.eq(postUid));
        
        return builder;
    }
}
