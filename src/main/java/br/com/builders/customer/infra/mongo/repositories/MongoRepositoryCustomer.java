package br.com.builders.customer.infra.mongo.repositories;

import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MongoRepositoryCustomer extends MongoRepository<CustomerEntity, String> {
    @Query(sort = "{'created_at': 1}")
    List<CustomerEntity> findByDocument(String document, PageRequest pageable);
}
