package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.UpdateFileDTO;
import com.example.demo.service.IUploadFileService;

@RestController
@RequestMapping("api/")
public class FileController {

	@Autowired
	IUploadFileService uploadFileService;

	@PostMapping(value = "uploadFile")
	public String uploadFile(@RequestParam MultipartFile file) {

		UpdateFileDTO updateFileDTO = new UpdateFileDTO();
		updateFileDTO.setFileDatas(file);
		updateFileDTO.setDescription("Upload");

		return uploadFileService.uploadFile(updateFileDTO);
	}
	
	@PostMapping(value = "updateFile")
	public String updateFile(@RequestParam MultipartFile file, @RequestParam Long id) {

		UpdateFileDTO updateFileDTO = new UpdateFileDTO();
		updateFileDTO.setFileDatas(file);
		updateFileDTO.setDescription("Update");
		updateFileDTO.setId(id);

		return uploadFileService.updateFile(updateFileDTO);
	}
	
	
	@DeleteMapping(value = "deleteFile")
	public String deleteFile(@RequestParam String name,@RequestParam Long id) {
		return uploadFileService.deleteFile(name, id);
	}
}
