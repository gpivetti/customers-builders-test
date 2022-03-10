package br.com.builders.customer.domain.customer;

import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll();
    Customer findById(String customerId);
    Customer save(Customer customer);
}