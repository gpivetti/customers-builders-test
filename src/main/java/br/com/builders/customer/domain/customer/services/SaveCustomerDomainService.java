package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.commons.utils.ValidatorUtils;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.CustomerRepository;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import br.com.builders.customer.domain.customer.validator.CustomerValidator;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SaveCustomerDomainService implements SaveCustomerService {
    private final CustomerValidator customerValidator;
    private final CustomerRepository customerRepository;

    @Autowired
    public SaveCustomerDomainService(final CustomerValidator customerValidator,
                                     final CustomerRepository customerRepository) {
        this.customerValidator = customerValidator;
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer insert(SaveCustomerDto saveCustomer) {
        this.validateCustomerRequest(saveCustomer);
        return this.saveCustomer(Customer.fromSaveCustomer(saveCustomer));
    }

    @Override
    public Customer update(String customerId, SaveCustomerDto saveCustomer) {
        this.validateCustomerRequest(saveCustomer);
        this.validateCustomerId(customerId);
        this.validateIfCustomerExists(customerId);
        return this.saveCustomer(Customer.fromSaveCustomer(saveCustomer));
    }

    private Customer saveCustomer(Customer customer) {
        this.customerValidator.validate(customer);
        return this.customerRepository.save(customer);
    }

    private void validateIfCustomerExists(String customerId) throws ResourceNotFoundException {
        if (this.customerRepository.findById(customerId) == null) {
            throw new ResourceNotFoundException("Customer", this.normalizeNotFoundCustomerExceptionFilters(customerId));
        }
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().equals("")) {
            throw new AppErrorException("customerId is null");
        }
    }

    private void validateCustomerRequest(SaveCustomerDto customer) {
        ValidatorUtils.validate("Customer", customer);
    }

    private Map<String, String> normalizeNotFoundCustomerExceptionFilters(String customerId) {
        return new HashMap<>(){{ put("customerId", customerId); }};
    }
}
