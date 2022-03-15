package br.com.builders.customer.domain.customer.services.helpers;

import br.com.builders.customer.domain.customer.Customer;

import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class CustomerServiceTestHelper {
    public static List<Customer> getCustomers() {
        return List.of(
            new Customer.Builder()
                    .id("1")
                    .name("Customer1")
                    .document("100100")
                    .birthdate(LocalDate.now())
                    .build(),
            new Customer.Builder()
                    .id("2")
                    .name("Customer2")
                    .document("100200")
                    .birthdate(LocalDate.now())
                    .build(),
            new Customer.Builder()
                    .id("3")
                    .name("Customer3")
                    .document("100300")
                    .birthdate(LocalDate.now())
                    .build()
        );
    }

    public static SaveCustomerDto mapCustomerToSave(Customer customer) {
        return SaveCustomerDto.builder()
                .name(customer.getName())
                .document(customer.getDocument())
                .birthdate(customer.getBirthdate())
                .build();
    }

    public static Customer mapSaveToCustomer(SaveCustomerDto saveCustomer) {
        return new Customer.Builder()
                .name(saveCustomer.getName())
                .document(saveCustomer.getDocument())
                .birthdate(saveCustomer.getBirthdate())
                .build();
    }
}
