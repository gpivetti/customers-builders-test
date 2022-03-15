package br.com.builders.customer.domain.customer.validator;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[UT] On Handle DefaultCustomerBusinessValidatorTests")
public class DefaultCustomerBusinessValidatorTests {
    @MockBean
    private FindCustomerRepository findCustomerRepository;

    @BeforeEach
    public void beforeEach(){
        reset(this.findCustomerRepository);
    }

    @Test
    @DisplayName("On Validate Customer: Should not validate when already exists another customer with same document")
    public void shouldNotValidateWhenAlreadyExistsAnotherCustomerWithSameDocument() {
        Customer anotherCustomer = mockedCustomer();
        anotherCustomer.setId("2");

        when(this.findCustomerRepository.findByDocument(any(String.class))).thenReturn(anotherCustomer);
        CustomerBusinessValidator validator = mockValidator(this.findCustomerRepository);

        assertThrows(InvalidConstraintException.class, () -> validator.validate(mockedCustomer()));
        verify(this.findCustomerRepository, times(1))
                .findByDocument(mockedCustomer().getDocument());
    }

    @Test
    @DisplayName("On Validate Customer: Should validate when the document is null")
    public void shouldValidateWhenDocumentIsNull() {
        CustomerBusinessValidator validator = mockValidator(this.findCustomerRepository);
        Customer mockedCustomer = mockedCustomer();
        mockedCustomer.setDocument(null);
        assertDoesNotThrow(() -> validator.validate(mockedCustomer));
    }

    @Test
    @DisplayName("On Validate Customer: Should validate when the customer is the only with the document")
    public void shouldValidateWhenTheCustomerIsTheOnlyWithTheDocument() {
        when(this.findCustomerRepository.findByDocument(any(String.class))).thenReturn(mockedCustomer());
        CustomerBusinessValidator validator = mockValidator(this.findCustomerRepository);

        assertDoesNotThrow(() -> validator.validate(mockedCustomer()));
        verify(this.findCustomerRepository, times(1))
                .findByDocument(mockedCustomer().getDocument());
    }

    @Test
    @DisplayName("On Validate Customer: Should validate when the find customer by document returns null")
    public void shouldValidateWhenFindCustomerByDocumentReturnsNull() {
        when(this.findCustomerRepository.findByDocument(any(String.class))).thenReturn(null);
        CustomerBusinessValidator validator = mockValidator(this.findCustomerRepository);

        assertDoesNotThrow(() -> validator.validate(mockedCustomer()));
        verify(this.findCustomerRepository, times(1))
                .findByDocument(mockedCustomer().getDocument());
    }

    @Test
    @DisplayName("On Validate Customer: Should not validate validate when the birthdate is greater than current date")
    public void shouldNotValidateWhenTheBirthdateIsGreaterThanCurrentDate() {
        when(this.findCustomerRepository.findByDocument(any(String.class))).thenReturn(null);
        CustomerBusinessValidator validator = mockValidator(this.findCustomerRepository);

        Customer mockedCustomer = mockedCustomer();
        mockedCustomer.setBirthdate(mockedCustomer.getBirthdate().plusDays(1));

        assertThrows(InvalidConstraintException.class, () -> validator.validate(mockedCustomer));
        verify(this.findCustomerRepository, times(1))
                .findByDocument(mockedCustomer().getDocument());
    }

    @Test
    @DisplayName("On Validate Customer: Should validate validate when the birthdate is less or equal than current date")
    public void shouldValidateWhenTheBirthdateIsLessOrEqualThanCurrentDate() {
        when(this.findCustomerRepository.findByDocument(any(String.class))).thenReturn(null);
        CustomerBusinessValidator validator = mockValidator(this.findCustomerRepository);

        Customer mockedCustomer = mockedCustomer();
        assertDoesNotThrow(() -> validator.validate(mockedCustomer()));

        Customer mockedCustomer2 = mockedCustomer();
        mockedCustomer2.setBirthdate(mockedCustomer.getBirthdate().minusDays(1));
        assertDoesNotThrow(() -> validator.validate(mockedCustomer()));

        verify(this.findCustomerRepository, times(2))
                .findByDocument(mockedCustomer().getDocument());
    }

    @Test
    @DisplayName("On Validate Customer: Should validate validate when the birthdate is null")
    public void shouldValidateWhenTheBirthdateIsNull() {
        when(this.findCustomerRepository.findByDocument(any(String.class))).thenReturn(null);
        CustomerBusinessValidator validator = mockValidator(this.findCustomerRepository);

        Customer mockedCustomer = mockedCustomer();
        mockedCustomer.setBirthdate(null);

        assertDoesNotThrow(() -> validator.validate(mockedCustomer()));
        verify(this.findCustomerRepository, times(1))
                .findByDocument(mockedCustomer().getDocument());
    }

    private Customer mockedCustomer() {
        return new Customer.Builder()
                .id("1")
                .name("Customer1")
                .document("100100")
                .birthdate(LocalDate.now())
                .build();
    }

    private CustomerBusinessValidator mockValidator(FindCustomerRepository findCustomerRepository) {
        return new DefaultCustomerBusinessValidator(findCustomerRepository);
    }
}
