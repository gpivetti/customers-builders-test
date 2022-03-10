package br.com.builders.customer.domain.customer;

import br.com.builders.customer.domain.customer.Customer;

import java.util.List;

public interface FindCustomerService {
    List<Customer> findCustomers();
}
