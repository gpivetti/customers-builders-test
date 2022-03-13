package br.com.builders.customer.infra.mongo.repositories.customer.mappers;

import br.com.builders.customer.domain.customer.dto.FiltersCustomerDto;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;

import java.util.HashMap;
import java.util.Map;

public class CustomerFilterEntityMapper {
    public static <T> Map<String, String> mapFieldNames(Class<T> clazz) throws InvalidConstraintException {
        try {
            if (Class.forName(clazz.getName()) == FiltersCustomerDto.class) {
                return mappingCustomerEntity();
            } else {
                throw new InvalidConstraintException("Invalid Filter Class");
            }
        } catch (InvalidConstraintException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidConstraintException("Invalid Filter Class [" + ex.getMessage() + "]");
        }
    }

    private static Map<String, String> mappingCustomerEntity() {
        return new HashMap<>(){{
            put("name", "name");
            put("document", "document");
            put("birthdate", "birthdate");
        }};
    }
}
