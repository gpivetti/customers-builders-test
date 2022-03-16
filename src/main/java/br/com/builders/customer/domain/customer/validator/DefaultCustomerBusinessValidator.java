package br.com.builders.customer.domain.customer.validator;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DefaultCustomerBusinessValidator implements CustomerBusinessValidator {
    private final FindCustomerRepository customerRepository;

    @Autowired
    public DefaultCustomerBusinessValidator(final FindCustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public void validate(Customer customer) throws InvalidConstraintException {
        this.validateCustomerRequest(customer);
        this.validateDocument(customer);
        this.validateBirthdate(customer);
    }

    public void validateCustomerRequest(Customer customer) {
        if (customer == null) {
            throw new InvalidConstraintException("Invalid Customer to Validate");
        }
    }

    public void validateDocument(Customer customer) {
        if (StringUtils.isEmpty(customer.getDocument())) return;
        Customer insertedCustomer = this.customerRepository.findByDocument(customer.getDocument());
        if (insertedCustomer != null && !insertedCustomer.getId().equals(customer.getId())) {
            throw new InvalidConstraintException("Document already inserted to another customer");
        }
    }

    public void validateBirthdate(Customer customer) {
        if (customer.getBirthdate() == null) return;
        if (customer.getBirthdate().isAfter(LocalDate.now())) {
            throw new InvalidConstraintException("Not allowed birthdate greater than the current date");
        }
    }
}
