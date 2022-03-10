package br.com.builders.customer.application.customer.helpers;

import br.com.builders.customer.domain.customer.Customer;

import java.util.Date;
import java.util.List;

public class CustomerTestHelper {
    public static List<Customer> getCustomers() {
        return List.of(
            Customer.builder()
                    .id("1")
                    .name("Customer1")
                    .document("AnyDocument")
                    .birthdate(new Date())
                    .build(),
            Customer.builder()
                    .id("2")
                    .name("Customer2")
                    .document("AnyDocument")
                    .birthdate(new Date())
                    .build(),
            Customer.builder()
                    .id("3")
                    .name("Customer3")
                    .document("AnyDocument")
                    .birthdate(new Date())
                    .build()
        );
    }

    public static String makeGetUrl(int port) {
        return "http://localhost:" + port + "v1/customer";
    }
}