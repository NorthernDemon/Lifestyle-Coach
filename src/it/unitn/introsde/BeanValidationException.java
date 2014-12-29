package it.unitn.introsde;

import com.google.common.base.Joiner;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Violations of JSR-303 Bean Validation
 */
public class BeanValidationException extends RuntimeException {

    public BeanValidationException(List<ObjectError> allErrors) {
        super(Joiner.on(",").join(allErrors));
    }
}
