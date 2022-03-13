package br.com.builders.customer.domain.customer;

import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.PageFiltersDataDTO;
import br.com.builders.customer.domain.customer.dto.FiltersCustomerDto;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;

import java.util.List;

public interface FindCustomerService {
    List<Customer> findCustomers(PageFiltersDataDTO pageFilters);
    List<Customer> findCustomers(FiltersDataDTO<FiltersCustomerDto> filters, PageFiltersDataDTO pageFilters);
    Customer findCustomerById(String customerId) throws ResourceNotFoundException;
}
