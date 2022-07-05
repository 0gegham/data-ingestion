package com.data.ingestion.app.mapper;

import com.data.ingestion.app.model.dto.PersonDto;
import com.data.ingestion.app.model.entity.Person;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring",
        imports = {LocalDate.class, DateTimeFormatter.class}, unmappedSourcePolicy = ReportingPolicy.IGNORE)
public abstract class PersonMapper {
    @Mappings({
            @Mapping(target = "firstname", source = "firstName"),
            @Mapping(target = "lastname", source = "lastName"),
            @Mapping(target = "date", source = "date", qualifiedByName ="getDate")
    })
    public abstract Person dtoToEntity(PersonDto dto);

    @Mappings({
            @Mapping(target = "firstName", source = "firstname"),
            @Mapping(target = "lastName", source = "lastname"),
            @Mapping(target = "date", source = "date")
    })
    public abstract PersonDto entityToDto(Person entity);

    @Named("getDate")
    public LocalDate getDate(String date) {
        String filteredDate = date.replaceAll("(?<=\\d)(?:st|nd|rd|th)|(,)", "");
        return LocalDate.parse(filteredDate, DateTimeFormatter.ofPattern("[MMMM dd yyyy][MMMM d yyyy][dd/MM/yyyy]"));
    }
}
