package br.com.builders.customer.infra.mongo.entities;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CustomerEntityListeners extends AbstractMongoEventListener<CustomerEntity> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<CustomerEntity> customerEntityEvent) {
        if (customerEntityEvent.getSource().getId() == null) {
            customerEntityEvent.getSource().setCreatedAt(new Date());
        }
        customerEntityEvent.getSource().setUpdatedAt(new Date());
    }
}
