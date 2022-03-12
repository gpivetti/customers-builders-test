package br.com.builders.customer.domain.customer.repository;

import br.com.builders.customer.commons.dto.FieldFilterData;
import br.com.builders.customer.commons.dto.PageFiltersData;
import br.com.builders.customer.commons.enums.SortingEnum;
import br.com.builders.customer.domain.customer.Customer;

import java.util.List;

public interface CustomerRepository {
    List<Customer> findAll();
    List<Customer> findAll(List<FieldFilterData> filters);
    List<Customer> findAll(PageFiltersData pageFilters);
    List<Customer> findAll(SortingEnum sorting);
    List<Customer> findAll(List<FieldFilterData> filters, PageFiltersData pageFilters);
    List<Customer> findAll(List<FieldFilterData> filters, SortingEnum sorting);
    List<Customer> findAll(PageFiltersData pageFilters, SortingEnum sorting);
    List<Customer> findAll(List<FieldFilterData> filters, PageFiltersData pageFilters, SortingEnum sorting);
    Customer findById(String customerId);
    Customer findByDocument(String document);
    Customer save(Customer customer);
    void delete(String customerId);
}
