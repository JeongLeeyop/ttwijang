package com.ttwijang.cms.api.post.repository;

import java.util.List;
import java.util.Optional;

import com.ttwijang.cms.entity.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PostRepository extends JpaRepository<Post, String>, QuerydslPredicateExecutor<Post> {

	void deleteByBoardUid(String boardUid);

	Optional<Post> findTopByOrderByViewOrderDesc();
	
	Optional<List<Post>> findAllByParentUid(String postUid);

	@Modifying
	@Query("DELETE FROM Post p WHERE p.userUid = ?1 OR p.ownerUid = ?1")
    void deleteWithdrawUser(String uid);
	
}
