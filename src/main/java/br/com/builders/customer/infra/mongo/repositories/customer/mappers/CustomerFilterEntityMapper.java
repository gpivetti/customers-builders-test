package br.com.builders.customer.infra.mongo.repositories.customer.mappers;

import br.com.builders.customer.domain.customer.dto.FiltersCustomerDto;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;
import br.com.builders.customer.main.exceptions.InvalidParameterException;

import java.util.HashMap;
import java.util.Map;

public class CustomerFilterEntityMapper {
    public static <T> Map<String, String> mapFieldNames(Class<T> clazz) throws InvalidParameterException {
        try {
            if (Class.forName(clazz.getName()) == FiltersCustomerDto.class) {
                return mappingFiltersCustomerToEntityFields();
            }
            throw new InvalidParameterException("Invalid Filter Class");
        } catch (InvalidConstraintException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidParameterException("Invalid Filter Class [" + ex.getMessage() + "]");
        }
    }

    private static Map<String, String> mappingFiltersCustomerToEntityFields() throws NoSuchFieldException {
        return new HashMap<>(){{
            put(FiltersCustomerDto.class.getDeclaredField("name").getName(),
                    CustomerEntity.class.getDeclaredField("name").getName());
            put(FiltersCustomerDto.class.getDeclaredField("document").getName(),
                    CustomerEntity.class.getDeclaredField("document").getName());
            put(FiltersCustomerDto.class.getDeclaredField("birthdate").getName(),
                    CustomerEntity.class.getDeclaredField("birthdate").getName());
        }};
    }
}
