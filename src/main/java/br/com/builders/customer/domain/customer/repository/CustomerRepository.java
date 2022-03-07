package br.com.builders.customer.domain.customer.repository;

import br.com.builders.customer.domain.customer.Customer;

public interface CustomerRepository {
    Customer getById(String customerId);
    Customer save(Customer customer);
}
