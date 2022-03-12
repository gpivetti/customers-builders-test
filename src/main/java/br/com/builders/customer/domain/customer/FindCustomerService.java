package br.com.builders.customer.domain.customer;

import br.com.builders.customer.commons.dto.FieldFilterData;
import br.com.builders.customer.domain.customer.dto.FindCustomersParamsDTO;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;

import java.util.List;

public interface FindCustomerService {
    List<Customer> findCustomers();
    List<Customer> findCustomers(FindCustomersParamsDTO findCustomersParamsDTO);
    Customer findCustomerById(String customerId) throws ResourceNotFoundException;
}
