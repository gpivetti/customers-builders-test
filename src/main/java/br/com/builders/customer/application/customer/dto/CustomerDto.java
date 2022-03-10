package br.com.builders.customer.application.customer.dto;

import br.com.builders.customer.domain.customer.Customer;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CustomerDto {
    private String id;
    private String name;
    private String document;
    private Date birthdate;
    private int age;
}
