package com.example.demo.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.dto.UpdateFileDTO;
import com.example.demo.entity.FileEntity;
import com.example.demo.entity.FileEntityES;
import com.example.demo.exception.FileStorageException;
import com.example.demo.repository.FileESRepository;
import com.example.demo.repository.FileRepository;
import com.example.demo.service.IUploadFileService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadFileService implements IUploadFileService {

	ResourceBundle resourceBundle = ResourceBundle.getBundle("folder");
	ResourceBundle message = ResourceBundle.getBundle("message");

	@Autowired
	FileRepository fileRepository;

	@Autowired
	FileESRepository fileESRepository;
	

	@Override
	public String uploadFile(UpdateFileDTO updateFileDTO) {
		
		String description = updateFileDTO.getDescription();
		System.out.println("Description: " + description);

		// Root folder upload file.
		String uploadRootPath = resourceBundle.getString("FOLDER");

		// Original file name at Client.
		String name = StringUtils.cleanPath(updateFileDTO.getFileDatas().getOriginalFilename());
		uploadAndUpdate(updateFileDTO, name, uploadRootPath);

//		Save name and Path folder file on database
		FileEntity entity = new FileEntity();
		entity.setFloder(uploadRootPath);
		entity.setName(name);
		entity.setDescription(description);
		entity = fileRepository.save(entity);
		
//		Set data ElasticSearch
		FileEntityES entityES = new FileEntityES();
		entityES.setId(entity.getId());
		entityES.setFloder(entity.getFloder());
		entityES.setName(entity.getName());
		entityES.setDescription(entity.getDescription());
		
//		Save ElasticSearch
		fileESRepository.save(entityES);
		
		return "Upload successfuly id: " + entity.getId();
	}

	@Override
	public String deleteFile(String nameFile, Long id) {
		File file = new File(resourceBundle.getString("FOLDER") + nameFile);
		if (file.delete()) {
//			Delete on database
			fileRepository.deleteById(id);

//			Delete on ElaticSearch			
			fileESRepository.deleteById(id);
			
			return message.getString("DELETE_SUCCESS");
		} else {
			throw new FileStorageException(message.getString("ERROR_DELETE"));
		}
	}

	@Override
	public String updateFile(UpdateFileDTO updateFileDTO) {
		// Root folder upload file.
		String uploadRootPath = resourceBundle.getString("FOLDER");

		// Original file name at Client.
		String name = StringUtils.cleanPath(updateFileDTO.getFileDatas().getOriginalFilename());

		Optional<FileEntity> entity = fileRepository.findById(updateFileDTO.getId());

		// Check file oldName and newName
		if (!name.equals(entity.get().getName())) {

//			Do rename file
			updateNameFile(entity.get().getName(), name, uploadRootPath);
//			Update nameFile on Database
			FileEntity entityNew = new FileEntity();

			entityNew.setFloder(uploadRootPath);
			entityNew.setId(updateFileDTO.getId());
			entityNew.setName(name);
			entityNew = fileRepository.save(entityNew);
			
//			Set data ElasticSearch
			FileEntityES entityES = new FileEntityES();
			entityES.setId(entityNew.getId());
			entityES.setFloder(entityNew.getFloder());
			entityES.setName(entityNew.getName());
			entityES.setDescription(entityNew.getDescription());
			
//			update on ElasticSearch
			fileESRepository.save(entityES);

			return message.getString("RENAME_SUCCESS");
		}

		File checkFile = new File(uploadRootPath + name);

		// Check file exists
		if (!checkFile.isFile()) {
			throw new FileStorageException(message.getString("CHECK_FILE_EXIST"));
		} else {
//			Update file
			uploadAndUpdate(updateFileDTO, name, uploadRootPath);
			return message.getString("UPDATE_SUCCESS");
		}
	}

	private Boolean updateNameFile(String oldName, String newName, String uploadRootPath) {
		File oldfile = new File(uploadRootPath + oldName);
		File newfile = new File(uploadRootPath + newName);

		return oldfile.renameTo(newfile);

	}

	private void uploadAndUpdate(UpdateFileDTO updateFileDTO, String name, String uploadRootPath) {
		File uploadRootDir = new File(uploadRootPath);
		// Create folder root upload if not exists.
		if (!uploadRootDir.exists()) {
			uploadRootDir.mkdirs();
		}
		// Check if the file's name contains invalid characters
		if (name.endsWith(".docx") || name.endsWith(".xlsx")) {
			if (name != null && name.length() > 0) {
				try {
					// Create file at Server.
					File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
					stream.write(updateFileDTO.getFileDatas().getBytes());
					stream.close();
				} catch (Exception e) {
					throw new FileStorageException(message.getString("COULD_NOT"));
				}
			} else {
				throw new FileStorageException(message.getString("COULD_NOT"));
			}
		} else {
			throw new FileStorageException(message.getString("WRONG_FORMAT"));
		}
	}

	@Override
	public List<FileEntityES> searchFileByDescriptionES(String desc) {
		List<FileEntityES> fileEntityESs = fileESRepository.findByDescription(desc);
		if (fileEntityESs.size() > 0) {
			return fileEntityESs;
		} else {
			throw new FileStorageException(message.getString("NOT_FOUND"));
		}
	}

	@Override
	public List<FileEntity> searchFileByDescription(String desc) {
		List<FileEntity> fileEntity = fileRepository.findByDescription(desc);
		if (fileEntity.size() > 0) {
			return fileEntity;
		} else {
			throw new FileStorageException(message.getString("NOT_FOUND"));
		}
	}
	
	@Override
	public String compareSearch(String desc) {
		
		String result = "";
		
//		Time search ElasticSearch
		Long startES = System.currentTimeMillis();
		List<FileEntityES> fileEntityESs = fileESRepository.findByDescription(desc);
		Long endES = System.currentTimeMillis() - startES;
		
		result += "Search ElaticSearch: " + endES + " ms -";
		
//		Time search Database
		Long start = System.currentTimeMillis();
		List<FileEntity> fileEntity = fileRepository.findByDescription(desc);
		Long end = System.currentTimeMillis() - start;
		
		result += "Search Database: " + end + " ms ";
		
		return result;
		
	}
	
	

}
