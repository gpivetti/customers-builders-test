package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.domain.customer.DeleteCustomerService;
import br.com.builders.customer.domain.customer.repository.SaveCustomerRepository;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeleteCustomerDomainService implements DeleteCustomerService {
    private final FindCustomerRepository findCustomerRepository;
    private final SaveCustomerRepository saveCustomerRepository;

    @Autowired
    public DeleteCustomerDomainService(final FindCustomerRepository findCustomerRepository,
                                       final SaveCustomerRepository saveCustomerRepository) {
        this.findCustomerRepository = findCustomerRepository;
        this.saveCustomerRepository = saveCustomerRepository;
    }

    @Override
    public void deleteCustomer(String customerId) throws ResourceNotFoundException {
        this.validateCustomerId(customerId);
        this.validateIfCustomerExists(customerId);
        this.saveCustomerRepository.delete(customerId);
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null || customerId.trim().equals("")) {
            throw new AppErrorException("customerId is null to delete");
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
