package br.com.builders.customer.domain.customer;

import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;

import java.util.List;

public interface FindCustomerService {
    List<Customer> findCustomers(PageDataDTO pages);
    <T> List<Customer> findCustomers(FiltersDataDTO<T> filters, PageDataDTO pages);
    Customer findCustomerById(String customerId) throws ResourceNotFoundException;
}
