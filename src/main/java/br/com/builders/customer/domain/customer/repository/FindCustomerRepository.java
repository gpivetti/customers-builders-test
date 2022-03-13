package br.com.builders.customer.domain.customer.repository;

import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.PageFiltersDataDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.dto.FiltersCustomerDto;

import java.util.List;

public interface FindCustomerRepository {
    List<Customer> findAll(PageFiltersDataDTO pageFilters);
    List<Customer> findAll(FiltersDataDTO<FiltersCustomerDto> filters, PageFiltersDataDTO pageFilters);
    Customer findById(String customerId);
    Customer findByDocument(String document);
}
