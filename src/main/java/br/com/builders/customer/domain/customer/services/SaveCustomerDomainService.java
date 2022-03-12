package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.commons.utils.ValidatorUtils;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.CustomerRepository;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import br.com.builders.customer.domain.customer.validator.CustomerBusinessValidator;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SaveCustomerDomainService implements SaveCustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerBusinessValidator customerBusinessValidator;

    @Autowired
    public SaveCustomerDomainService(final CustomerRepository customerRepository,
                                     final CustomerBusinessValidator customerBusinessValidator) {
        this.customerRepository = customerRepository;
        this.customerBusinessValidator = customerBusinessValidator;
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
        this.customerBusinessValidator.validate(customer);
        return this.customerRepository.save(customer);
    }

    private void validateIfCustomerExists(String customerId) throws ResourceNotFoundException {
        if (this.customerRepository.findById(customerId) == null) {
            throw new ResourceNotFoundException(Customer.class.getName(),
                    this.normalizeNotFoundCustomerExceptionFilters(customerId));
        }
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().equals("")) {
            throw new AppErrorException("customerId is null to save");
        }
    }

    private void validateCustomerRequest(SaveCustomerDto customer) {
        ValidatorUtils.validate(Customer.class.getName(), customer);
    }

    private Map<String, String> normalizeNotFoundCustomerExceptionFilters(String customerId) {
        return new HashMap<>(){{ put("customerId", customerId); }};
    }
}
