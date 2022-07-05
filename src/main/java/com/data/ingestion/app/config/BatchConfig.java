package com.data.ingestion.app.config;

import com.data.ingestion.app.model.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ItemWriter<PersonDto> databaseWriter;
    private final MultiResourceItemReader<PersonDto> multiResourceItemReader;

    @Bean
    public Job job() {
        return jobBuilderFactory
                .get("job")
                .start(mainStep())
                .build();
    }

    @Bean
    public Step mainStep() {
        return stepBuilderFactory.get("main step")
                .<PersonDto, PersonDto>chunk(1)
                .reader(multiResourceItemReader)
                .writer(databaseWriter)
                .build();
    }

}
