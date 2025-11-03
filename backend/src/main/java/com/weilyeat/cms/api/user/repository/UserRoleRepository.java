package com.weilyeat.cms.api.user.repository;

import com.weilyeat.cms.entity.Role;
import com.weilyeat.cms.entity.UserRole;
import com.weilyeat.cms.entity.UserRolePk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRolePk>, QuerydslPredicateExecutor<UserRole> {

	void deleteByUserUid(String uid);

	void deleteByRole(Role role);
}
