package it.unitn.introsde.persistence.process;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * Convenient class to abstract message passing functionality
 */
public abstract class AbstractProcess {

    protected static HttpEntity<Object> createHeader(String accept, Object body) {
        MediaType mediaType = MediaType.parseMediaType(accept);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(mediaType));
        httpHeaders.setContentType(mediaType);
        return new HttpEntity<>(body, httpHeaders);
    }
}
