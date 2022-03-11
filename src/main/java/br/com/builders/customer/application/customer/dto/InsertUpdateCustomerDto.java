package br.com.builders.customer.application.customer.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
public class InsertUpdateCustomerDto {
    @NotBlank(message = "name not be empty")
    private String name;
    @NotBlank(message = "document not be empty")
    private String document;
    @NotNull(message = "birthdate not be empty")
    private Date birthdate;
}
