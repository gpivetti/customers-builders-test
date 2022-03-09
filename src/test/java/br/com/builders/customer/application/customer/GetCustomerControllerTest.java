package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.customer.mocks.CustomerMocks;
import br.com.builders.customer.application.customer.services.FindCustomerService;
import br.com.builders.customer.domain.log.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("When Call Get Customer's Routes")
@SuppressWarnings("unchecked")
public class GetCustomerControllerTest {

    @LocalServerPort
    private int port;

    @MockBean
    private FindCustomerService findCustomerService;

    @MockBean
    private LogService logService;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setup(){
        doNothing().when(this.logService).sendLogError(any(String.class), any(String.class));
    }

    @Test
    @DisplayName("Should Return Customers on Successful")
    public void shouldReturnCustomersWhenServiceReturnsWithSuccess() {
        when(this.findCustomerService.findCustomers()).thenReturn(CustomerMocks.getCustomers());
        List<CustomerDto> customers = restTemplate.getForObject(makeUrl(), List.class);
        assertEquals(customers.size(), CustomerMocks.getCustomers().size());
    }

    private String makeUrl() {
        return "http://localhost:" + this.port + "v1/customer";
    }
}
