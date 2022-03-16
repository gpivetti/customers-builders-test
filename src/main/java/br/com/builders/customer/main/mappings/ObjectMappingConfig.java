package br.com.builders.customer.main.mappings;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ObjectMappingConfig {

    private final ModelMapper modelMapper;

    public ObjectMappingConfig() {
        this.modelMapper = new ModelMapper();
        List<ObjectMapping> mappings = ObjectMappingsProvider.getMappings();
        if (mappings != null && !mappings.isEmpty()) {
            mappings.forEach(mapping -> mapping.addMappings(this.modelMapper));
        }
    }

    @Bean
    public ModelMapper modelMapper() {
        return this.modelMapper;
    }
}