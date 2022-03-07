package br.com.builders.customer.application.dto;

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
