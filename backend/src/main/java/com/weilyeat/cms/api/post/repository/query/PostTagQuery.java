package com.ttwijang.cms.api.post.repository.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ttwijang.cms.api.post.dto.PostDto;
import com.ttwijang.cms.api.post.dto.PostTagDto;

@Component
public class PostTagQuery {
    @PersistenceContext
    private EntityManager entityManager;

    public List<PostTagDto.best> list() {
        List<PostTagDto.best> datas = new ArrayList<PostTagDto.best>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT pt.tag, COUNT(pt.tag) \n");
        sql.append("FROM post_tag pt \n");
        sql.append("LEFT JOIN post p ON p.uid = pt.post_uid \n");
        sql.append("WHERE pt.post_uid is not null AND p.delete_status = 0 \n");
        sql.append("GROUP BY tag \n");
        sql.append("ORDER BY COUNT(tag) DESC \n");
        sql.append("LIMIT 3 \n");

        Query query = entityManager.createNativeQuery(sql.toString());

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            PostTagDto.best data = new PostTagDto.best();
            if (row[0] != null) data.setTag(row[0].toString());
            datas.add(data);
        }

        return datas;
    }
}
