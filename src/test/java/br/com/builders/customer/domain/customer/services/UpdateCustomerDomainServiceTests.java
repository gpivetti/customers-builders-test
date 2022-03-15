package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.domain.customer.repository.SaveCustomerRepository;
import br.com.builders.customer.domain.customer.services.helpers.CustomerServiceTestHelper;
import br.com.builders.customer.domain.customer.validator.CustomerBusinessValidator;
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
@DisplayName("[UT] On Handle SaveCustomerDomainService for Update")
public class UpdateCustomerDomainServiceTests {

    @MockBean
    private FindCustomerRepository findCustomerRepository;

    @MockBean
    private SaveCustomerRepository saveCustomerRepository;

    @MockBean
    private CustomerBusinessValidator customerBusinessValidator;

    @BeforeEach
    public void beforeEach(){
        reset(this.findCustomerRepository);
        reset(this.saveCustomerRepository);
        reset(this.customerBusinessValidator);
    }

    @Test
    @DisplayName("On Update Customer: Should return customer when success on validations")
    public void shouldReturnCustomersOnSuccessWhenCallServiceWithPageParameters() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        SaveCustomerDto mockedSaveCustomer = CustomerServiceTestHelper.mapCustomerToSave(mockedCustomer);
        when(this.saveCustomerRepository.update(any(Customer.class))).thenReturn(mockedCustomer);
        when(this.findCustomerRepository.findById(any(String.class))).thenReturn(mockedCustomer);

        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);
        Customer customer = service.update(mockedCustomer.getId(), mockedSaveCustomer);

        verify(this.saveCustomerRepository, times(1)).update(mockedCustomer);
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
        assertNotNull(customer);
        assertEquals(customer.getId(), mockedCustomer.getId());
        assertEquals(customer.getName(), mockedCustomer.getName());
        assertEquals(customer.getDocument(), mockedCustomer.getDocument());
        assertEquals(customer.getAge(), mockedCustomer.getAge());
    }

    @Test
    @DisplayName("On Update Customer: Should throw Error when call service without parameters")
    public void shouldThrowErrorWhenCallServiceWithoutParameters() {
        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);
        assertThrows(AppErrorException.class, () -> service.update(null,null));
    }

    @Test
    @DisplayName("On Update Customer: Should throw Error when call service with null customerId")
    public void shouldThrowErrorWhenCallServiceWithNullCustomerId() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        SaveCustomerDto mockedSaveCustomer = CustomerServiceTestHelper.mapCustomerToSave(mockedCustomer);
        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);
        assertThrows(AppErrorException.class, () -> service.update(null,mockedSaveCustomer));
    }

    @Test
    @DisplayName("On Update Customer: Should throw Error when call service with null data")
    public void shouldThrowErrorWhenCallServiceWithNullData() {
        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);
        assertThrows(AppErrorException.class, () -> service.update("any_customer_id",null));
    }

    @Test
    @DisplayName("On Update Customer: Should throw error when occurs errors on update customers")
    public void shouldThrowErrorWhenOccursErrorsOnInsertCustomers() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        SaveCustomerDto mockedSaveCustomer = CustomerServiceTestHelper.mapCustomerToSave(mockedCustomer);
        when(this.saveCustomerRepository.update(any(Customer.class))).thenThrow(AppErrorException.class);
        when(this.findCustomerRepository.findById(any(String.class))).thenReturn(mockedCustomer);

        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);

        assertThrows(AppErrorException.class, () -> service.update(mockedCustomer.getId(), mockedSaveCustomer));
        verify(this.saveCustomerRepository, times(1)).update(mockedCustomer);
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
    }

    @Test
    @DisplayName("On Update Customer: Should throw error when occurs errors on find customer before update")
    public void shouldThrowErrorWhenOccursErrorsOnFindCustomerBeforeUpdate() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        SaveCustomerDto mockedSaveCustomer = CustomerServiceTestHelper.mapCustomerToSave(mockedCustomer);
        when(this.findCustomerRepository.findById(any(String.class))).thenThrow(AppErrorException.class);

        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);

        assertThrows(AppErrorException.class, () -> service.update(mockedCustomer.getId(), mockedSaveCustomer));
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
    }

    @Test
    @DisplayName("On Update Customer: Should Throw ResourceNotFound when find customer returns null data " +
            "before update")
    public void shouldThrowResourceNotFoundWhenFindReturnsNullDataCustomerBeforeUpdate() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        SaveCustomerDto mockedSaveCustomer = CustomerServiceTestHelper.mapCustomerToSave(mockedCustomer);
        when(this.findCustomerRepository.findById(any(String.class))).thenReturn(null);

        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);

        assertThrows(ResourceNotFoundException.class, () ->
                service.update(mockedCustomer.getId(), mockedSaveCustomer));
        verify(this.findCustomerRepository, times(1)).findById(mockedCustomer.getId());
    }

    private SaveCustomerService mockService(FindCustomerRepository findCustomerRepository,
                                            SaveCustomerRepository saveCustomerRepository,
                                            CustomerBusinessValidator customerBusinessValidator) {
        return new SaveCustomerDomainService(findCustomerRepository, saveCustomerRepository, customerBusinessValidator);
    }
}
