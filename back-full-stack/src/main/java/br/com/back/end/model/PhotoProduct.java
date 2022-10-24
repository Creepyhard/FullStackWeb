package br.com.back.end.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
public class PhotoProduct{

	@Id
	private Long id;
	@Column(nullable = false)
	private String nameFile;
	@Column(nullable = false)
	private String description;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false, length = 64)
    private String photos;
}
