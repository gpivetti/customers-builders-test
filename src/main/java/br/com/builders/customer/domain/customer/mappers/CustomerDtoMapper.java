package br.com.builders.customer.domain.customer.mappers;

import br.com.builders.customer.application.dto.CustomerDto;
import br.com.builders.customer.domain.customer.Customer;

public class CustomerDtoMapper {
    public static CustomerDto toCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .birthdate(customer.getBirthdate())
                .age(customer.getAge())
                .build();
    }
}
