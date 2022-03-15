package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.customer.helpers.CustomerTestHelper;
import br.com.builders.customer.application.dto.ApiResponseErrorDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[UT] On Processing PutCustomerController")
public class PutCustomerControllerTests {

    @LocalServerPort
    private int port;

    @MockBean
    private SaveCustomerService saveCustomerService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void beforeEach(){
        reset(this.saveCustomerService);
    }

    @Test
    @DisplayName("On Put Customer: Should return success on customer update when parameters are correct")
    public void shouldReturnSuccessWhenAllParametersAreCorrect() {
        Customer customer = CustomerTestHelper.getCustomers().get(0);
        when(this.saveCustomerService.update(any(String.class), any(SaveCustomerDto.class))).thenReturn(customer);

        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port, customer.getId()),
                CustomerTestHelper.getCustomerToSave(customer),
                CustomerDto.class);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        this.assertCustomerFields(response.getBody(), CustomerTestHelper.getCustomers().get(0));
    }

    @Test
    @DisplayName("On Put Customer: Should return error when put payload is null")
    public void shouldReturnErrorWhenPostPayloadIsNull() {
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port, "any_id"),
                null,
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("On Put Customer: Should return error when customerId is null")
    public void shouldReturnErrorWhenCustomerIdIsNull() {
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port),
                CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0)),
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Test
    @DisplayName("On Put Customer: Should return error when ResourceNotFound was returned")
    public void shouldReturnErrorWhenResourceNotFoundWasReturned() {
        when(this.saveCustomerService.update(any(String.class), any(SaveCustomerDto.class)))
                .thenThrow(ResourceNotFoundException.class);
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port, "any_id"),
                CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0)),
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("On Put Customer: Should return error when errors occurs to save customer")
    public void shouldReturnErrorWhenErrorsOccursToSaveCustomer() {
        when(this.saveCustomerService.update(any(String.class), any(SaveCustomerDto.class)))
                .thenThrow(RuntimeException.class);
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port, "any_id"),
                CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0)),
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("On Put Customer: Should return error when saved customer is null")
    public void shouldReturnErrorWhenSavedCustomerIsNull() {
        when(this.saveCustomerService.insert(any(SaveCustomerDto.class))).thenReturn(null);
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port, "any_id"),
                CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0)),
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void assertCustomerFields(CustomerDto returnedBody, Customer mockedCustomer) {
        assertEquals(returnedBody.getId(), mockedCustomer.getId());
        assertEquals(returnedBody.getName(), mockedCustomer.getName());
        assertEquals(returnedBody.getDocument(), mockedCustomer.getDocument());
        assertEquals(returnedBody.getAge(), mockedCustomer.getAge());
    }

    public <T> ResponseEntity<T> mapCustomersResponse(String url, Object payload, Class<T> clazz) {
        HttpEntity<Object> request = new HttpEntity<>(payload, CustomerTestHelper.getDefaultHeaders());
        return restTemplate.exchange(url, HttpMethod.PUT, request, clazz);
    }
}
