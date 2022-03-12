package br.com.builders.customer.domain.customer.validator;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.CustomerRepository;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultCustomerValidator implements CustomerValidator {
    private final CustomerRepository customerRepository;

    @Autowired
    public DefaultCustomerValidator(final CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public void validate(Customer customer) throws InvalidConstraintException {
        return;
    }

    public void validateDocument(Customer customer) {

    }
}
