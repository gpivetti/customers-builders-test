package br.com.builders.customer.application.customer.helpers;

import br.com.builders.customer.application.customer.dto.InsertUpdateCustomerDto;
import br.com.builders.customer.domain.customer.Customer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Date;
import java.util.List;

public class CustomerTestHelper {
    public static List<Customer> getCustomers() {
        return List.of(
            new Customer.Builder()
                    .id("1")
                    .name("Customer1")
                    .document("100100")
                    .birthdate(new Date())
                    .build(),
            new Customer.Builder()
                    .id("2")
                    .name("Customer2")
                    .document("100200")
                    .birthdate(new Date())
                    .build(),
            new Customer.Builder()
                    .id("3")
                    .name("Customer3")
                    .document("100300")
                    .birthdate(new Date())
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
        return httpHeaders;
    }

    public static String makeUrl(int port) {
        return makeUrl(port, null);
    }

    public static String makeUrl(int port, String customerId) {
        return "http://localhost:" + port + "v1/customers" + (customerId != null ? "/" + customerId : "");
    }
}
