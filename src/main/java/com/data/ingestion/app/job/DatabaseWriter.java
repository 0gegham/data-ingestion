package com.data.ingestion.app.job;

import com.data.ingestion.app.mapper.PersonMapper;
import com.data.ingestion.app.model.dto.PersonDto;
import com.data.ingestion.app.model.entity.Person;
import com.data.ingestion.app.repos.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseWriter implements ItemWriter<PersonDto> {

    private final PersonMapper personMapper;
    private final PersonRepository personRepository;

    @Override
    public void write(List<? extends PersonDto> list) {
        try {
            RepositoryItemWriter<Person> writer = new RepositoryItemWriter<>();
            writer.setRepository(personRepository);
            writer.setMethodName("save");
            List<Person> collect = list.stream().map(personMapper::dtoToEntity).collect(Collectors.toList());
            writer.write(collect);
        } catch (Exception e) {
            log.error("something went wrong: " + e.getMessage());
        }
    }

    @AfterStep
    private void clean() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources("*.csv");
            for (Resource resource : resources) {
                resource.getFile().delete();
            }
        } catch (IOException e) {
            log.error("Something went wrong: " + e.getMessage());
        }
    }
}
