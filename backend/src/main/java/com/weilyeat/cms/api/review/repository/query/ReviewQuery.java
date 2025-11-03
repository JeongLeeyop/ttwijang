package com.weilyeat.cms.api.review.repository.query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.weilyeat.cms.api.review.dto.ReviewDto;
import com.weilyeat.cms.oauth.SinghaUser;

@Component
public class ReviewQuery {
    @PersistenceContext
    private EntityManager entityManager;

    public List<ReviewDto.list> list(Pageable pageable, SinghaUser authUser, boolean isOwn) {
        List<ReviewDto.list> datas = new ArrayList<ReviewDto.list>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("    r.idx, \n");
        sql.append("    r.score, \n");
        sql.append("    r.content, \n");
        sql.append("    r.create_date, \n");
        sql.append("    (SELECT file_uid FROM review_photo WHERE review_idx = r.idx LIMIT 1), \n");
        sql.append("    (SELECT COUNT(*) FROM review_like WHERE review_idx = r.idx), \n");
        sql.append("    (SELECT COUNT(*) FROM review_like WHERE review_idx = r.idx AND user_uid = :userUid), \n");
        sql.append("    u.actual_name \n");
        sql.append("FROM review r \n");
        sql.append("LEFT JOIN user u ON u.uid = r.user_uid \n");
        sql.append("WHERE r.delete_status = 0 \n");
        if (isOwn) sql.append("AND r.user_uid = :userUid \n");
        sql.append("LIMIT :startLimit, :endLimit");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("startLimit", pageable.getPageNumber() * pageable.getPageSize());
        query.setParameter("endLimit", pageable.getPageSize());
        if (authUser != null) query.setParameter("userUid", authUser.getUser().getUid());
        else query.setParameter("userUid", "");

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            ReviewDto.list data = new ReviewDto.list();
            data.setIdx(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setScore(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setContent(row[index].toString());
            if (row[++index] != null) data.setCreateDate(row[index].toString());
            if (row[++index] != null) data.setThumbUid(row[index].toString());
            if (row[++index] != null) data.setLikeCount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setLikeStatus(Integer.parseInt(row[index].toString()) > 0);
            if (row[++index] != null) {
                String userName = row[index].toString();
                StringBuilder sb = new StringBuilder(userName);
                for (int i = 1; i < userName.length(); i++) {
                    sb.setCharAt(i, '*');
                }
                data.setUserName(sb.toString());
            }
            datas.add(data);
        }

        return datas;
    }

    public int getTotalCount(Pageable pageable, SinghaUser authUser, boolean isOwn) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("    COUNT(r.idx) \n");
        sql.append("FROM review r \n");
        sql.append("LEFT JOIN user u ON u.uid = r.user_uid \n");
        sql.append("WHERE r.delete_status = 0 \n");
        if (isOwn) sql.append("AND r.user_uid = :userUid \n");

        Query query = entityManager.createNativeQuery(sql.toString());
        if (isOwn) {
            if (authUser != null) query.setParameter("userUid", authUser.getUser().getUid());
            else query.setParameter("userUid", "");
        }
        return ((BigInteger) query.getSingleResult()).intValue();
    }
}
