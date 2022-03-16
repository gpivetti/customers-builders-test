package br.com.builders.customer.domain.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltersCustomerDto {
    private String name;
    private String document;
    private LocalDate birthdate;
}
