package com.ttwijang.cms.api.attached_file.repository;

import java.util.List;

import com.ttwijang.cms.entity.AttachedFile;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachedFileRepository extends JpaRepository<AttachedFile, String>{
    public List<AttachedFile> findAll();
}
