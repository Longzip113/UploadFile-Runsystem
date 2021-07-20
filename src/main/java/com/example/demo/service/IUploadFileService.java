package com.example.demo.service;

import com.example.demo.dto.UpdateFileDTO;

public interface IUploadFileService {
	public String uploadFile(UpdateFileDTO updateFileDTO);
	public String deleteFile(String nameFile, Long id);
	public String updateFile(UpdateFileDTO updateFileDTO);
}
