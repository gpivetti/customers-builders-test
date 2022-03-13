package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.commons.utils.ValidatorUtils;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import br.com.builders.customer.domain.customer.repository.SaveCustomerRepository;
import br.com.builders.customer.domain.customer.validator.CustomerBusinessValidator;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SaveCustomerDomainService implements SaveCustomerService {
    private final FindCustomerRepository findCustomerRepository;
    private final SaveCustomerRepository saveCustomerRepository;
    private final CustomerBusinessValidator customerBusinessValidator;

    @Autowired
    public SaveCustomerDomainService(final FindCustomerRepository findCustomerRepository,
                                     final SaveCustomerRepository saveCustomerRepository,
                                     final CustomerBusinessValidator customerBusinessValidator) {
        this.findCustomerRepository = findCustomerRepository;
        this.saveCustomerRepository = saveCustomerRepository;
        this.customerBusinessValidator = customerBusinessValidator;
    }

    @Override
    public Customer insert(SaveCustomerDto saveCustomer) {
        this.validateCustomerRequest(saveCustomer);
        this.validateCustomerRequestDataFields(saveCustomer);
        return this.insertCustomer(Customer.fromSaveCustomer(saveCustomer));
    }

    @Override
    public Customer update(String customerId, SaveCustomerDto saveCustomer) {
        this.validateCustomerRequest(saveCustomer);
        this.validateCustomerId(customerId);
        this.validateIfCustomerExists(customerId);
        return this.updateCustomer(Customer.fromSaveCustomer(customerId, saveCustomer));
    }

    private Customer insertCustomer(Customer customer) {
        this.customerBusinessValidator.validate(customer);
        return this.saveCustomerRepository.insert(customer);
    }

    private Customer updateCustomer(Customer customer) {
        this.customerBusinessValidator.validate(customer);
        return this.saveCustomerRepository.update(customer);
    }

    private void validateCustomerRequest(SaveCustomerDto saveCustomer) {
        if (saveCustomer == null) {
            throw new AppErrorException("customer data is null to save");
        }
    }

    private void validateCustomerRequestDataFields(SaveCustomerDto customer) {
        ValidatorUtils.validate(Customer.class.getName(), customer);
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().equals("")) {
            throw new AppErrorException("customerId is null to save");
        }
    }

    private void validateIfCustomerExists(String customerId) throws ResourceNotFoundException {
        if (this.findCustomerRepository.findById(customerId) == null) {
            throw new ResourceNotFoundException(Customer.class.getName(),
                    this.normalizeNotFoundCustomerExceptionFilters(customerId));
        }
    }

    private Map<String, String> normalizeNotFoundCustomerExceptionFilters(String customerId) {
        return new HashMap<>(){{ put("customerId", customerId); }};
    }
}
