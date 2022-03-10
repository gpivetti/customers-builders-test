package br.com.builders.customer.domain.customer.services;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.CustomerRepository;
import br.com.builders.customer.domain.customer.FindCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindCustomerDomainService implements FindCustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public FindCustomerDomainService(final CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> findCustomers() {
        List<Customer> customers = this.customerRepository.findAll();
        return this.checkCustomers(customers)
                ? customers
                : new ArrayList<>();
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }
}
