package br.com.builders.customer.controllers.customer.helpers;

import br.com.builders.customer.controllers.customer.dto.InsertUpdateCustomerDto;
import br.com.builders.customer.domain.customer.Customer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

public class CustomerTestHelper {
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

    public static InsertUpdateCustomerDto getCustomerToSave(Customer customer) {
        return InsertUpdateCustomerDto.builder()
                .name(customer.getName())
                .document(customer.getDocument())
                .birthdate(customer.getBirthdate())
                .build();
    }

    public static HttpHeaders getDefaultHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth("builders", "test");
        return httpHeaders;
    }

    public static String makeUrl(int port) {
        return makeUrl(port, null);
    }

    public static String makeUrl(int port, String customerId) {
        return "http://localhost:" + port + "v1/customers" + (customerId != null ? "/" + customerId : "");
    }
}
