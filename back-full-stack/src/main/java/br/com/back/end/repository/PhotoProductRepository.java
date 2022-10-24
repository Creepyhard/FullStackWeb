package br.com.back.end.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.back.end.model.PhotoProduct;

public interface PhotoProductRepository extends JpaRepository<PhotoProduct, Long>{

}
