package com.weilyeat.cms.api.post.repository;

import com.weilyeat.cms.entity.BoardCategory;
import com.weilyeat.cms.entity.PostCategory;
import com.weilyeat.cms.entity.PostLike;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostCategoryRepository extends JpaRepository<PostCategory, String>{
    @Query(value = "select * from post_category pc WHERE pc.category_uid IN ?1", nativeQuery = true)
    List<PostCategory> findAllByCategoryUidIn(List<String> uidList);
    
    List<PostCategory> findByCategoryUid(String uid);
}
