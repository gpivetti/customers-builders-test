package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.DeleteCustomerService;
import br.com.builders.customer.domain.customer.repository.DeleteCustomerRepository;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.domain.customer.services.helpers.CustomerServiceTestHelper;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[UT] On Handle DeleteCustomerDomainService")
public class DeleteCustomerDomainServiceTests {

    @MockBean
    private FindCustomerRepository findCustomerRepository;

    @MockBean
    private DeleteCustomerRepository deleteCustomerRepository;

    @BeforeEach
    public void beforeEach(){
        reset(this.findCustomerRepository);
        reset(this.deleteCustomerRepository);
    }

    @Test
    @DisplayName("On Delete Customer: Should delete customer when success")
    public void shouldDeleteCustomerWhenSuccess() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        doNothing().when(this.deleteCustomerRepository).delete(any(String.class));
        when(this.findCustomerRepository.findById(any(String.class))).thenReturn(mockedCustomer);

        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.deleteCustomerRepository);
        service.deleteCustomer(mockedCustomer.getId());

        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
        verify(this.deleteCustomerRepository, times(1)).delete(mockedCustomer.getId());
    }

    @Test
    @DisplayName("On Delete Customer: Should throw Error when call service with null customerId")
    public void shouldThrowErrorWhenCallServiceWithNullCustomerId() {
        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.deleteCustomerRepository);
        assertThrows(AppErrorException.class, () -> service.deleteCustomer(null));
    }

    @Test
    @DisplayName("On Delete Customer: Should throw error when occurs errors on delete customers")
    public void shouldThrowErrorWhenOccursErrorsOnDeleteCustomers() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        doThrow(AppErrorException.class).when(this.deleteCustomerRepository).delete(any(String.class));
        when(this.findCustomerRepository.findById(any(String.class))).thenReturn(mockedCustomer);

        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.deleteCustomerRepository);

        assertThrows(AppErrorException.class, () -> service.deleteCustomer(mockedCustomer.getId()));
        verify(this.deleteCustomerRepository, times(1)).delete(mockedCustomer.getId());
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
    }

    @Test
    @DisplayName("On Delete Customer: Should throw error when occurs errors on find customer before delete")
    public void shouldThrowErrorWhenOccursErrorsOnFindCustomerBeforeDelete() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        when(this.findCustomerRepository.findById(any(String.class))).thenThrow(AppErrorException.class);

        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.deleteCustomerRepository);

        assertThrows(AppErrorException.class, () -> service.deleteCustomer(mockedCustomer.getId()));
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
    }

    @Test
    @DisplayName("On Update Customer: Should Throw ResourceNotFound when find customer returns null data " +
            "before delete")
    public void shouldThrowResourceNotFoundWhenFindReturnsNullDataCustomerBeforeDelete() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        when(this.findCustomerRepository.findById(any(String.class))).thenReturn(null);

        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.deleteCustomerRepository);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCustomer(mockedCustomer.getId()));
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
    }

    private DeleteCustomerService mockService(FindCustomerRepository findCustomerRepository,
                                              DeleteCustomerRepository deleteCustomerRepository) {
        return new DeleteCustomerDomainService(findCustomerRepository, deleteCustomerRepository);
    }
}
