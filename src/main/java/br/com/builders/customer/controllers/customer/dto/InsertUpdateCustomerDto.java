package br.com.builders.customer.controllers.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class InsertUpdateCustomerDto {
    @NotBlank(message = "name not be empty")
    @Schema(description = "Customer's name", required = true)
    private String name;

    @NotBlank(message = "document not be empty")
    @Pattern(regexp ="[0-9]+", message = "Only numbers are allowed on document")
    @Schema(description = "Customer's document (only numbers allowed)", required = true)
    private String document;

    @NotNull(message = "birthdate not be empty")
    @Schema(description = "Customer's birthdate (pattern: YYYY-MM-DD)", example = "1988-11-06", required = true)
    private LocalDate birthdate;
}
