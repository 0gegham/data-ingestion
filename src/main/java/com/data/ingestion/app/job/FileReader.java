package com.data.ingestion.app.job;

import com.data.ingestion.app.model.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileReader {

    @Value("${app.file_name}")
    private String dataFile;

    @PostConstruct
    private void init() {
        try(ZipFile zipFile = new ZipFile(ClassLoader.getSystemResource(dataFile).getFile())) {
            zipFile.extractAll(ClassLoader.getSystemResource("").getPath());
        } catch (IOException e) {
            log.error("something went wrong: " + e.getMessage());
        }
    }

    @Bean
    public MultiResourceItemReader<PersonDto> multiResourceItemReader(FlatFileItemReader<PersonDto> reader) {
        MultiResourceItemReader<PersonDto> resourceItemReader = new MultiResourceItemReader<>();
        try {
            Resource[] resolver = new PathMatchingResourcePatternResolver().getResources("*.csv");
            resourceItemReader.setResources(resolver);
            resourceItemReader.setDelegate(reader);
        } catch (IOException e) {
            log.error("something went wrong: " + e.getMessage());
        }

        return resourceItemReader;
    }

    @Bean
    public FlatFileItemReader<PersonDto> reader() {
        return new FlatFileItemReaderBuilder<PersonDto>()
                .name("reader")
                .linesToSkip(1)
                .lineMapper(lineMapper())
                .build();
    }

    private LineMapper<PersonDto> lineMapper() {
        DefaultLineMapper<PersonDto> mapper = new DefaultLineMapper<>();
        mapper.setFieldSetMapper(fieldSetMapper());
        mapper.setLineTokenizer(tokenizer());
        return mapper;
    }

    private LineTokenizer tokenizer() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("firstName", "lastName", "data");
        return tokenizer;
    }

    private FieldSetMapper<PersonDto> fieldSetMapper() {
        BeanWrapperFieldSetMapper<PersonDto> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(PersonDto.class);
        return mapper;
    }

}
