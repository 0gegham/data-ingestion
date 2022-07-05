package com.data.ingestion.app.controller;

import com.data.ingestion.app.mapper.PersonMapper;
import com.data.ingestion.app.model.dto.PersonDto;
import com.data.ingestion.app.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final PersonService service;
    private final PersonMapper mapper;

    @GetMapping
    public List<PersonDto> getAll(@RequestParam(name = "page", defaultValue = "0") int page,
                                  @RequestParam(name = "size", defaultValue = "10") int size,
                                  @RequestParam(name = "sortBy", defaultValue = "id") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return service.getAll(pageable).stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PersonDto getById(@PathVariable("id") Long id) {
        return mapper.entityToDto(service.getById(id));
    }

    @GetMapping("/search/firstname/{firstname}")
    public List<PersonDto> searchByFirstname(@RequestParam(name = "page", defaultValue = "0") int page,
                                             @RequestParam(name = "size", defaultValue = "10") int size,
                                             @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                                             @PathVariable(name = "firstname") String firstname) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return service.searchByFirstname(pageable, firstname).stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

    @GetMapping("/search/lastname/{lastname}")
    public List<PersonDto> searchByLastname(@RequestParam(name = "page", defaultValue = "0") int page,
                                            @RequestParam(name = "size", defaultValue = "10") int size,
                                            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                                            @PathVariable(name = "lastname") String lastname) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return service.searchByLastname(pageable, lastname).stream().map(mapper::entityToDto).collect(Collectors.toList());
    }

}
