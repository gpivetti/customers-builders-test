package br.com.builders.customer.application.usecases;

import br.com.builders.customer.application.dto.CustomerDto;

import java.util.List;

public interface FindCustomerUseCase {
    List<CustomerDto> findCustomers();
}
