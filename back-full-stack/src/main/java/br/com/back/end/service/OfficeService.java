package br.com.back.end.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.back.end.exception.ResourceNotFoundException;
import br.com.back.end.model.Office;
import br.com.back.end.repository.OfficeRepository;

@Service
public class OfficeService {

	@Autowired
	private OfficeRepository officeRepository;

	public List<Office> getAllOffice() {
		return officeRepository.findAll();
	}

	public Office addOfficeService(@RequestBody Office office) {
		return officeRepository.save(office);
	}

	public ResponseEntity<Office> findIdService(@PathVariable long id) {
		return officeRepository.findById(id).map(record -> ResponseEntity.ok().body(record))
				.orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}

	public ResponseEntity<Object> deleteOfficeService(@PathVariable long id) {
		return officeRepository.findById(id).map(record -> {
			officeRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("This id does not exist :" + id));
	}

	public ResponseEntity<Office> attOfficeService(Long id, Office officeDetails) {
		Office office = officeRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("This id does not exist :" + id + " Confirm the entered data!!"));

		office.setNameOffice(officeDetails.getNameOffice());

		Office attOffice = officeRepository.save(office);
		return ResponseEntity.ok(attOffice);
	}
}
