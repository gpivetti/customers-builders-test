package br.com.builders.customer.domain.customer;

import br.com.builders.customer.main.exceptions.ResourceNotFoundException;

import java.util.List;

public interface FindCustomerService {
    List<Customer> findCustomers();
    Customer findCustomerById(String customerId) throws ResourceNotFoundException;
}
