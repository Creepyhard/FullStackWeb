package br.com.back.end.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

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
