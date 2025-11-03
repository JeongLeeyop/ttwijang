package com.ttwijang.cms.api.board.repository;

import java.util.List;

import com.ttwijang.cms.entity.BoardCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, String>, QuerydslPredicateExecutor<BoardCategory>{

	List<BoardCategory> findAllByDepthOrderByCreateDate(int depth);

	@Modifying
	@Query("DELETE from BoardCategory bc where bc.uid in ?1")
	void deleteList(String[] deleteList);
}
