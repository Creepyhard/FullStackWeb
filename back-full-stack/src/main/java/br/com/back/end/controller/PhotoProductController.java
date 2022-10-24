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

import br.com.back.end.model.PhotoProduct;
import br.com.back.end.service.PhotoProductService;

@CrossOrigin(origins="http://localhost:4200")  
@RestController
@RequestMapping(value = "/photos")
public class PhotoProductController {
		
	@Autowired
	private PhotoProductService photoProductService;
	
	@GetMapping
	public List<PhotoProduct> getPhoto() {
		return photoProductService.getAllPhoto();
	}
	
	@GetMapping(path = {"/{id}"})
	public ResponseEntity<PhotoProduct> getPhotoId(@PathVariable long id) {
		return photoProductService.findIdService(id);
	}
	
	@PostMapping
	public PhotoProduct addPhoto(@RequestBody PhotoProduct photo) {
		return photoProductService.addPhotoService(photo);
	}
	
	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<Object> deletePhoto(@PathVariable long id) {
		return photoProductService.deletePhotoService(id);
	}
	
}
