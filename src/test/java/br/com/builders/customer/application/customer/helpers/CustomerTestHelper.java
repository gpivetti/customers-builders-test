package br.com.builders.customer.application.customer.helpers;

import br.com.builders.customer.domain.customer.Customer;

import java.util.Date;
import java.util.List;

public class CustomerTestHelper {
    public static List<Customer> getCustomers() {
        return List.of(
            new Customer.Builder()
                    .id("1")
                    .name("Customer1")
                    .document("AnyDocument")
                    .birthdate(new Date())
                    .build(),
            new Customer.Builder()
                    .id("2")
                    .name("Customer2")
                    .document("AnyDocument")
                    .birthdate(new Date())
                    .build(),
            new Customer.Builder()
                    .id("3")
                    .name("Customer3")
                    .document("AnyDocument")
                    .birthdate(new Date())
                    .build()
        );
    }

    public static String makeUrl(int port) {
        return makeUrl(port, null);
    }

    public static String makeUrl(int port, String customerId) {
        return "http://localhost:" + port + "v1/customers" + (customerId != null ? "/" + customerId : "");
    }
}
