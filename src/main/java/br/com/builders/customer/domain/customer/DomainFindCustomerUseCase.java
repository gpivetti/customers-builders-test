package br.com.builders.customer.domain.customer;

import br.com.builders.customer.application.dto.CustomerDto;
import br.com.builders.customer.application.usecases.FindCustomerUseCase;
import br.com.builders.customer.domain.customer.mappers.CustomerDtoMapper;
import br.com.builders.customer.domain.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DomainFindCustomerUseCase implements FindCustomerUseCase {
//    private final CustomerRepository customerRepository;
//
//    @Autowired
//    public DomainFindCustomerUseCase(final CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }

    @Override
    public List<CustomerDto> findCustomers() {
        List<Customer> customers = new ArrayList<>();
        return customers.stream().map(customer -> {
            customer.calculateAge();
            return CustomerDtoMapper.toCustomerDto(customer);
        }).collect(Collectors.toList());
    }
}
