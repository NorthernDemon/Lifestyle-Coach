package it.unitn.introsde.persistence.process;

import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = ServiceConfiguration.NAME)
public class UserProcess extends AbstractProcess {

    private RestTemplate restTemplate;

    @RequestMapping(value = "/profile", method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Person> createProfile(
            @RequestHeader(value = "Accept") String accept,
            @RequestBody Person person) {
        System.out.println("Incoming UserProcess with accept=" + accept + ", person=" + person);
        String url = ServiceConfiguration.getUrl() + "/person";
        ResponseEntity<?> exchange = restTemplate.exchange(url, HttpMethod.POST, createHeader(accept, person), Person.class);
        Person createdPerson = (Person) exchange.getBody();
        System.out.println("Outgoing UserProcess with accept=" + accept + ", person=" + createdPerson);
        return new ResponseEntity<>(createdPerson, HttpStatus.OK);
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
