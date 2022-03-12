package br.com.builders.customer.main.mappings.providers;

import br.com.builders.customer.infra.mongo.config.MongoMappingsProvider;

public class DatabaseMappingsProviderFactory {
    public static DatabaseMappingsProvider get() {
        return new MongoMappingsProvider();
    }
}
