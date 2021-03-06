package br.com.builders.customer.unit.controllers.customer.helpers;

import br.com.builders.customer.controllers.customer.dto.CustomerDTO;
import br.com.builders.customer.controllers.customer.dto.InsertCustomerDto;
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
                    .birthdate(LocalDate.parse("1988-11-06"))
                    .build()
        );
    }

    public static CustomerDTO mapCustomerDTO(Customer customerDTO) {
        return CustomerDTO.fromCustomer(customerDTO);
    }

    public static InsertCustomerDto getCustomerToSave(Customer customer) {
        return InsertCustomerDto.builder()
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
