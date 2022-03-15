package br.com.builders.customer.domain.customer;

import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Customer {
    private String id;
    private String name;
    private String document;
    private LocalDate birthdate;

    public int getAge() {
        return 0;
    }

    private Customer() {}

    public static Customer fromSaveCustomer(SaveCustomerDto saveCustomerDto) {
        return new Customer.Builder()
                .name(saveCustomerDto.getName())
                .document(saveCustomerDto.getDocument())
                .birthdate(saveCustomerDto.getBirthdate())
                .build();
    }

    public static Customer fromSaveCustomer(String customerId, SaveCustomerDto saveCustomerDto) {
        return new Customer.Builder()
                .id(customerId)
                .name(saveCustomerDto.getName())
                .document(saveCustomerDto.getDocument())
                .birthdate(saveCustomerDto.getBirthdate())
                .build();
    }

    public static class Builder {
        private final Customer customer;

        public Builder() {
            this.customer = new Customer();
        }

        public Builder id(String id) {
            customer.setId(id);
            return this;
        }

        public Builder name(String name) {
            customer.setName(name);
            return this;
        }

        public Builder document(String document) {
            customer.setDocument(document);
            return this;
        }

        public Builder birthdate(LocalDate birthdate) {
            customer.setBirthdate(birthdate);
            return this;
        }

        public Customer build() {
            return customer;
        }
    }
}