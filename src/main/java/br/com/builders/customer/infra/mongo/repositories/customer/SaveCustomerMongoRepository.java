package br.com.builders.customer.infra.mongo.repositories.customer;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.SaveCustomerRepository;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import br.com.builders.customer.infra.mongo.helper.MongoUpdateProcessorHelper;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class SaveCustomerMongoRepository implements SaveCustomerRepository {
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public SaveCustomerMongoRepository(final ModelMapper modelMapper,
                                       final MongoTemplate mongoTemplate) {
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Customer insert(Customer customer) {
        this.validateCustomer(customer);
        return this.mapToModel(this.insertCustomer(this.mapToEntity(customer)));
    }

    @Override
    public Customer update(Customer customer) {
        this.validateCustomer(customer);
        this.validateCustomerId(customer.getId());
        this.updateCustomer(this.mapToEntity(customer));
        return customer;
    }

    @Override
    public void delete(String customerId) {
        this.validateCustomerId(customerId);
        this.mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(customerId)), CustomerEntity.class);
    }

    private CustomerEntity insertCustomer(CustomerEntity customerEntity) {
        customerEntity.setCreatedAt(LocalDateTime.now());
        customerEntity.setUpdatedAt(LocalDateTime.now());
        return this.mongoTemplate.save(customerEntity);
    }

    private void updateCustomer(CustomerEntity customerEntity) {
        customerEntity.setUpdatedAt(LocalDateTime.now());
        this.mongoTemplate.update(CustomerEntity.class)
                .matching(Query.query(Criteria.where("id").is(customerEntity.getId())))
                .apply(this.buildUpdateFields(customerEntity))
                .all();
    }

    private Update buildUpdateFields(CustomerEntity customerEntity) {
        Update update = MongoUpdateProcessorHelper.buildUpdateByObject(customerEntity);
        if (update == null) {
            throw new InvalidParameterException("There are no valid and allowed attributes for customer update");
        }
        return update;
    }

    private Customer mapToModel(CustomerEntity customer) {
        return this.modelMapper.map(customer, Customer.class);
    }

    private CustomerEntity mapToEntity(Customer customer) {
        return this.modelMapper.map(customer, CustomerEntity.class);
    }

    private void validateCustomer(Customer customer) {
        if (customer == null) {
            throw new AppErrorException("Invalid CustomerModel on SaveCustomerRepository");
        }
    }

    private void validateCustomerId(String customerId) {
        if (StringUtils.isEmpty(customerId)) {
            throw new AppErrorException("Invalid CustomerId on SaveCustomerRepository");
        }
    }
}