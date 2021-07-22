package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long>{
	List<FileEntity> findByDescription(String desc);
}
