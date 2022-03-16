package br.com.builders.customer.unit.domain.customer.services;

import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import br.com.builders.customer.domain.customer.services.SaveCustomerDomainService;
import br.com.builders.customer.unit.domain.customer.services.helpers.CustomerServiceTestHelper;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.domain.customer.repository.SaveCustomerRepository;
import br.com.builders.customer.domain.customer.validator.CustomerBusinessValidator;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ObjectValidationException;
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
@DisplayName("[UT] On Handle SaveCustomerDomainService for Insert")
public class InsertCustomerDomainServiceTests {

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
    @DisplayName("On Insert Customer: Should return customer when success on validations")
    public void shouldReturnCustomersOnSuccessWhenCallServiceWithPageParameters() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        SaveCustomerDto mockedSaveCustomer = CustomerServiceTestHelper.mapCustomerToSave(mockedCustomer);
        when(this.saveCustomerRepository.insert(any(Customer.class))).thenReturn(mockedCustomer);

        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);
        Customer customer = service.insert(mockedSaveCustomer);

        verify(this.saveCustomerRepository, times(1))
                .insert(CustomerServiceTestHelper.mapSaveToCustomer(mockedSaveCustomer));
        assertNotNull(customer);
        assertEquals(customer.getId(), mockedCustomer.getId());
        assertEquals(customer.getName(), mockedCustomer.getName());
        assertEquals(customer.getDocument(), mockedCustomer.getDocument());
        assertEquals(customer.getAge(), mockedCustomer.getAge());
    }

    @Test
    @DisplayName("On Insert Customer: Should throw Error when call service with null data")
    public void shouldThrowErrorWhenCallServiceWithNullData() {
        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);
        assertThrows(AppErrorException.class, () -> service.insert(null));
    }

    @Test
    @DisplayName("On Insert Customer: Should throw Error when call service with data with null name")
    public void shouldThrowErrorWhenCallServiceWithDataWithNullName() {
        SaveCustomerDto mockData =
                CustomerServiceTestHelper.mapCustomerToSave(CustomerServiceTestHelper.getCustomers().get(0));
        mockData.setName(null);

        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);

        assertThrows(ObjectValidationException.class, () -> service.insert(mockData));
    }

    @Test
    @DisplayName("On Insert Customer: Should throw Error when call service with data with null document")
    public void shouldThrowErrorWhenCallServiceWithDataWithNullDocument() {
        SaveCustomerDto mockData =
                CustomerServiceTestHelper.mapCustomerToSave(CustomerServiceTestHelper.getCustomers().get(0));
        mockData.setDocument(null);

        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);

        assertThrows(ObjectValidationException.class, () -> service.insert(mockData));
    }

    @Test
    @DisplayName("On Insert Customer: Should throw Error when call service with data with null birthdate")
    public void shouldThrowErrorWhenCallServiceWithDataWithNullBirthdate() {
        SaveCustomerDto mockData =
                CustomerServiceTestHelper.mapCustomerToSave(CustomerServiceTestHelper.getCustomers().get(0));
        mockData.setBirthdate(null);

        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);

        assertThrows(ObjectValidationException.class, () -> service.insert(mockData));
    }

    @Test
    @DisplayName("On Insert Customer: Should throw error when occurs errors on insert customers")
    public void shouldThrowErrorWhenOccursErrorsOnInsertCustomers() {
        Customer mockedCustomer = CustomerServiceTestHelper.getCustomers().get(0);
        SaveCustomerDto mockedSaveCustomer = CustomerServiceTestHelper.mapCustomerToSave(mockedCustomer);
        when(this.saveCustomerRepository.insert(any(Customer.class))).thenThrow(AppErrorException.class);

        SaveCustomerService service = this.mockService(this.findCustomerRepository, this.saveCustomerRepository,
                this.customerBusinessValidator);

        assertThrows(AppErrorException.class, () -> service.insert(mockedSaveCustomer));
        verify(this.saveCustomerRepository, times(1))
                .insert(CustomerServiceTestHelper.mapSaveToCustomer(mockedSaveCustomer));
    }

    private SaveCustomerService mockService(FindCustomerRepository findCustomerRepository,
                                            SaveCustomerRepository saveCustomerRepository,
                                            CustomerBusinessValidator customerBusinessValidator) {
        return new SaveCustomerDomainService(findCustomerRepository, saveCustomerRepository, customerBusinessValidator);
    }
}
