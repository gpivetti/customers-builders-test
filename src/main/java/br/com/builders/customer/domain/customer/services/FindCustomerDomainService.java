package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.PageFiltersDataDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.dto.FiltersCustomerDto;
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
    public List<Customer> findCustomers(PageFiltersDataDTO pageFilters) {
        return this.findCustomers(null, pageFilters);
    }

    @Override
    public List<Customer> findCustomers(FiltersDataDTO<FiltersCustomerDto> filters, PageFiltersDataDTO pageFilters) {
        this.validatePageParameters(pageFilters);
        List<Customer> customers = this.findAllCustomers(filters, pageFilters);
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

    private List<Customer> findAllCustomers(FiltersDataDTO<FiltersCustomerDto> filters,
                                            PageFiltersDataDTO pageFilters) {
        return (filters == null || filters.getFields() == null || filters.getFields().isEmpty())
                ? this.customerRepository.findAll(pageFilters)
                : this.customerRepository.findAll(filters, pageFilters);
    }

    private Map<String, String> normalizeNotFoundCustomerExceptionFilters(String customerId) {
        return new HashMap<>(){{ put("customerId", customerId); }};
    }

    private void validatePageParameters(PageFiltersDataDTO pageFilters) {
        if (pageFilters == null) {
            throw new AppErrorException("Invalid filter customers without page parameters");
        }
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }
}
