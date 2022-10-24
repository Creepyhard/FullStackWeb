package br.com.back.end.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.back.end.model.Product;
import br.com.back.end.service.ProductService;

@CrossOrigin(origins="http://localhost:4200")  
@RestController
@RequestMapping(value = "/product")
public class ProductController {
		
	@Autowired
	private ProductService ProductService;
	
	@GetMapping
	public List<Product> getPhoto() {
		return ProductService.getAllProduct();
	}
	
	@GetMapping(path = {"/{id}"})
	public ResponseEntity<Product> getPhotoId(@PathVariable long id) {
		return ProductService.findIdService(id);
	}
	
	@PostMapping
	public Product addPhoto(@RequestBody Product product) {
		return ProductService.addProductService(product);
	}
	
	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<Object> deleteProduct(@PathVariable long id) {
		return ProductService.deleteProductService(id);
	}
	
}
