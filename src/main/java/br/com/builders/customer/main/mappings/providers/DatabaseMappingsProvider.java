package br.com.builders.customer.main.mappings.providers;

import br.com.builders.customer.main.mappings.ObjectMapping;

import java.util.List;

public interface DatabaseMappingsProvider {
    List<ObjectMapping> getDatabaseMappings();
}
