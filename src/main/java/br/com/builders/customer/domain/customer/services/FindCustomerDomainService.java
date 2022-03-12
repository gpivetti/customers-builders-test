package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.commons.dto.FieldFilterData;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.dto.FindCustomersParamsDTO;
import br.com.builders.customer.domain.customer.repository.CustomerRepository;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FindCustomerDomainService implements FindCustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public FindCustomerDomainService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findCustomers() {
        return findCustomers(null);
    }

    @Override
    public List<Customer> findCustomers(FindCustomersParamsDTO findCustomersParamsDTO) {
        List<Customer> customers = this.customerRepository.findAll();
        return this.checkCustomers(customers)
                ? customers
                : new ArrayList<>();
    }

    @Override
    public Customer findCustomerById(String customerId) throws ResourceNotFoundException {
        Customer customer = this.customerRepository.findById(customerId);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer", this.normalizeNotFoundCustomerExceptionFilters(customerId));
        }
        return customer;
    }

    private Map<String, String> normalizeNotFoundCustomerExceptionFilters(String customerId) {
        return new HashMap<>(){{ put("customerId", customerId); }};
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }
}
