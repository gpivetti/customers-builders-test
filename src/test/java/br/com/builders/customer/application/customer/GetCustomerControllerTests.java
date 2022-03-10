package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.customer.helpers.CustomerTestHelper;
import br.com.builders.customer.commons.dto.ApiResponseErrorDTO;
import br.com.builders.customer.commons.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
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
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[UT] On Processing GetCustomerController")
public class GetCustomerControllerTests {

    @LocalServerPort
    private int port;

    @MockBean
    private FindCustomerService findCustomerService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("On Get Customers: Should return customers list when it has many customers inserted")
    public void shouldReturnCustomersWhenHasManyCustomersInserted() {
        when(this.findCustomerService.findCustomers()).thenReturn(CustomerTestHelper.getCustomers());
        ResponseEntity<CustomerDto[]> response =
                restTemplate.getForEntity(CustomerTestHelper.makeUrl(this.port), CustomerDto[].class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().length, CustomerTestHelper.getCustomers().size());
        IntStream.range(0, CustomerTestHelper.getCustomers().size())
                .forEach(index -> {
                    this.assertCustomerFields(response.getBody()[index], CustomerTestHelper.getCustomers().get(index));
                });
    }

    @Test
    @DisplayName("On Get Customers: Should return customers list when it has only one customers inserted")
    public void shouldReturnCustomersWhenHasManyOnlyOneCustomerInserted() {
        when(this.findCustomerService.findCustomers()).thenReturn(List.of(CustomerTestHelper.getCustomers().get(0)));
        ResponseEntity<CustomerDto[]> response =
                restTemplate.getForEntity(CustomerTestHelper.makeUrl(this.port), CustomerDto[].class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().length, 1);
        this.assertCustomerFields(response.getBody()[0], CustomerTestHelper.getCustomers().get(0));
    }

    @Test
    @DisplayName("On Get Customers: Should return blank customers list when has no customer to return")
    public void shouldReturnBlankCustomersWhenHasNoCustomersToReturn() {
        when(this.findCustomerService.findCustomers()).thenReturn(new ArrayList<>());
        ResponseEntity<CustomerDto[]> response =
                restTemplate.getForEntity(CustomerTestHelper.makeUrl(this.port), CustomerDto[].class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().length, 0);
    }

    @Test
    @DisplayName("On Get Customers: Should return blank customers list when service return null")
    public void shouldReturnBlankCustomersWhenServiceReturnNull() {
        when(this.findCustomerService.findCustomers()).thenReturn(null);
        ResponseEntity<CustomerDto[]> response =
                restTemplate.getForEntity(CustomerTestHelper.makeUrl(this.port), CustomerDto[].class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().length, 0);
    }

    @Test
    @DisplayName("On Get Customers: Should throw InternalServerError when errors occurs to find customer")
    public void shouldThrowInternalServerErrorWhenErrorsOccursToFindCustomer() {
        when(this.findCustomerService.findCustomers()).thenThrow(RuntimeException.class);
        ResponseEntity<ApiResponseErrorDTO> response =
                restTemplate.getForEntity(CustomerTestHelper.makeUrl(this.port), ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("On Get CustomerById: Should return customer when the customer exists")
    public void shouldReturnCustomersWhenTheCustomerExists() {
        Customer mockedCustomer = CustomerTestHelper.getCustomers().get(0);
        when(this.findCustomerService.findCustomerById(any(String.class))).thenReturn(mockedCustomer);
        ResponseEntity<CustomerDto> response = restTemplate.getForEntity(
                CustomerTestHelper.makeUrl(this.port, mockedCustomer.getId()), CustomerDto.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        this.assertCustomerFields(response.getBody(), mockedCustomer);
    }

    @Test
    @DisplayName("On Get CustomerById: Should return NotFound when customer not exists")
    public void shouldReturnNotFoundWhenTheCustomerNotExists() {
        when(this.findCustomerService.findCustomerById(any(String.class))).thenThrow(ResourceNotFoundException.class);
        ResponseEntity<ApiResponseNotFoundDTO> response = restTemplate.getForEntity(
                CustomerTestHelper.makeUrl(this.port, "any_id"), ApiResponseNotFoundDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("On Get CustomerById: Should return NotFound when customer returned is null")
    public void shouldReturnNotFoundWhenTheCustomerReturnedIsNull() {
        when(this.findCustomerService.findCustomerById(any(String.class))).thenReturn(null);
        ResponseEntity<ApiResponseNotFoundDTO> response = restTemplate.getForEntity(
                CustomerTestHelper.makeUrl(this.port, "any_id"), ApiResponseNotFoundDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    private void assertCustomerFields(CustomerDto returnedBody, Customer mockedCustomer) {
        assertEquals(returnedBody.getId(), mockedCustomer.getId());
        assertEquals(returnedBody.getName(), mockedCustomer.getName());
        assertEquals(returnedBody.getDocument(), mockedCustomer.getDocument());
        assertEquals(returnedBody.getAge(), mockedCustomer.getAge());
    }
}
