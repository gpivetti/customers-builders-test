package br.com.builders.customer.infra.mongo.repositories.customer;

import br.com.builders.customer.domain.customer.repository.DeleteCustomerRepository;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import br.com.builders.customer.main.exceptions.AppErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class DeleteCustomerMongoRepository implements DeleteCustomerRepository {
    private final MongoTemplate mongoTemplate;

    @Autowired
    public DeleteCustomerMongoRepository(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void delete(String customerId) {
        this.validateCustomerId(customerId);
        this.mongoTemplate.findAndRemove(Query.query(Criteria.where("id").is(customerId)), CustomerEntity.class);
    }

    private void validateCustomerId(String customerId) {
        if (StringUtils.isEmpty(customerId)) {
            throw new AppErrorException("Invalid CustomerId on SaveCustomerRepository");
        }
    }
}