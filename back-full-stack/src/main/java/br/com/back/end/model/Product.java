package br.com.back.end.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false, unique = true)
	private String description;
	@Column(nullable = false)
	private double price;
	@Column(nullable = false)
	private boolean purchasable;
} 
