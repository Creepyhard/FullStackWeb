package br.com.back.end.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.back.end.model.Office;
import br.com.back.end.service.OfficeService;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping(value = "/offices")
public class OfficeController {
	
	@Autowired
	private OfficeService officeService;
	
	@GetMapping
	public List<Office> getOffice() {
		return officeService.getAllOffice();
	}
	
	@GetMapping(path = {"/{id}"})
	public ResponseEntity<Office> getOfficeById(@PathVariable long id) {
		return officeService.findIdService(id);
	}
	
	@PostMapping
	public Office addOffice(@RequestBody Office office) {
		return officeService.addOfficeService(office);
	}
	
	@DeleteMapping(path = { "/{id}" })
	public ResponseEntity<Object> deleteOffice(@PathVariable long id) {
		return officeService.deleteOfficeService(id);
	}
	
	@PutMapping({"/{id}"})
	public ResponseEntity<Office> attOffice(@PathVariable Long id, @RequestBody Office officeDetails){
		return officeService.attOfficeService(id, officeDetails);
	}
	
}
