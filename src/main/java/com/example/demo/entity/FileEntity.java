package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "file")
public class FileEntity {
	@Id //(định nghia khoa chinh va not null)
	@GeneratedValue(strategy = GenerationType.IDENTITY) //id tự động tăng
	private Long id;  
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "floder")
	private String floder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFloder() {
		return floder;
	}

	public void setFloder(String floder) {
		this.floder = floder;
	}
}
