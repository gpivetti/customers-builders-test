package br.com.builders.customer.domain.customer.repository;

import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.domain.customer.Customer;

import java.util.List;

public interface FindCustomerRepository {
    List<Customer> findAll(PageDataDTO pages);
    <T> List<Customer> findAll(FiltersDataDTO<T> filters, PageDataDTO pages);
    Customer findById(String customerId);
    Customer findByDocument(String document);
}
