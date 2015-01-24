package it.unitn.introsde.process;

import it.unitn.introsde.ServiceConfiguration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * Convenient class to abstract message passing functionality
 */
public abstract class AbstractProcess {

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * Invoke RESTful service via Spring Rest Template
     *
     * @param restPath   - relative path to service
     * @param httpMethod - http method to invoke
     * @param clazz      - type of expected result
     * @param mediaType  - request type, such as XML or JSON
     * @param <T>        - type of expected object
     * @return response from RESTful service of given class and type
     */
    protected <T> ResponseEntity<?> request(String restPath, HttpMethod httpMethod, Class<T> clazz, String mediaType) {
        return request(restPath, httpMethod, clazz, null, mediaType);
    }

    /**
     * Invoke RESTful service via Spring Rest Template
     *
     * @param restPath   - relative path to service
     * @param httpMethod - http method to invoke
     * @param clazz      - type of expected result
     * @param object     - object to send
     * @param mediaType  - request type, such as XML or JSON
     * @param <T>        - type of expected object
     * @return response from RESTful service of given class and type
     */
    protected <T> ResponseEntity<?> request(String restPath, HttpMethod httpMethod, Class<T> clazz, T object, String mediaType) {
        return restTemplate.exchange(ServiceConfiguration.getUrl() + "/" + restPath, httpMethod, createHeader(mediaType, object), clazz);
    }

    /**
     * Create header for with preset body payload and it's content type
     *
     * @param accept media type and content type
     * @return HttpEntity consumed by rest template
     */
    protected static HttpEntity<Object> createHeader(String accept) {
        return createHeader(accept, null);
    }

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
