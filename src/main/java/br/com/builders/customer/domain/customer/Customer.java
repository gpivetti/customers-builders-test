package br.com.builders.customer.domain.customer;

import lombok.Data;

import java.util.Date;

@Data
public class Customer {
    private String id;
    private String name;
    private String document;
    private Date birthdate;
    private int age;

    public void calculateAge() {
        this.age = 0;
    }
}
