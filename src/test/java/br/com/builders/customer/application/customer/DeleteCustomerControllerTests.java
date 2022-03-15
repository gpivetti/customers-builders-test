package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.helpers.CustomerTestHelper;
import br.com.builders.customer.application.dto.ApiResponseErrorDTO;
import br.com.builders.customer.application.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.domain.customer.DeleteCustomerService;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[UT] On Processing DeleteCustomerController")
public class DeleteCustomerControllerTests {

    @LocalServerPort
    private int port;

    @MockBean
    private DeleteCustomerService deleteCustomerService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    @DisplayName("On Delete Customer: Should return success on customer remove when parameters are correct")
    public void shouldReturnSuccessWhenAllParametersAreCorrect() {
        doNothing().when(this.deleteCustomerService).deleteCustomer(any(String.class));
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port, "any_id"),
                Void.class);
        assertNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("On Delete Customer: Should return error when ResourceNotFound was returned")
    public void shouldReturnErrorWhenResourceNotFoundWasReturned() {
        doThrow(ResourceNotFoundException.class).when(this.deleteCustomerService).deleteCustomer(any(String.class));
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port, "any_id"),
                ApiResponseNotFoundDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("On Delete Customer: Should return error when errors occurs to save customer")
    public void shouldReturnErrorWhenErrorsOccursToSaveCustomer() {
        doThrow(RuntimeException.class).when(this.deleteCustomerService).deleteCustomer(any(String.class));
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port, "any_id"),
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public <T> ResponseEntity<T> mapCustomersResponse(String url, Class<T> clazz) {
        HttpEntity<Object> request = new HttpEntity<>(CustomerTestHelper.getDefaultHeaders());
        return restTemplate.exchange(url, HttpMethod.DELETE, request, clazz);
    }
}
