package br.com.builders.customer.domain.customer.adapters;

import br.com.builders.customer.controllers.customer.dto.CustomerDTO;

public interface CustomerCacheAdapter {
    CustomerDTO findById(String customerId);
    void save(CustomerDTO customerDTO);
}
