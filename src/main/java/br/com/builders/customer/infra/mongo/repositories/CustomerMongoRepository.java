package br.com.builders.customer.infra.mongo.repositories;

import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerMongoRepository extends MongoRepository<CustomerEntity, String> {
}
