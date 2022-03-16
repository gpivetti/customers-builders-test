package br.com.builders.customer.feature;

import br.com.builders.customer.controllers.customer.dto.CustomerDTO;
import br.com.builders.customer.controllers.dto.ApiResponseErrorDTO;
import br.com.builders.customer.controllers.dto.GenericPaginatedResponseDTO;
import br.com.builders.customer.feature.setup.CustomerMockHelper;
import br.com.builders.customer.feature.setup.TestCustomerSetup;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("rawtypes")
@ActiveProfiles(value = "tst")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[IT] On Get Customers")
public class GetCustomersIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestCustomerSetup customerSetup;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void beforeEach(){
        this.customerSetup.startRestTemplate(restTemplate);
        this.customerSetup.cleanDatabase();
    }

    @AfterEach
    public void afterEach(){
        this.customerSetup.cleanDatabase();
    }

    @Test
    @DisplayName("Should return all customers with success")
    public void shouldReturnAllCustomersWithSuccess() {
        List<CustomerEntity> customers = CustomerMockHelper.getCustomers();
        customers.forEach(customerSetup::insertData);

        ResponseEntity<GenericPaginatedResponseDTO> response = customerSetup.handleCustomersRequest(
                HttpMethod.GET,
                TestCustomerSetup.makeUrl(this.port),
                GenericPaginatedResponseDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        List<CustomerDTO> customerDTO = this.mapCustomersResponse(response.getBody());
        assertEquals(customerDTO.size(), 3);
    }

    @Test
    @DisplayName("Should return customer with success")
    public void shouldReturnCustomersWithSuccess() {
        List<CustomerEntity> customers = CustomerMockHelper.getCustomers();
        customers.forEach(customerSetup::insertData);

        ResponseEntity<CustomerDTO> response = customerSetup.handleCustomersRequest(
                HttpMethod.GET,
                TestCustomerSetup.makeUrl(this.port) + "/" + customers.get(0).getId(),
                CustomerDTO.class);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody().getId(), customers.get(0).getId().toString());
        assertEquals(response.getBody().getName(), customers.get(0).getName());
        assertEquals(response.getBody().getDocument(), customers.get(0).getDocument());
        assertEquals(response.getBody().getBirthdate(), customers.get(0).getBirthdate());
        assertEquals(response.getBody().getAge(), 10);
    }

    @Test
    @DisplayName("Should throw error when customer not exists")
    public void shouldThrowErrorWhenCustomerNotExists() {
        ResponseEntity<ApiResponseErrorDTO> response = customerSetup.handleCustomersRequest(
                HttpMethod.GET,
                TestCustomerSetup.makeUrl(this.port) + "/any_id",
                ApiResponseErrorDTO.class);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    public List<CustomerDTO> mapCustomersResponse(GenericPaginatedResponseDTO responseBody) {
        if (responseBody == null) return null;
        Type listType = new TypeToken<List<CustomerDTO>>(){}.getType();
        return this.modelMapper.map(responseBody.getPayload(), listType);
    }
}
