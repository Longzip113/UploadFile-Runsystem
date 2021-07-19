package com.example.demo.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.dto.UpdateFileDTO;
import com.example.demo.exception.FileStorageException;
import com.example.demo.service.IUploadFileService;

@Service
public class UploadFileService implements IUploadFileService {

	@Override
	public String uploadFile(UpdateFileDTO updateFileDTO) {

		String description = updateFileDTO.getDescription();
		System.out.println("Description: " + description);

		// Root folder upload file.
		String uploadRootPath = "/Users/ftwbeatn.t.long/Desktop/GitBooking";

		// Original file name at Client.
		String name = StringUtils.cleanPath(updateFileDTO.getFileDatas().getOriginalFilename());

		return uploadAndUpdate(updateFileDTO, name, uploadRootPath);

	}

	@Override
	public String deleteFile(String nameFile) {
		File file = new File("/Users/ftwbeatn.t.long/Desktop/GitBooking/" + nameFile);
		if (file.delete()) {
			return "File is deleted!";
		} else {
			throw new FileStorageException("Sorry, unable to delete the file." + nameFile);
		}
	}

	@Override
	public String updateFile(UpdateFileDTO updateFileDTO) {
		// Root folder upload file.
		String uploadRootPath = "/Users/ftwbeatn.t.long/Desktop/GitBooking/";

		// Original file name at Client.
		String name = StringUtils.cleanPath(updateFileDTO.getFileDatas().getOriginalFilename());

		File checkFile = new File(uploadRootPath + name);

		// Check file exists 
		if (!checkFile.isFile()) {
			throw new FileStorageException("File [" + name + "] does not exis. Please try again!");
		} else {
			return uploadAndUpdate(updateFileDTO, name, uploadRootPath);
		}
	}

	private String uploadAndUpdate(UpdateFileDTO updateFileDTO, String name, String uploadRootPath) {
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
					return "Save file successfully: " + serverFile + " tyle file: "
							+ updateFileDTO.getFileDatas().getContentType();
				} catch (Exception e) {
					throw new FileStorageException("Could not store file " + name + ". Please try again!");
				}
			} else {
				throw new FileStorageException("Could not store file " + name + ". Please try again!");
			}
		} else {
			throw new FileStorageException("Sorry! Filename contains invalid path sequence " + name);
		}
	}

}
