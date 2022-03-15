package br.com.builders.customer.application.customer.mapper;

import br.com.builders.customer.application.customer.dto.InsertUpdateCustomerDto;
import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;

public class CustomerMapper {
    public static SaveCustomerDto toSaveCustomerDto(InsertUpdateCustomerDto customer) {
        return SaveCustomerDto.builder()
                .name(customer.getName())
                .document(customer.getDocument())
                .birthdate(customer.getBirthdate())
                .build();
    }
}
