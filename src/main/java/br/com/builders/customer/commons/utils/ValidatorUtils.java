package br.com.builders.customer.commons.utils;

import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ObjectValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidatorUtils  {
    public static <T> void validate(T object) throws AppErrorException, ObjectValidationException {
        validate(null, object);
    }

    public static <T> void validate(String entity, T object) throws AppErrorException, ObjectValidationException {
        if (object == null) {
            throw new AppErrorException("ValidationError: Null Object Data");
        }
        try {
            Set<ConstraintViolation<T>> violations = buildValidator().validate(object);
            Map<String, String> mapViolations = getMapViolations(violations);
            if (!mapViolations.isEmpty()) {
                if (entity != null && !entity.trim().equals(""))
                    throw new ObjectValidationException(entity, mapViolations);
                else
                    throw new ObjectValidationException(mapViolations);
            }
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    private static Validator buildValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        return factory.getValidator();
    }

    private static <T> Map<String, String> getMapViolations(Set<ConstraintViolation<T>> violations) {
        Map<String, String> validatorsMap = new HashMap<>();
        if (checkViolations(violations)) {
            for (ConstraintViolation<T> violation : violations) {
                validatorsMap.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
        }
        return validatorsMap;
    }

    private static <T> boolean checkViolations(Set<ConstraintViolation<T>> violations) {
        return violations != null && !violations.isEmpty();
    }
}
