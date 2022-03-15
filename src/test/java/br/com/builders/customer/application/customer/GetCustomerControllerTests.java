package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.customer.helpers.CustomerTestHelper;
import br.com.builders.customer.application.dto.ApiResponseErrorDTO;
import br.com.builders.customer.application.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.application.dto.GenericPaginatedResponseDTO;
import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.domain.log.LogService;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

import java.lang.reflect.Type;

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
@SuppressWarnings({"rawtypes", "unchecked"})
public class GetCustomerControllerTests {

    @LocalServerPort
    private int port;

    @MockBean
    private FindCustomerService findCustomerService;

    @MockBean
    private LogService logService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void beforeEach(){
        reset(this.logService);
        reset(this.findCustomerService);
    }

    @Test
    @DisplayName("On Get Customers: Should return customers list when it has many customers inserted")
    public void shouldReturnCustomersWhenHasManyCustomersInserted() {
        when(this.findCustomerService.findCustomers(any(PageDataDTO.class)))
                .thenReturn(CustomerTestHelper.getCustomers());

        var response = this.makingGetRequest(CustomerTestHelper.makeUrl(this.port), GenericPaginatedResponseDTO.class);
        List<CustomerDto> customers = this.mapCustomersResponse(response.getBody());

        assertNotNull(customers);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(customers.size(), CustomerTestHelper.getCustomers().size());
        IntStream.range(0, CustomerTestHelper.getCustomers().size())
                .forEach(index ->
                        this.assertCustomerFields(customers.get(index), CustomerTestHelper.getCustomers().get(index)));
    }

    @Test
    @DisplayName("On Get Customers: Should return customers list when it has only one customers inserted")
    public void shouldReturnCustomersWhenHasManyOnlyOneCustomerInserted() {
        when(this.findCustomerService.findCustomers(any(PageDataDTO.class)))
                .thenReturn(List.of(CustomerTestHelper.getCustomers().get(0)));

        var response = this.makingGetRequest(CustomerTestHelper.makeUrl(this.port), GenericPaginatedResponseDTO.class);
        List<CustomerDto> customers = this.mapCustomersResponse(response.getBody());

        assertNotNull(customers);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(customers.size(), 1);
        this.assertCustomerFields(customers.get(0), CustomerTestHelper.getCustomers().get(0));
    }

    @Test
    @DisplayName("On Get Customers: Should return blank customers list when has no customer to return")
    public void shouldReturnBlankCustomersWhenHasNoCustomersToReturn() {
        when(this.findCustomerService.findCustomers(any(PageDataDTO.class)))
                .thenReturn(new ArrayList<>());

        var response = this.makingGetRequest(CustomerTestHelper.makeUrl(this.port), GenericPaginatedResponseDTO.class);
        List<CustomerDto> customers = this.mapCustomersResponse(response.getBody());

        assertNotNull(customers);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(customers.size(), 0);
    }

    @Test
    @DisplayName("On Get Customers: Should return blank customers list when service return null")
    public void shouldReturnBlankCustomersWhenServiceReturnNull() {
        when(this.findCustomerService.findCustomers(any(PageDataDTO.class)))
                .thenReturn(null);

        var response = this.makingGetRequest(CustomerTestHelper.makeUrl(this.port), GenericPaginatedResponseDTO.class);
        List<CustomerDto> customers = this.mapCustomersResponse(response.getBody());

        assertNotNull(customers);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(customers.size(), 0);
    }

    @Test
    @DisplayName("On Get Customers: Should return customers list when request was called with page filters")
    public void shouldReturnCustomersWhenRequestWasCalledWithPageFilters() {
        when(this.findCustomerService.findCustomers(any(PageDataDTO.class)))
                .thenReturn(CustomerTestHelper.getCustomers());

        String url = CustomerTestHelper.makeUrl(this.port) + "?page=1&size=10";
        var response = this.makingGetRequest(url, GenericPaginatedResponseDTO.class);
        List<CustomerDto> customers = this.mapCustomersResponse(response.getBody());

        assertNotNull(customers);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(customers.size(), CustomerTestHelper.getCustomers().size());
        IntStream.range(0, CustomerTestHelper.getCustomers().size())
                .forEach(index ->
                    this.assertCustomerFields(customers.get(index), CustomerTestHelper.getCustomers().get(index))
                );
    }

