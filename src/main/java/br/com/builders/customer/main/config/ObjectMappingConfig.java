package br.com.builders.customer.main.config;

import br.com.builders.customer.infra.mongo.entities.CustomerEntityMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ObjectMappingConfig {

    private final ModelMapper modelMapper;

    public ObjectMappingConfig() {
        this.modelMapper = new ModelMapper();
        this.getMappings().forEach(mapping -> mapping.addMappings(this.modelMapper));
    }

    @Bean
    public ModelMapper modelMapper() {
        return this.modelMapper;
    }

    private List<ObjectMapping> getMappings() {
        return List.of(new CustomerEntityMapper());
    }
}