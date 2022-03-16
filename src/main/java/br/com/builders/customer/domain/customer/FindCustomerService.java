package br.com.builders.customer.domain.customer;

import br.com.builders.customer.commons.dto.FieldsDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;

import java.util.List;

public interface FindCustomerService {
    List<Customer> findCustomers(PageDataDTO pages);
    <T> List<Customer> findCustomers(FieldsDataDTO<T> fields, PageDataDTO pages);
    Customer findCustomerById(String customerId) throws ResourceNotFoundException;
}
