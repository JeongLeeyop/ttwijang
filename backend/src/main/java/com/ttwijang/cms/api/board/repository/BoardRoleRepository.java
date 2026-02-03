package com.ttwijang.cms.api.board.repository;

import com.ttwijang.cms.entity.BoardRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRoleRepository extends JpaRepository<BoardRole, String>{

	@Modifying
	@Query("DELETE FROM BoardRole br WHERE br.boardUid = ?1")
	void deleteByBoardUid(String uid);
}
