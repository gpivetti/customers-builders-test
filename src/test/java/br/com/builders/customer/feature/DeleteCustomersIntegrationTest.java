package br.com.builders.customer.feature;

import br.com.builders.customer.controllers.dto.ApiResponseErrorDTO;
import br.com.builders.customer.feature.setup.CustomerMockHelper;
import br.com.builders.customer.feature.setup.TestCustomerSetup;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(value = "tst")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[IT] On Delete Customer")
public class DeleteCustomersIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestCustomerSetup customerSetup;

    @Autowired
    private TestRestTemplate restTemplate;

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
    @DisplayName("Should delete customer with success")
    public void shouldDeleteCustomerWithSuccess() {
        CustomerEntity insertedCustomer = this.customerSetup.insertData(CustomerMockHelper.getCustomers().get(0));
        ResponseEntity<Void> response = customerSetup.handleCustomersRequest(
                HttpMethod.DELETE,
                TestCustomerSetup.makeUrl(this.port) + "/" + insertedCustomer.getId(),
                Void.class);
        assertNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
        assertNull(customerSetup.getData(insertedCustomer.getId().toString()));
    }

    @Test
    @DisplayName("Should throw error when customer not exists")
    public void shouldThrowErrorWhenCustomerNotExists() {
        ResponseEntity<ApiResponseErrorDTO> response = customerSetup.handleCustomersRequest(
                HttpMethod.DELETE,
                TestCustomerSetup.makeUrl(this.port) + "/any_id",
                ApiResponseErrorDTO.class);

        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
        assertEquals(customerSetup.countData(), 0);
    }
}
