package br.com.back.end.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.back.end.exception.ResourceNotFoundException;
import br.com.back.end.model.PhotoProduct;
import br.com.back.end.repository.PhotoProductRepository;

@Service
public class PhotoProductService {

	@Autowired
	private PhotoProductRepository PhotoProductRepository;

	public List<PhotoProduct> getAllPhoto() {
		return PhotoProductRepository.findAll();
	}

	public PhotoProduct addPhotoService(@RequestBody PhotoProduct photo) {
		return PhotoProductRepository.save(photo);
	}

	public ResponseEntity<PhotoProduct> findIdService(@PathVariable long id) {
		return PhotoProductRepository.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}

	public ResponseEntity<Object> deletePhotoService(@PathVariable long id) {
		return PhotoProductRepository.findById(id).map(record -> {
			PhotoProductRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}

}
