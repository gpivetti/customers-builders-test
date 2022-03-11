package br.com.builders.customer.domain.customer;

import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;

public interface SaveCustomerService {
    Customer insert(SaveCustomerDto saveCustomer);
    Customer update(String customerId, SaveCustomerDto saveCustomer) throws ResourceNotFoundException;
}
