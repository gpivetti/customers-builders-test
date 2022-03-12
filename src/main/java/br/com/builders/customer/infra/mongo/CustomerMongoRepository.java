package br.com.builders.customer.infra.mongo;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.CustomerRepository;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import br.com.builders.customer.infra.mongo.repositories.MongoRepositoryCustomer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerMongoRepository implements CustomerRepository {
    private final ModelMapper modelMapper;
    private final MongoRepositoryCustomer mongoRepositoryCustomer;

    @Autowired
    public CustomerMongoRepository(final ModelMapper modelMapper,
                                   final MongoRepositoryCustomer mongoRepositoryCustomer) {
        this.modelMapper = modelMapper;
        this.mongoRepositoryCustomer = mongoRepositoryCustomer;
    }

    @Override
    public List<Customer> findAll() {
        List<CustomerEntity> customers = this.mongoRepositoryCustomer.findAll();
        return customers.stream().map(this::mapToModel).collect(Collectors.toList());
    }

    @Override
    public Customer findById(String customerId) {
        Optional<CustomerEntity> customer = this.mongoRepositoryCustomer.findById(customerId);
        return customer.map(this::mapToModel).orElse(null);
    }

    @Override
    public Customer findByDocument(String document) {
        List<CustomerEntity> customers =
                this.mongoRepositoryCustomer.findByDocument(document, PageRequest.of(0, 1));
        return customers != null && !customers.isEmpty()
                ? this.mapToModel(customers.get(0))
                : null;
    }

    @Override
    public Customer save(Customer customer) {
        CustomerEntity customerEntity = this.mongoRepositoryCustomer.save(this.mapToEntity(customer));
        return this.mapToModel(customerEntity);
    }

    @Override
    public void delete(String customerId) {
        this.mongoRepositoryCustomer.deleteById(customerId);
    }

    private Customer mapToModel(CustomerEntity customer) {
        return this.modelMapper.map(customer, Customer.class);
    }

    private CustomerEntity mapToEntity(Customer customer) {
        return this.modelMapper.map(customer, CustomerEntity.class);
    }
}
