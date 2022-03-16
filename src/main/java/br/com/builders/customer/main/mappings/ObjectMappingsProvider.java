package br.com.builders.customer.main.mappings;

import br.com.builders.customer.main.mappings.providers.DatabaseMappingsProviderFactory;

import java.util.List;

public class ObjectMappingsProvider {
    public static List<ObjectMapping> getMappings() {
        return DatabaseMappingsProviderFactory.get().getDatabaseMappings();
    }
}
