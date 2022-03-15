package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public <T> List<Customer> findCustomers(FiltersDataDTO<T> filters, PageDataDTO pages) {
        this.validatePageParameters(pages);
        List<Customer> customers = this.findAllCustomers(filters, pages);
        return this.checkCustomers(customers) ? customers : new ArrayList<>();
    }

    @Override
    public Customer findCustomerById(String customerId) throws ResourceNotFoundException {
        Customer customer = this.customerRepository.findById(customerId);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer", this.normalizeNotFoundCustomerExceptionFilters(customerId));
        }
        return customer;
    }

    private <T> List<Customer> findAllCustomers(FiltersDataDTO<T> filters,
                                            PageDataDTO pages) {
        return (filters == null || filters.getFields() == null || filters.getFields().isEmpty())
                ? this.customerRepository.findAll(pages)
                : this.customerRepository.findAll(filters, pages);
    }

    private Map<String, String> normalizeNotFoundCustomerExceptionFilters(String customerId) {
        return new HashMap<>(){{ put("customerId", customerId); }};
    }

    private void validatePageParameters(PageDataDTO pages) {
        if (pages == null) {
            throw new AppErrorException("Invalid filter customers without page parameters");
        }
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }
}
