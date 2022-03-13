package br.com.builders.customer.domain.customer.validator;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultCustomerBusinessValidator implements CustomerBusinessValidator {
    private final FindCustomerRepository customerRepository;

    @Autowired
    public DefaultCustomerBusinessValidator(final FindCustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public void validate(Customer customer) throws InvalidConstraintException {
        if (customer == null) {
            throw new InvalidConstraintException("Invalid Customer to Validate");
        }
        this.validateDocument(customer);
    }

    public void validateDocument(Customer customer) {
        Customer insertedCustomer = this.customerRepository.findByDocument(customer.getDocument());
        if (insertedCustomer != null && !insertedCustomer.getId().equals(customer.getId())) {
            throw new InvalidConstraintException("Document already inserted to another customer");
        }
    }
}
