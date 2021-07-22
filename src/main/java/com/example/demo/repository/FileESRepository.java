package com.example.demo.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.FileEntityES;

@Repository
public interface FileESRepository extends ElasticsearchRepository<FileEntityES, Long>{
	  
	  List<FileEntityES> findByDescription(String desc);
	}
