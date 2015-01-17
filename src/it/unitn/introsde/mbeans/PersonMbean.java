package it.unitn.introsde.mbeans;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import it.unitn.introsde.ServiceConfiguration;
import it.unitn.introsde.persistence.entity.Person;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by davie on 1/15/2015.
 */
@ManagedBean(name = "personMBean", eager = true)
@SessionScoped
public class PersonMbean implements Serializable {

}
