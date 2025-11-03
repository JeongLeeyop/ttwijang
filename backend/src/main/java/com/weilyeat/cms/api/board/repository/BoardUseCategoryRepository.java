package com.ttwijang.cms.api.board.repository;

import java.util.List;

import com.ttwijang.cms.entity.BoardUseCategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardUseCategoryRepository extends JpaRepository<BoardUseCategory, String>{
	
	@Modifying
	@Query("DELETE FROM BoardUseCategory buc WHERE buc.boardUid = ?1")
	void deleteByBoardUid(String uid);

	@Query("SELECT buc FROM BoardUseCategory buc WHERE buc.boardUid = ?1 ORDER BY buc.viewOrder ASC")
	List<BoardUseCategory> findByBoardUid(String boardUid);

}
