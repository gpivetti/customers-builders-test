package br.com.builders.customer.application.customer.dto;

import br.com.builders.customer.domain.customer.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDto {
    private String id;
    private String name;
    private String document;
    private Date birthdate;
    private int age;

    public static CustomerDto fromCustomer(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .document(customer.getDocument())
                .birthdate(customer.getBirthdate())
                .age(customer.getAge())
                .build();
    }
}
