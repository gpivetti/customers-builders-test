package br.com.builders.customer.infra.mongo.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "customers")
public class CustomerEntity {
    @Id
    private String id;
    private String name;
    private String document;
    private Date birthdate;
    private Date createdAt;
    private Date updatedAt;
}
