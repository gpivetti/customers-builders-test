package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.customer.helpers.CustomerTestHelper;
import br.com.builders.customer.commons.dto.ApiResponseErrorDTO;
import br.com.builders.customer.domain.customer.FindCustomerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("When Call Get Customer's Routes")
public class GetCustomerControllerTests {

    @LocalServerPort
    private int port;

    @MockBean
    private FindCustomerService findCustomerService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should return customers collection when get all return with success")
    public void shouldReturnCustomersWhenGetAllReturnsWithSuccess() {
        when(this.findCustomerService.findCustomers()).thenReturn(CustomerTestHelper.getCustomers());
        ResponseEntity<CustomerDto[]> response =
                restTemplate.getForEntity(CustomerTestHelper.makeGetUrl(this.port), CustomerDto[].class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).length, CustomerTestHelper.getCustomers().size());
    }

    @Test
    @DisplayName("Should return blank customers collection when has no customer to return")
    public void shouldReturnBlankCustomersWhenHasNoCustomersToReturn() {
        when(this.findCustomerService.findCustomers()).thenReturn(new ArrayList<>());
        ResponseEntity<CustomerDto[]> response =
                restTemplate.getForEntity(CustomerTestHelper.makeGetUrl(this.port), CustomerDto[].class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Objects.requireNonNull(response.getBody()).length, 0);
    }

    @Test
    @DisplayName("should throw InternalServerError when errors occurs to find customer")
    public void shouldThrowInternalServerErrorWhenErrorsOccursToFindCustomer() {
        when(this.findCustomerService.findCustomers()).thenThrow(RuntimeException.class);
        ResponseEntity<ApiResponseErrorDTO> response =
                restTemplate.getForEntity(CustomerTestHelper.makeGetUrl(this.port), ApiResponseErrorDTO.class);
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertNotNull(response.getBody());
    }
}
