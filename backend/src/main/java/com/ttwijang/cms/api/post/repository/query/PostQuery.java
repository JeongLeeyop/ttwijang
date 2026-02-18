package com.ttwijang.cms.api.post.repository.query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ttwijang.cms.api.post.dto.PostDto;
import com.ttwijang.cms.api.post.dto.search.PostSearch;
import com.ttwijang.cms.oauth.SinghaUser;

@Component
public class PostQuery {
    @PersistenceContext
    private EntityManager entityManager;

    public List<PostDto.ClientList> list(PostSearch search, Pageable pageable, SinghaUser authUser) {
        List<PostDto.ClientList> datas = new ArrayList<PostDto.ClientList>();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("    p.uid, \n"); // 0
        sql.append("    p.board_uid, \n"); // 1
        sql.append("    p.title, \n"); // 2
        sql.append("    p.content, \n"); // 3
        sql.append("    p.writer, \n"); // 4
        sql.append("    p.view_count, \n"); // 5
        sql.append("    p.hidden_status, \n"); // 6
        sql.append("    (SELECT COUNT(*) FROM post_like pl WHERE pl.post_uid = p.uid) as like_count, \n"); // 7
        sql.append("    (SELECT COUNT(*) FROM post_like pl WHERE pl.post_uid = p.uid AND pl.user_uid = :userUid), \n"); // 8
        sql.append("    (SELECT GROUP_CONCAT(bc.name) FROM post_category pc2 JOIN board_category bc ON bc.uid = pc2.category_uid WHERE pc2.post_uid = p.uid), \n"); // 9
        sql.append("    (SELECT COUNT(*) FROM post WHERE parent_uid = p.uid), \n"); // 10
        sql.append("    (SELECT GROUP_CONCAT(DISTINCT pf.file_uid) FROM post_file pf WHERE pf.post_uid = p.uid), \n"); // 11
        sql.append("    (SELECT GROUP_CONCAT(tag) FROM post_tag WHERE post_uid = p.uid), \n"); // 12
        sql.append("    p.create_date, \n"); // 13
        sql.append("    p.notice_status, \n"); // 14
        sql.append("    (SELECT COUNT(*) FROM comment c WHERE c.post_uid = p.uid), \n"); // 15
        sql.append("    p.user_uid \n"); // 16
        sql.append("FROM post p \n");
        if (StringUtils.hasText(search.getTeamUid())) {
            sql.append("WHERE p.team_uid = :teamUid \n");
        } else {
            sql.append("WHERE p.board_uid = :boardUid \n");
        }
        sql.append("AND p.parent_uid is null AND p.delete_status = 0 \n");
        
        sql.append("AND ((p.hidden_status = 1 AND p.user_uid = :userUid) OR p.hidden_status = 0) \n");
        
        if (search.isSecretBoard()) sql.append("AND p.user_uid = :userUid \n");

        if (StringUtils.hasText(search.getPostUid())) sql.append("AND p.uid = :postUid \n");
        
        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("title")) sql.append("AND p.title LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("content")) sql.append("AND p.content LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("titleOrContentOrTag")) {
                if (search.getSearchValue().startsWith("#") && search.getSearchValue().length() >= 2) {
                    sql.append("AND CONCAT('#', pt.tag) LIKE CONCAT ('%', :searchValue, '%') \n");
                } else {
                    sql.append("AND (p.title LIKE CONCAT('%', :searchValue, '%') OR \n");
                    sql.append("p.content LIKE CONCAT('%', :searchValue, '%')) \n");
                }
            }
            
        }

        if (search.getCategoryList().size() > 0) {
            sql.append("AND EXISTS (SELECT 1 FROM post_category pc WHERE pc.post_uid = p.uid AND pc.category_uid IN :categoryList) \n");
        }

        if (StringUtils.hasText(search.getSort()) && search.getSort().equals("hot")) {
            sql.append("ORDER BY like_count DESC, p.create_date DESC \n");
        } else {
            sql.append("ORDER BY p.create_date DESC \n");
        }
        sql.append("LIMIT :startLimit, :endLimit");

        Query query = entityManager.createNativeQuery(sql.toString());
        if (StringUtils.hasText(search.getTeamUid())) {
            query.setParameter("teamUid", search.getTeamUid());
        } else {
            query.setParameter("boardUid", search.getBoardUid());
        }
        query.setParameter("startLimit", pageable.getPageNumber() * pageable.getPageSize());
        query.setParameter("endLimit", pageable.getPageSize());
        if (authUser != null) query.setParameter("userUid", authUser.getUser().getUid());
        else query.setParameter("userUid", "");

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            query.setParameter("searchValue", search.getSearchValue());
        }

        if (StringUtils.hasText(search.getPostUid())) {
            query.setParameter("postUid", search.getPostUid());
        }

        if (search.getCategoryList().size() > 0) {
            query.setParameter("categoryList", search.getCategoryList());
        }

        List<Object[]> resultList = query.getResultList();
        for (Object[] row : resultList) {
            int index = 0;
            PostDto.ClientList data = new PostDto.ClientList();
            data.setUid(row[index].toString());
            if (row[++index] != null) data.setBoardUid(row[index].toString());
            if (row[++index] != null) data.setTitle(row[index].toString());
            if (row[++index] != null) data.setContent(row[index].toString());
            if (row[++index] != null) data.setWriter(row[index].toString());
            if (row[++index] != null) data.setViewCount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setHiddenStatus(row[index].toString().equals("1"));
            if (row[++index] != null) data.setLikeCount(Integer.parseInt(row[index].toString()));
            if (row[++index] != null) data.setLikeStatus(row[index].toString().equals("1"));
            if (row[++index] != null) data.setCategoryList(Arrays.asList(row[index].toString().split(",")));
            if (row[++index] != null) data.setReplyStatus(Integer.parseInt(row[index].toString()) > 0);
            if (row[++index] != null) data.setFileList(Arrays.asList(row[index].toString().split(",")));
            if (row[++index] != null) data.setTags(Arrays.asList(row[index].toString().split(",")));
            if (row[++index] != null) data.setCreateDate(row[index].toString());
            if (row[++index] != null) data.setNoticeStatus(row[index].toString().equals("1") || row[index].toString().equalsIgnoreCase("true"));
            if (row[++index] != null) data.setCommentCount(Integer.parseInt(row[index].toString()));
            String postUserUid = null;
            if (row[++index] != null) {
                postUserUid = row[index].toString();
                data.setUserUid(postUserUid);
            }
            // hasAuthority: 본인 게시글인지 확인
            if (authUser != null && postUserUid != null) {
                data.setHasAuthority(postUserUid.equals(authUser.getUser().getUid()));
            }
            
            datas.add(data);
        }

        return datas;
    }

    public int getTotalCount(PostSearch search, Pageable pageable, SinghaUser authUser) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT \n");
        sql.append("    COUNT(DISTINCT p.uid) \n"); // 0
        sql.append("FROM post p \n");
        sql.append("LEFT JOIN post_category pc ON pc.post_uid = p.uid \n");
        // sql.append("LEFT JOIN post_file pf ON pf.post_uid = p.uid \n");
        sql.append("LEFT JOIN post_tag pt ON pt.post_uid = p.uid \n");
        if (StringUtils.hasText(search.getTeamUid())) {
            sql.append("WHERE p.team_uid = :teamUid \n");
        } else {
            sql.append("WHERE p.board_uid = :boardUid \n");
        }
        sql.append("AND p.parent_uid is null AND p.delete_status = 0 \n");
        sql.append("AND ((p.hidden_status = 1 AND p.user_uid = :userUid) OR p.hidden_status = 0) \n");

        if (search.isSecretBoard()) sql.append("AND p.user_uid = :userUid \n");
        if (StringUtils.hasText(search.getPostUid())) sql.append("AND p.uid = :postUid \n");

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            if (search.getSearchType().equals("title")) sql.append("AND p.title LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("content")) sql.append("AND p.content LIKE CONCAT('%', :searchValue, '%') \n");
            if (search.getSearchType().equals("titleOrContentOrTag")) {
                if (search.getSearchValue().startsWith("#") && search.getSearchValue().length() >= 2) {
                    sql.append("AND CONCAT('#', pt.tag) LIKE CONCAT ('%', :searchValue, '%') \n");
                } else {
                    sql.append("AND (p.title LIKE CONCAT('%', :searchValue, '%') OR \n");
                    sql.append("p.content LIKE CONCAT('%', :searchValue, '%')) \n");
                }
            }
        }
        if (search.getCategoryList().size() > 0) {
            sql.append("AND pc.category_uid IN :categoryList \n");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        if (StringUtils.hasText(search.getTeamUid())) {
            query.setParameter("teamUid", search.getTeamUid());
        } else {
            query.setParameter("boardUid", search.getBoardUid());
        }
        if (authUser != null) query.setParameter("userUid", authUser.getUser().getUid());
        else query.setParameter("userUid", "");

        if (StringUtils.hasText(search.getSearchType()) && StringUtils.hasText(search.getSearchValue())) {
            query.setParameter("searchValue", search.getSearchValue());
        }

        if (search.getCategoryList().size() > 0) {
            query.setParameter("categoryList", search.getCategoryList());
        }

        if (StringUtils.hasText(search.getPostUid())) {
            query.setParameter("postUid", search.getPostUid());
        }

        return ((BigInteger) query.getSingleResult()).intValue();
    }
}
