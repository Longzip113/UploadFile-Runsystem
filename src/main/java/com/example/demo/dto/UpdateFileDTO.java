package com.example.demo.dto;

import org.springframework.web.multipart.MultipartFile;

public class UpdateFileDTO {
	
	private String description;
	 
    // Upload files.
    private MultipartFile fileDatas;
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
 
    public MultipartFile getFileDatas() {
        return fileDatas;
    }
 
    public void setFileDatas(MultipartFile fileDatas) {
        this.fileDatas = fileDatas;
    }

}
