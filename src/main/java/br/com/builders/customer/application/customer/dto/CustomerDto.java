package br.com.builders.customer.application.customer.dto;

import br.com.builders.customer.domain.customer.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDto {
    @Schema(description = "Customer's id")
    private String id;
    @Schema(description = "Customer's name")
    private String name;
    @Schema(description = "Customer's document")
    private String document;
    @Schema(description = "Customer's birthdate (pattern: YYYY-MM-DD)", example = "1988-11-06")
    private Date birthdate;
    @Schema(description = "Customer's age (in years)")
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
