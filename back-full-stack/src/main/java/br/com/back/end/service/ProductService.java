package br.com.back.end.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.back.end.exception.ResourceNotFoundException;
import br.com.back.end.model.Product;
import br.com.back.end.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public List<Product> getAllProduct() {
		return productRepository.findAll();
	}

	public Product addProductService(@RequestBody Product produto) {
		return productRepository.save(produto);
	}

	public ResponseEntity<Product> findIdService(@PathVariable long id) {
		return productRepository.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}

	public ResponseEntity<Object> deleteProductService(@PathVariable long id) {
		return productRepository.findById(id).map(record -> {
			productRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}

}
