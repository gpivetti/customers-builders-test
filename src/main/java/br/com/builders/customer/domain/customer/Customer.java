package br.com.builders.customer.domain.customer;

import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Data
@AllArgsConstructor
public class Customer {
    private String id;
    private String name;
    private String document;
    private LocalDate birthdate;

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        return this.birthdate == null || this.birthdate.isAfter(currentDate)
                ? 0
                : Period.between(this.birthdate, currentDate).getYears();
    }

    public String getAgeNormalized() {
        LocalDate currentDate = LocalDate.now();
        if (this.birthdate == null || birthdate.isAfter(currentDate)) {
            return "0y0m0d";
        } else {
            Period period = Period.between(this.birthdate, currentDate);
            return period.getYears() + "y" + period.getMonths() + "y" + period.getDays() + "d";
        }
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