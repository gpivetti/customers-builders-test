package br.com.builders.customer.domain.customer.repository;

import br.com.builders.customer.domain.customer.Customer;

import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll();
    Customer findById(String customerId);
    Customer findByDocument(String document);
    Customer save(Customer customer);
    void delete(String customerId);
}
