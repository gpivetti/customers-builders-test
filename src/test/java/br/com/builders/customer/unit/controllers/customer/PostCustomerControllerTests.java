package br.com.builders.customer.unit.controllers.customer;

import br.com.builders.customer.controllers.customer.dto.CustomerDTO;
import br.com.builders.customer.controllers.customer.dto.InsertUpdateCustomerDto;
import br.com.builders.customer.unit.controllers.customer.helpers.CustomerTestHelper;
import br.com.builders.customer.controllers.dto.ApiResponseErrorDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import br.com.builders.customer.domain.log.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[UT] On Processing PostCustomerController")
public class PostCustomerControllerTests {

    @LocalServerPort
    private int port;

    @MockBean
    private SaveCustomerService saveCustomerService;

    @MockBean
    private LogService logService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void beforeEach(){
        reset(this.logService);
        reset(this.saveCustomerService);
    }

    @Value("${app.api-user}")
    private String user;

    @Value("${app.api-password}")
    private String password;

    @Test
    @DisplayName("On Post Customer: Should return success on customer insert when parameters are correct")
    public void shouldReturnSuccessWhenAllParametersAreCorrect() {
        when(this.saveCustomerService.insert(any(SaveCustomerDto.class)))
                .thenReturn(CustomerTestHelper.getCustomers().get(0));
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port),
                CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0)),
                CustomerDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        this.assertCustomerFields(response.getBody(), CustomerTestHelper.getCustomers().get(0));
    }

    @Test
    @DisplayName("On Post Customer: Should return error when name of post payload is null")
    public void shouldReturnErrorWhenNameOfPostPayloadIsNull() {
        InsertUpdateCustomerDto body = CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0));
        body.setName(null);

        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port),
                body,
                CustomerDTO.class);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        verify(this.logService, times(1)).sendLogError(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("On Post Customer: Should return error when document of post payload is null")
    public void shouldReturnErrorWhenDocumentOfPostPayloadIsNull() {
        InsertUpdateCustomerDto body = CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0));
        body.setDocument(null);

        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port),
                body,
                ApiResponseErrorDTO.class);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        verify(this.logService, times(1)).sendLogError(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("On Post Customer: Should return error when birthdate of post payload is null")
    public void shouldReturnErrorWhenBirthdateOfPostPayloadIsNull() {
        InsertUpdateCustomerDto body = CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0));
        body.setBirthdate(null);

        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port),
                body,
                ApiResponseErrorDTO.class);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        verify(this.logService, times(1)).sendLogError(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("On Post Customer: Should return error when post payload is null")
    public void shouldReturnErrorWhenPostPayloadIsNull() {
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port),
                null,
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        verify(this.logService, times(1)).sendLogError(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("On Post Customer: Should return error when errors occurs to save customer")
    public void shouldReturnErrorWhenErrorsOccursToSaveCustomer() {
        when(this.saveCustomerService.insert(any(SaveCustomerDto.class))).thenThrow(RuntimeException.class);
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port),
                CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0)),
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        verify(this.logService, times(1)).sendLogError(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("On Post Customer: Should return error when saved customer is null")
    public void shouldReturnErrorWhenSavedCustomerIsNull() {
        when(this.saveCustomerService.insert(any(SaveCustomerDto.class))).thenReturn(null);
        var response = this.mapCustomersResponse(
                CustomerTestHelper.makeUrl(this.port),
                CustomerTestHelper.getCustomerToSave(CustomerTestHelper.getCustomers().get(0)),
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        verify(this.logService, times(1)).sendLogError(any(String.class), any(String.class));
    }

    private void assertCustomerFields(CustomerDTO returnedBody, Customer mockedCustomer) {
        assertEquals(returnedBody.getId(), mockedCustomer.getId());
        assertEquals(returnedBody.getName(), mockedCustomer.getName());
        assertEquals(returnedBody.getDocument(), mockedCustomer.getDocument());
        assertEquals(returnedBody.getAge(), mockedCustomer.getAge());
    }

    public <T> ResponseEntity<T> mapCustomersResponse(String url, Object payload, Class<T> clazz) {
        HttpEntity<Object> request = new HttpEntity<>(payload, CustomerTestHelper.getDefaultHeaders());
        return restTemplate.withBasicAuth(this.user, this.password).exchange(url, HttpMethod.POST, request, clazz);
    }
}
