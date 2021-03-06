package br.com.builders.customer.infra.mongo.repositories.customer.mappers;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import br.com.builders.customer.main.mappings.ObjectMapping;
import org.modelmapper.ModelMapper;

public class CustomerEntityMapper implements ObjectMapping {
    @Override
    public void addMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(Customer.class, CustomerEntity.class);
        modelMapper.typeMap(CustomerEntity.class, Customer.class);
    }
}