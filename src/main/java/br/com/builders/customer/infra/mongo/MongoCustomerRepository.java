package br.com.builders.customer.infra.mongo;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.CustomerRepository;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import br.com.builders.customer.infra.mongo.repositories.CustomerMongoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MongoCustomerRepository implements CustomerRepository {
    private final ModelMapper modelMapper;
    private final CustomerMongoRepository customerMongoRepository;

    @Autowired
    public MongoCustomerRepository(final ModelMapper modelMapper,
                                   final CustomerMongoRepository customerMongoRepository) {
        this.modelMapper = modelMapper;
        this.customerMongoRepository = customerMongoRepository;
    }

    @Override
    public List<Customer> findAll() {
        List<CustomerEntity> customers = this.customerMongoRepository.findAll();
        return customers.stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public Customer findById(String customerId) {
        Optional<CustomerEntity> customer = this.customerMongoRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new IllegalArgumentException("Customer " + customerId + " not found");
        }
        return this.mapToModel(customer.get());
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = this.customerMongoRepository.save(this.mapToEntity(customer));
        return this.mapToModel(customerEntity);
    }

    private Customer mapToModel(CustomerEntity customer) {
        return this.modelMapper.map(customer, Customer.class);
    }

    private CustomerEntity mapToEntity(Customer customer) {
        return this.modelMapper.map(customer, CustomerEntity.class);
    }
}
