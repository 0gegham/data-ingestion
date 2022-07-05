package com.data.ingestion.app.service.impl;

import com.data.ingestion.app.exception.PersonNotFoundException;
import com.data.ingestion.app.model.entity.Person;
import com.data.ingestion.app.repos.PersonRepository;
import com.data.ingestion.app.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;

    @Override
    public List<Person> getAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    @Override
    public Person getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new PersonNotFoundException("Wrong person id: " + id));
    }

    @Override
    public List<Person> searchByFirstname(Pageable pageable, String firstname) {
        return repository.findByFirstnameContainingIgnoreCase(pageable, firstname).getContent();
    }

    @Override
    public List<Person> searchByLastname(Pageable pageable, String lastname) {
        return repository.findByLastnameContainingIgnoreCase(pageable, lastname).getContent();
    }

}
