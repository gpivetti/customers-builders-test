package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.CustomerRepository;
import br.com.builders.customer.domain.customer.DeleteCustomerService;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeleteCustomerDomainService implements DeleteCustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public DeleteCustomerDomainService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void deleteCustomer(String customerId) throws ResourceNotFoundException {
        Customer customer = this.customerRepository.findById(customerId);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer", this.normalizeNotFoundCustomerExceptionFilters(customerId));
        }
        this.customerRepository.delete(customerId);
    }

    private Map<String, String> normalizeNotFoundCustomerExceptionFilters(String customerId) {
        return new HashMap<>(){{ put("customerId", customerId); }};
    }
}
