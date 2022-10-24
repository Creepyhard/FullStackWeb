package br.com.back.end.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.back.end.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
