package br.com.builders.customer.domain.customer;

import br.com.builders.customer.application.customer.services.FindCustomerService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DomainFindCustomerService implements FindCustomerService {
//    private final CustomerRepository customerRepository;
//
//    @Autowired
//    public DomainFindCustomerService(final CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }

    @Override
    public List<Customer> findCustomers() {
        List<Customer> customers = new ArrayList<>();
        return this.checkCustomers(customers)
                ? customers
                : new ArrayList<>();
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }
}
