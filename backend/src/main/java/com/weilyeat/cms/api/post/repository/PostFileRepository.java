package com.weilyeat.cms.api.post.repository;

import com.weilyeat.cms.entity.PostFile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, String> {
}
