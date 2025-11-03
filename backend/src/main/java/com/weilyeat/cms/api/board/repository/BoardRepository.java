package com.weilyeat.cms.api.board.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.weilyeat.cms.entity.Board;

public interface BoardRepository extends JpaRepository<Board, String>, QuerydslPredicateExecutor<Board> {

	Board findTopBySkinOrderByCreateDate(String boardType);

    List<Board> findAllByOrderByCreateDate();

}
