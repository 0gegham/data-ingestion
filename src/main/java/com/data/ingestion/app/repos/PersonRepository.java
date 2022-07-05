package com.data.ingestion.app.repos;

import com.data.ingestion.app.model.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Page<Person> findByFirstnameContainingIgnoreCase(Pageable pageable, String firstname);
    Page<Person> findByLastnameContainingIgnoreCase(Pageable pageable, String lastname);
}
