package it.unitn.introsde.persistence.process;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * Convenient class to abstract message passing functionality
 */
public abstract class AbstractProcess {

    /**
     * Create header for with preset body payload and it's content type
     *
     * @param accept media type and content type
     * @param body   payload
     * @return HttpEntity consumed by rest template
     */
    protected static HttpEntity<Object> createHeader(String accept, Object body) {
        MediaType mediaType = MediaType.parseMediaType(accept);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(mediaType));
        httpHeaders.setContentType(mediaType);
        return new HttpEntity<>(body, httpHeaders);
    }
}
