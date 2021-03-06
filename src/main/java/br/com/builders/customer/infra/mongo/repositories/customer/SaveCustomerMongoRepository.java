package br.com.builders.customer.infra.mongo.repositories.customer;

import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.SaveCustomerRepository;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import br.com.builders.customer.infra.mongo.helper.MongoUpdateProcessorHelper;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.InvalidParameterException;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
        this.updateCustomer(customer.getId(), this.mapToEntity(customer));
        return customer;
    }

    private CustomerEntity insertCustomer(CustomerEntity customerEntity) {
        customerEntity.setCreatedAt(LocalDateTime.now());
        customerEntity.setUpdatedAt(LocalDateTime.now());
        customerEntity.setId(new ObjectId());
        return this.mongoTemplate.save(customerEntity);
    }

    private void updateCustomer(String customerId, CustomerEntity customerEntity) {
        customerEntity.setUpdatedAt(LocalDateTime.now());
        this.mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(customerId)),
                this.buildUpdateFields(customerEntity),
                CustomerEntity.class);
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