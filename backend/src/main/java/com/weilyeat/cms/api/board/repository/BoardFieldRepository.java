package com.weilyeat.cms.api.board.repository;

import java.util.List;

import com.weilyeat.cms.entity.BoardField;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardFieldRepository extends JpaRepository<BoardField, String>{
	
	@Modifying
	@Query("DELETE FROM BoardField bf WHERE bf.uid IN ?1")
	void deleteByDeleteList(String[] deleteFieldList);

	@Query("SELECT bf FROM BoardField bf WHERE bf.boardUid = ?1 ORDER BY bf.viewOrder ASC")
	List<BoardField> findByBoardUid(String boardUid);
}
