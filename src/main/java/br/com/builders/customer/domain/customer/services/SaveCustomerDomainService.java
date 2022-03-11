package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.commons.utils.ValidatorUtils;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.CustomerRepository;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.domain.customer.dto.SaveCustomerDto;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveCustomerDomainService implements SaveCustomerService {
    private final CustomerRepository customerRepository;
    private final FindCustomerService findCustomerService;

    @Autowired
    public SaveCustomerDomainService(final CustomerRepository customerRepository,
                                     final FindCustomerService findCustomerService) {
        this.customerRepository = customerRepository;
        this.findCustomerService = findCustomerService;
    }

    @Override
    public Customer insert(SaveCustomerDto saveCustomer) {
        this.validateData(saveCustomer);
        return this.customerRepository.save(Customer.fromSaveCustomer(saveCustomer));
    }

    @Override
    public Customer update(String customerId, SaveCustomerDto saveCustomer) {
        this.validateData(saveCustomer);
        this.validateCustomerId(customerId);
        this.validateIfCustomerExists(customerId);
        return this.customerRepository.save(Customer.fromSaveCustomer(customerId, saveCustomer));
    }

    private void validateIfCustomerExists(String customerId) throws ResourceNotFoundException {
        if (this.findCustomerService.findCustomerById(customerId) == null) {
            throw new ResourceNotFoundException();
        }
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().equals("")) {
            throw new AppErrorException("customerId is null");
        }
    }

    private void validateData(SaveCustomerDto customer) {
        ValidatorUtils.validate("Customer", customer);
    }
}
