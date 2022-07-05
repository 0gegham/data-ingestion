package com.data.ingestion.app.controller;

import com.data.ingestion.app.model.dto.PersonDto;
import com.data.ingestion.app.model.entity.Person;
import com.data.ingestion.app.repos.PersonRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private Person firstPerson;

    private String url;

    @BeforeEach
    void setUp() {
        clean();

        firstPerson = new Person();
        firstPerson.setFirstname("firstname1");
        firstPerson.setLastname("lastname1");
        firstPerson.setDate(LocalDate.now());
        personRepository.save(firstPerson);

        IntStream.range(2, 11).forEach(id -> {
            Person person = new Person();
            person.setFirstname("firstname" + id);
            person.setLastname("lastname" + id);
            person.setDate(LocalDate.now());

            personRepository.save(person);
        });

        url = restTemplate.getRootUri();
    }

    @Test
    void shouldGetAllUsers() {
        // given, when
        ResponseEntity<PersonDto[]> responseEntity = restTemplate.getForEntity(url, PersonDto[].class);
        PersonDto[] people = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(people).hasSize(10);
    }

    @Test
    void shouldGetPersonById() {
        // given, when
        ResponseEntity<PersonDto> responseEntity = restTemplate.getForEntity(url + "/" + firstPerson.getId(), PersonDto.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getFirstName()).isEqualTo("firstname1");
    }

    @Test
    void shouldFindByFirstname() {
        // given, when
        ResponseEntity<PersonDto[]> responseEntity = restTemplate.getForEntity(url + "/search/firstname/ame1?sortBy=id", PersonDto[].class);
        PersonDto[] people = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(people).hasSize(2);
        assertThat(people[0].getFirstName()).isEqualTo("firstname1");
        assertThat(people[1].getFirstName()).isEqualTo("firstname10");
    }

    @Test
    void shouldFindByLastname() {
        // given, when
        ResponseEntity<PersonDto[]> responseEntity = restTemplate.getForEntity(url + "/search/lastname/ame1?sortBy=id", PersonDto[].class);
        PersonDto[] people = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(people).hasSize(2);
        assertThat(people[0].getLastName()).isEqualTo("lastname1");
        assertThat(people[1].getLastName()).isEqualTo("lastname10");
    }

    @Test
    void theStatusCodeIs404CauseOfWrongId() {
        // given, when
        ResponseEntity<PersonDto> responseEntity = restTemplate.getForEntity(url + "/200", PersonDto.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @AfterEach
    void clean() {
        personRepository.deleteAll();
    }
}