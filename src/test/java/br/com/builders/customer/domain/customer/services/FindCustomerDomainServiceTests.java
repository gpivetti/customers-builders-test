package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.domain.customer.services.helpers.CustomerServiceTestHelper;
import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.FieldsDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.commons.enums.FilterEnum;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.domain.customer.dto.FiltersCustomerDto;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.main.exceptions.AppErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[UT] On Handle FindCustomerDomainService")
@SuppressWarnings("unchecked")
public class FindCustomerDomainServiceTests {

    @MockBean
    private FindCustomerRepository customerRepository;

    @BeforeEach
    public void beforeEach(){
        reset(this.customerRepository);
    }

    @Test
    @DisplayName("On Find Customers: Should return customers on success when call service with page parameters")
    public void shouldReturnCustomersOnSuccessWhenCallServiceWithPageParameters() {
        when(this.customerRepository.findAll(any(PageDataDTO.class)))
                .thenReturn(CustomerServiceTestHelper.getCustomers());

        FindCustomerService service = this.mockService(this.customerRepository);
        List<Customer> customers = service.findCustomers(new PageDataDTO(0, 20));

        assertNotNull(customers);
        assertEquals(customers.size(), CustomerServiceTestHelper.getCustomers().size());
    }

    @Test
    @DisplayName("On Find Customers: Should return customers on success when call service with page " +
            "and filters parameters")
    public void shouldReturnCustomersOnSuccessWhenCallServiceWithPageAndFiltersParameters() {
        when(this.customerRepository.findAll(any(FiltersDataDTO.class), any(PageDataDTO.class)))
                .thenReturn(CustomerServiceTestHelper.getCustomers());

        FindCustomerService service = this.mockService(this.customerRepository);
        List<Customer> customers = service.findCustomers(this.mockValidFilters(),
                new PageDataDTO(0, 20));

        assertNotNull(customers);
        assertEquals(customers.size(), CustomerServiceTestHelper.getCustomers().size());
    }

    @Test
    @DisplayName("On Find Customers: Should throw error when repository return null data")
    public void shouldReturnEmptyCustomersWhenRepositoryReturnNullData() {
        when(this.customerRepository.findAll(any(PageDataDTO.class))).thenReturn(null);

        FindCustomerService service = this.mockService(this.customerRepository);
        List<Customer> customers = service.findCustomers(new PageDataDTO(0, 20));

        assertNotNull(customers);
        assertEquals(customers.size(), 0);
    }

    @Test
    @DisplayName("On Find Customers: Should throw error when repository return empty data")
    public void shouldReturnEmptyCustomersWhenRepositoryReturnEmptyData() {
        when(this.customerRepository.findAll(any(PageDataDTO.class))).thenReturn(new ArrayList<>());

        FindCustomerService service = this.mockService(this.customerRepository);
        List<Customer> customers = service.findCustomers(new PageDataDTO(0, 20));

        assertNotNull(customers);
        assertEquals(customers.size(), 0);
    }

    @Test
    @DisplayName("On Find Customers: Should return error when pass null page parameters")
    public void shouldReturnErrorWhenPassNullPageParameters() {
        FindCustomerService service = this.mockService(this.customerRepository);
        assertThrows(AppErrorException.class, () -> service.findCustomers(null));
    }

    @Test
    @DisplayName("On Find Customers: Should return error when pass null page and filters parameters")
    public void shouldReturnErrorWhenPassNullPageAnfFiltersParameters() {
        FindCustomerService service = this.mockService(this.customerRepository);
        assertThrows(AppErrorException.class, () -> service.findCustomers(null, null));
    }

    @Test
    @DisplayName("On Find Customers: Should throw error when occurs errors on find customers")
    public void shouldReturnThrowErrorWhenOccursErrorsOnFindCustomers() {
        when(this.customerRepository.findAll(any(PageDataDTO.class))).thenThrow(AppErrorException.class);
        FindCustomerService service = this.mockService(this.customerRepository);
        assertThrows(AppErrorException.class, () -> service.findCustomers(new PageDataDTO(0, 20)));
    }

    private FindCustomerService mockService(FindCustomerRepository customerRepository) {
        return new FindCustomerDomainService(customerRepository);
    }

    private FiltersDataDTO<FiltersCustomerDto> mockValidFilters() {
        List<FieldsDataDTO> fields = List.of(
                new FieldsDataDTO("name", FilterEnum.EQUAL, "Gabriel"),
                new FieldsDataDTO("document", FilterEnum.NOT_EQUAL, "123")
        );
        return FiltersDataDTO.fromClassFields(fields, FiltersCustomerDto.class);
    }
}
