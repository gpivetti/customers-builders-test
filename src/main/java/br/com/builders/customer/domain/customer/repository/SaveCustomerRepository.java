package br.com.builders.customer.domain.customer.repository;

import br.com.builders.customer.domain.customer.Customer;

public interface SaveCustomerRepository {
    Customer insert(Customer customer);
    Customer update(Customer customer);
    void delete(String customerId);
}
