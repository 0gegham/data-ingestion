package com.data.ingestion.app.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException() {
        super();
    }

    public PersonNotFoundException(String msg) {
        super(msg);
        log.error(msg);
    }

    public PersonNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
        log.error(msg + ". Cause: " + cause);
    }

}
