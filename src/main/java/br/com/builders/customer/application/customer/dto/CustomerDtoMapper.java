package br.com.builders.customer.application.customer.dto;

import br.com.builders.customer.domain.customer.Customer;

public class CustomerDtoMapper {
    public static CustomerDto toCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .document(customer.getDocument())
                .birthdate(customer.getBirthdate())
                .age(customer.getAge())
                .build();
    }
}
