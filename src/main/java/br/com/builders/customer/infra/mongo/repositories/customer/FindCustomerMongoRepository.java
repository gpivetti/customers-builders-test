package br.com.builders.customer.infra.mongo.repositories.customer;

import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.PageFiltersDataDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.dto.FiltersCustomerDto;
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
    public List<Customer> findAll(PageFiltersDataDTO pageFilters) {
        return this.findAll(null, pageFilters);
    }

    @Override
    public List<Customer> findAll(FiltersDataDTO<FiltersCustomerDto> customerFilters,
                                  PageFiltersDataDTO pageFilters) {
        MongoQueryProcessorHelper queryProcessor = new MongoQueryProcessorHelper();
        this.setQueryWithFilters(queryProcessor, customerFilters);
        this.setQueryWithPages(queryProcessor, pageFilters);
        List<CustomerEntity> customers = this.mongoTemplate.find(queryProcessor.getQueries(), CustomerEntity.class);
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

    private void setQueryWithFilters(MongoQueryProcessorHelper queryProcessor,
                                     FiltersDataDTO<FiltersCustomerDto> customerFilter) {
        if (customerFilter != null && customerFilter.getFilterClass() != null) {
            Map<String, String> mapFields = CustomerFilterEntityMapper.mapFieldNames(customerFilter.getFilterClass());
            queryProcessor.setQueryByFilters(customerFilter.getFields(), mapFields);
        }
    }

    private void setQueryWithPages(MongoQueryProcessorHelper queryProcessor, PageFiltersDataDTO pageFilters) {
        queryProcessor.setQueryByPages(pageFilters);
    }

    private Customer mapToModel(CustomerEntity customer) {
        return this.modelMapper.map(customer, Customer.class);
    }
}