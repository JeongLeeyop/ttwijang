package com.ttwijang.cms.api.user.repository;

import com.ttwijang.cms.entity.Role;
import com.ttwijang.cms.entity.UserRole;
import com.ttwijang.cms.entity.UserRolePk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRoleRepository extends JpaRepository<UserRole, String>, QuerydslPredicateExecutor<UserRole> {

	void deleteByUserUid(String uid);

	void deleteByRole(Role role);
}