    @Test
    @DisplayName("On Get Customers: Should return customers list when request was called with page filters and filters")
    public void shouldReturnCustomersWhenRequestWasCalledWithPageFiltersAndFilters() {
        when(this.findCustomerService.findCustomers(any(FiltersDataDTO.class), any(PageDataDTO.class)))
                .thenReturn(CustomerTestHelper.getCustomers());

        String url = CustomerTestHelper.makeUrl(this.port) +
                "?page=1&size=10&filter=name:eq:Gabriel&filter=document:ne:123";
        var response = this.makingGetRequest(url, GenericPaginatedResponseDTO.class);
        List<CustomerDto> customers = this.mapCustomersResponse(response.getBody());

        assertNotNull(customers);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(customers.size(), CustomerTestHelper.getCustomers().size());
        IntStream.range(0, CustomerTestHelper.getCustomers().size())
                .forEach(index ->
                        this.assertCustomerFields(customers.get(index), CustomerTestHelper.getCustomers().get(index))
                );
    }

    @Test
    @DisplayName("On Get Customers: Should throw InternalServerError when errors occurs to find customer")
    public void shouldThrowInternalServerErrorWhenErrorsOccursToFindCustomer() {
        when(this.findCustomerService.findCustomers(any(PageDataDTO.class))).thenThrow(RuntimeException.class);
        var response = this.makingGetRequest(CustomerTestHelper.makeUrl(this.port), ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        verify(this.logService, times(1)).sendLogError(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("On Get CustomerById: Should return customer when the customer exists")
    public void shouldReturnCustomersWhenTheCustomerExists() {
        Customer mockedCustomer = CustomerTestHelper.getCustomers().get(0);

        when(this.findCustomerService.findCustomerById(any(String.class))).thenReturn(mockedCustomer);
        var response = this.makingGetRequest(
                CustomerTestHelper.makeUrl(this.port, mockedCustomer.getId()), CustomerDto.class);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        this.assertCustomerFields(response.getBody(), mockedCustomer);
    }

    @Test
    @DisplayName("On Get CustomerById: Should return NotFound when customer not exists")
    public void shouldReturnNotFoundWhenTheCustomerNotExists() {
        when(this.findCustomerService.findCustomerById(any(String.class))).thenThrow(ResourceNotFoundException.class);
        var response = this.makingGetRequest(CustomerTestHelper.makeUrl(this.port, "any_id"),
                ApiResponseNotFoundDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("On Get CustomerById: Should return NotFound when customer returned is null")
    public void shouldReturnNotFoundWhenTheCustomerReturnedIsNull() {
        when(this.findCustomerService.findCustomerById(any(String.class))).thenReturn(null);
        var response = this.makingGetRequest(CustomerTestHelper.makeUrl(this.port, "any_id"),
                ApiResponseNotFoundDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    private void assertCustomerFields(CustomerDto returnedBody, Customer mockedCustomer) {
        assertEquals(returnedBody.getId(), mockedCustomer.getId());
        assertEquals(returnedBody.getName(), mockedCustomer.getName());
        assertEquals(returnedBody.getDocument(), mockedCustomer.getDocument());
        assertEquals(returnedBody.getAge(), mockedCustomer.getAge());
    }

    public <T> ResponseEntity<T> makingGetRequest(String url, Class<T> clazz) {
        HttpEntity<Object> request = new HttpEntity<>(CustomerTestHelper.getDefaultHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, request, clazz);
    }

    public List<CustomerDto> mapCustomersResponse(GenericPaginatedResponseDTO responseBody) {
        if (responseBody == null) return null;
        Type listType = new TypeToken<List<CustomerDto>>(){}.getType();
        return this.modelMapper.map(responseBody.getPayload(), listType);
    }
}
