package br.com.back.end.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.back.end.model.Office;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {

}
