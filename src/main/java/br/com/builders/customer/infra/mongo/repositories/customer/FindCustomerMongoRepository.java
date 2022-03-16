package br.com.builders.customer.infra.mongo.repositories.customer;

import br.com.builders.customer.commons.dto.FieldsDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.repository.FindCustomerRepository;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import br.com.builders.customer.infra.mongo.repositories.customer.mappers.CustomerFilterEntityMapper;
import br.com.builders.customer.infra.mongo.helper.MongoQueryProcessorHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FindCustomerMongoRepository implements FindCustomerRepository {
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public FindCustomerMongoRepository(final ModelMapper modelMapper,
                                       final MongoTemplate mongoTemplate) {
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Customer> findAll(PageDataDTO pages) {
        return this.findAll(null, pages);
    }

    @Override
    public <T> List<Customer> findAll(FieldsDataDTO<T> customerFields, PageDataDTO pages) {
        Query query = this.getQueryForFindCustomers(customerFields, pages);
        List<CustomerEntity> customers = this.mongoTemplate.find(query, CustomerEntity.class);
        return customers.stream().map(this::mapToModel).collect(Collectors.toList());
    }

    @Override
    public Customer findById(String customerId) {
        CustomerEntity customer = this.mongoTemplate.findById(customerId, CustomerEntity.class);
        return customer != null ? this.mapToModel(customer) : null;
    }

    @Override
    public Customer findByDocument(String document) {
        CustomerEntity customer = this.mongoTemplate
                .findOne(Query.query(Criteria.where("document").is(document)), CustomerEntity.class);
        return customer != null ? this.mapToModel(customer) : null;
    }

    private <T> Query getQueryForFindCustomers(FieldsDataDTO<T> customerFields, PageDataDTO pages) {
        MongoQueryProcessorHelper queryProcessor = new MongoQueryProcessorHelper();
        this.setQueryWithFilters(queryProcessor, customerFields);
        this.setQueryWithSorting(queryProcessor, customerFields);
        this.setQueryWithPages(queryProcessor, pages);
        return queryProcessor.getQueries();
    }

    private <T> void setQueryWithFilters(MongoQueryProcessorHelper queryProcessor, FieldsDataDTO<T> customerFilter) {
        if (customerFilter != null && customerFilter.getFieldsClass() != null) {
            Map<String, String> mapFields = CustomerFilterEntityMapper.mapFieldNames(customerFilter.getFieldsClass());
            queryProcessor.setQueryByFilters(customerFilter.getFilters(), mapFields);
        }
    }

    private <T> void setQueryWithSorting(MongoQueryProcessorHelper queryProcessor, FieldsDataDTO<T> customerFilter) {
        if (customerFilter != null && customerFilter.getFieldsClass() != null) {
            Map<String, String> mapFields = CustomerFilterEntityMapper.mapFieldNames(customerFilter.getFieldsClass());
            queryProcessor.setQueryBySorting(customerFilter.getSorting(), mapFields);
        }
    }

    private void setQueryWithPages(MongoQueryProcessorHelper queryProcessor, PageDataDTO pages) {
        queryProcessor.setQueryByPages(pages);
    }

    private Customer mapToModel(CustomerEntity customer) {
        return this.modelMapper.map(customer, Customer.class);
    }
}