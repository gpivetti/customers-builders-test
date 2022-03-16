package br.com.builders.customer.feature;

import br.com.builders.customer.controllers.customer.dto.CustomerDTO;
import br.com.builders.customer.controllers.customer.dto.InsertUpdateCustomerDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles(value = "tst")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[IT] On Save Customer")
public class SaveCustomersIntegrationTest {
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
    @DisplayName("Should insert customer with success")
    public void shouldInsertCustomerWithSuccess() {
        ResponseEntity<CustomerDTO> response = customerSetup.handleCustomersRequest(
                HttpMethod.POST,
                TestCustomerSetup.makeUrl(this.port),
                CustomerMockHelper.getCustomerForInsert(),
                CustomerDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        CustomerEntity insertedCustomer = customerSetup.getData(response.getBody().getId());
        assertNotNull(insertedCustomer);
    }

    @Test
    @DisplayName("Should throw error when required field is null")
    public void shouldThrowErrorWhenRequiredFieldIsNull() {
        InsertUpdateCustomerDto customerForInsert = CustomerMockHelper.getCustomerForInsert();
        customerForInsert.setName(null);

        ResponseEntity<ApiResponseErrorDTO> response = customerSetup.handleCustomersRequest(
                HttpMethod.POST,
                TestCustomerSetup.makeUrl(this.port),
                customerForInsert,
                ApiResponseErrorDTO.class);
        assertNotNull(response.getBody());
        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        assertEquals(customerSetup.countData(), 0);
    }
}
