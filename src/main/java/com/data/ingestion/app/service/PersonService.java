package com.data.ingestion.app.service;

import com.data.ingestion.app.model.entity.Person;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PersonService {
    List<Person> getAll(Pageable pageable);
    Person getById(Long id);
    List<Person> searchByFirstname(Pageable pageable, String firstname);
    List<Person> searchByLastname(Pageable pageable, String lastname);
}
