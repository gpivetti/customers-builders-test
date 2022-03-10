package br.com.builders.customer.domain.customer;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private String id;
    private String name;
    private String document;
    private Date birthdate;

    public int getAge() {
        return 0;
    }
}
