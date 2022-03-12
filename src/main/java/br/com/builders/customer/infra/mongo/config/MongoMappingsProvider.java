package br.com.builders.customer.infra.mongo.config;

import br.com.builders.customer.infra.mongo.entities.CustomerEntityMapper;
import br.com.builders.customer.main.mappings.ObjectMapping;
import br.com.builders.customer.main.mappings.providers.DatabaseMappingsProvider;

import java.util.List;

public class MongoMappingsProvider implements DatabaseMappingsProvider {
    @Override
    public List<ObjectMapping> getDatabaseMappings() {
        return List.of(new CustomerEntityMapper());
    }
}
