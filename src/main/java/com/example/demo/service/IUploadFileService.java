package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.UpdateFileDTO;
import com.example.demo.entity.FileEntity;
import com.example.demo.entity.FileEntityES;

public interface IUploadFileService {
	public String uploadFile(UpdateFileDTO updateFileDTO);
	public String deleteFile(String nameFile, Long id);
	public String updateFile(UpdateFileDTO updateFileDTO);
	
	public List<FileEntityES> searchFileByDescriptionES(String desc);
	public List<FileEntity> searchFileByDescription(String desc);
	public String compareSearch(String desc);
}
