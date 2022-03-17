package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.commons.dto.FieldsDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindCustomerDomainService implements FindCustomerService {
    private final FindCustomerRepository customerRepository;

    @Autowired
    public FindCustomerDomainService(final FindCustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findCustomers(PageDataDTO pages) {
        return this.findCustomers(null, pages);
    }

    @Override
    public <T> List<Customer> findCustomers(FieldsDataDTO<T> fields, PageDataDTO pages) {
        this.validatePageParameters(pages);
        List<Customer> customers = this.findAllCustomers(fields, pages);
        return this.checkCustomers(customers) ? customers : new ArrayList<>();
    }

    @Override
    public Customer findCustomerById(String customerId) throws ResourceNotFoundException {
        this.validateCustomerId(customerId);
        return this.customerRepository.findById(customerId);
    }

    private <T> List<Customer> findAllCustomers(FieldsDataDTO<T> fields, PageDataDTO pages) {
        return fields == null
                ? this.customerRepository.findAll(pages)
                : this.customerRepository.findAll(fields, pages);
    }

    private void validatePageParameters(PageDataDTO pages) {
        if (pages == null) {
            throw new AppErrorException("Invalid filter customers without page parameters");
        }
    }

    private void validateCustomerId(String customerId) {
        if (customerId == null) {
            throw new AppErrorException("Invalid customerId for find customer");
        }
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }
}
