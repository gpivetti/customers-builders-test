package br.com.builders.customer.domain.customer.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class SaveCustomerDto {
    @NotBlank(message = "name not be empty")
    private String name;
    @NotBlank(message = "document not be empty")
    private String document;
    @NotNull(message = "birthdate not be empty")
    private LocalDate birthdate;
}
