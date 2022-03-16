package br.com.builders.customer.controllers.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UpdateCustomerDto {
    @Schema(description = "Customer's name")
    private String name;

    @Schema(description = "Customer's document (only numbers allowed)")
    private String document;

    @Schema(description = "Customer's birthdate (pattern: YYYY-MM-DD)", example = "1988-11-06")
    private LocalDate birthdate;
}
