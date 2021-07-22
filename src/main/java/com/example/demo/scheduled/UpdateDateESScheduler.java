package com.example.demo.scheduled;

import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entity.FileEntity;
import com.example.demo.entity.FileEntityES;
import com.example.demo.exception.FileStorageException;
import com.example.demo.repository.FileESRepository;
import com.example.demo.repository.FileRepository;

@Component
public class UpdateDateESScheduler {

	@Autowired
	FileRepository fileRepository;

	@Autowired
	FileESRepository fileESRepository;
	
	ResourceBundle message = ResourceBundle.getBundle("message");


//	@Scheduled(fixedRate = 5000) //Update data form database to ElasticSearch  every after 5s
	
	@Scheduled(cron = "0 0/30 * * * ?") //	Update data form database to ElasticSearch every after 30 minutes
	public void updateDataES() {
		List<FileEntity> fileEntity = fileRepository.findAll();
		
		if (fileEntity.size() > 0) {
//			Clear data on ElasticSearch
			fileESRepository.deleteAll();
			
			fileEntity.forEach( item -> { 
//				Set data ElasticSearch
				FileEntityES entityES = new FileEntityES();
				entityES.setId(item.getId());
				entityES.setFloder(item.getFloder());
				entityES.setName(item.getName());
				entityES.setDescription(item.getDescription());
				
//				Save ElasticSearch
				fileESRepository.save(entityES);
			});
		} else {
			throw new FileStorageException(message.getString("NOT_FOUND"));
		}
	}
}
