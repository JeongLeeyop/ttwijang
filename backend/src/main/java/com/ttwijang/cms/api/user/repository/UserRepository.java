package com.ttwijang.cms.api.user.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;
import com.ttwijang.cms.entity.User;
import com.ttwijang.cms.oauth.soical.SocialType;

@Repository
public interface UserRepository extends JpaRepository<User, String>, QuerydslPredicateExecutor<User>{
	Optional<User> findByUserId(String userId);

	Page<User> findAll(Predicate search, Pageable pageable);

	Optional<User> findByUid(String uid);

    Optional<User> findByProviderAndProviderId(SocialType provider, String providerId);

    List<User> findByRolesRoleCode(String roleUid);

    Optional<User> findByUserIdAndUserPassword(String username, String password);

    List<User> findAllByBirth(LocalDate birth);

}
