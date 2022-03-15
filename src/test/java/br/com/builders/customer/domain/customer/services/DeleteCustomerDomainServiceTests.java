package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.DeleteCustomerService;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.domain.customer.repository.SaveCustomerRepository;
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
    private SaveCustomerRepository saveCustomerRepository;

    @BeforeEach
    public void beforeEach(){
        reset(this.findCustomerRepository);
        reset(this.saveCustomerRepository);
    }

    @Test
    @DisplayName("On Delete Customer: Should delete customer when success")
    public void shouldDeleteCustomerWhenSuccess() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        doNothing().when(this.saveCustomerRepository).delete(any(String.class));
        when(this.findCustomerRepository.findById(any(String.class))).thenReturn(mockedCustomer);

        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository);
        service.deleteCustomer(mockedCustomer.getId());

        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
        verify(this.saveCustomerRepository, times(1)).delete(mockedCustomer.getId());
    }

    @Test
    @DisplayName("On Delete Customer: Should throw Error when call service with null customerId")
    public void shouldThrowErrorWhenCallServiceWithNullCustomerId() {
        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository);
        assertThrows(AppErrorException.class, () -> service.deleteCustomer(null));
    }

    @Test
    @DisplayName("On Delete Customer: Should throw error when occurs errors on delete customers")
    public void shouldThrowErrorWhenOccursErrorsOnDeleteCustomers() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        doThrow(AppErrorException.class).when(this.saveCustomerRepository).delete(any(String.class));
        when(this.findCustomerRepository.findById(any(String.class))).thenReturn(mockedCustomer);

        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository);

        assertThrows(AppErrorException.class, () -> service.deleteCustomer(mockedCustomer.getId()));
        verify(this.saveCustomerRepository, times(1)).delete(mockedCustomer.getId());
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
    }

    @Test
    @DisplayName("On Delete Customer: Should throw error when occurs errors on find customer before delete")
    public void shouldThrowErrorWhenOccursErrorsOnFindCustomerBeforeDelete() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        when(this.findCustomerRepository.findById(any(String.class))).thenThrow(AppErrorException.class);

        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository);

        assertThrows(AppErrorException.class, () -> service.deleteCustomer(mockedCustomer.getId()));
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
    }

    @Test
    @DisplayName("On Update Customer: Should Throw ResourceNotFound when find customer returns null data " +
            "before delete")
    public void shouldThrowResourceNotFoundWhenFindReturnsNullDataCustomerBeforeDelete() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        when(this.findCustomerRepository.findById(any(String.class))).thenReturn(null);

        DeleteCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository);

        assertThrows(ResourceNotFoundException.class, () -> service.deleteCustomer(mockedCustomer.getId()));
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
    }

    private DeleteCustomerService mockService(FindCustomerRepository findCustomerRepository,
                                              SaveCustomerRepository saveCustomerRepository) {
        return new DeleteCustomerDomainService(findCustomerRepository, saveCustomerRepository);
    }
}
