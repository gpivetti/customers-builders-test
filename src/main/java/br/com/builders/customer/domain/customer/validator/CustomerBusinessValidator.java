package br.com.builders.customer.domain.customer.validator;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;

public interface CustomerBusinessValidator {
    void validate(Customer customer) throws InvalidConstraintException;
}
