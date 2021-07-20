package com.example.demo.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.demo.dto.UpdateFileDTO;
import com.example.demo.entity.FileEntity;
import com.example.demo.exception.FileStorageException;
import com.example.demo.repository.FileRepository;
import com.example.demo.service.IUploadFileService;

@Service
public class UploadFileService implements IUploadFileService {

	ResourceBundle resourceBundle = ResourceBundle.getBundle("folder");
	ResourceBundle message = ResourceBundle.getBundle("message");

	@Autowired
	FileRepository fileRepository;

	@Override
	@Transactional
	public String uploadFile(UpdateFileDTO updateFileDTO) {

		String description = updateFileDTO.getDescription();
		System.out.println("Description: " + description);

		// Root folder upload file.
		String uploadRootPath = resourceBundle.getString("FOLDER");

		// Original file name at Client.
		String name = StringUtils.cleanPath(updateFileDTO.getFileDatas().getOriginalFilename());

		if (uploadAndUpdate(updateFileDTO, name, uploadRootPath)) {

			FileEntity entity = new FileEntity();

			entity.setFloder(uploadRootPath);
			entity.setName(name);
			entity = fileRepository.save(entity);

			return "Upload successfuly" + entity.getId();
		} else {
			throw new FileStorageException(message.getString("CHECK_FILE_EXIST"));
		}
	}

	@Override
	@Transactional
	public String deleteFile(String nameFile) {
		File file = new File(resourceBundle.getString("FOLDER") + nameFile);
		if (file.delete()) {
			return message.getString("DELETE_SUCCESS");
		} else {
			throw new FileStorageException(message.getString("ERROR_DELETE"));
		}
	}

	@Override
	@Transactional
	public String updateFile(UpdateFileDTO updateFileDTO) {
		// Root folder upload file.
		String uploadRootPath = resourceBundle.getString("FOLDER");

		// Original file name at Client.
		String name = StringUtils.cleanPath(updateFileDTO.getFileDatas().getOriginalFilename());

		Optional<FileEntity> entity = fileRepository.findById(updateFileDTO.getId());

		// Check file oldName and newName
		if (!name.equals(entity.get().getName())) {

			// Do rename file
			if (updateNameFile(entity.get().getName(), name, uploadRootPath)) {
//				Update nameFile Database
				FileEntity entityNew = new FileEntity();
				
				entityNew.setFloder(uploadRootPath);
				entityNew.setId(updateFileDTO.getId());
				entityNew.setName(name);
				entityNew = fileRepository.save(entityNew);
				
				//return message.getString("RENAME_SUCCESS");
			} else {
				throw new FileStorageException(message.getString("RENAME_FAILURE"));
			}
		}

		File checkFile = new File(uploadRootPath + name);

		// Check file exists
		if (!checkFile.isFile()) {
			throw new FileStorageException(message.getString("CHECK_FILE_EXIST"));
		} else {
//			Update file
			if (uploadAndUpdate(updateFileDTO, name, uploadRootPath)) {
				return message.getString("UPDATE_SUCCESS");
			} else {
				throw new FileStorageException(message.getString("UPDATE_FAILURE"));
			}
		}
	}

	private Boolean updateNameFile(String oldName, String newName, String uploadRootPath) {
		File oldfile = new File(uploadRootPath + oldName);
		File newfile = new File(uploadRootPath + newName);

		return oldfile.renameTo(newfile);

	}

	private Boolean uploadAndUpdate(UpdateFileDTO updateFileDTO, String name, String uploadRootPath) {
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
					return true;
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

}
