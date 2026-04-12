package com.ttwijang.cms.api.faq.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ttwijang.cms.entity.Faq;

@Repository
public interface FaqRepository extends JpaRepository<Faq, String> {
    Optional<Faq> findByUid(String uid);
    List<Faq> findAllByOrderByDisplayOrderAscCreatedDateAsc();
}
