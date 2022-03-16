package br.com.builders.customer.controllers.customer.mapper;

import br.com.builders.customer.controllers.customer.dto.InsertCustomerDto;
import br.com.builders.customer.controllers.customer.dto.UpdateCustomerDto;
import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;

public class CustomerMapper {
    public static SaveCustomerDto toSaveCustomerDto(InsertCustomerDto customer) {
        return SaveCustomerDto.builder()
                .name(customer.getName())
                .document(customer.getDocument())
                .birthdate(customer.getBirthdate())
                .build();
    }

    public static SaveCustomerDto toSaveCustomerDto(UpdateCustomerDto customer) {
        return SaveCustomerDto.builder()
                .name(customer.getName())
                .document(customer.getDocument())
                .birthdate(customer.getBirthdate())
                .build();
    }
}
