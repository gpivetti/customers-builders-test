package br.com.builders.customer.application.customer.services;

import br.com.builders.customer.domain.customer.Customer;

import java.util.List;

public interface FindCustomerService {
    List<Customer> findCustomers();
}
