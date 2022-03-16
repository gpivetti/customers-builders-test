package br.com.builders.customer.feature.setup;

import br.com.builders.customer.controllers.customer.dto.InsertCustomerDto;
import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

public class CustomerMockHelper {
    public static InsertCustomerDto getCustomerForUpdate() {
        return InsertCustomerDto.builder()
                .name("TestCustomer2")
                .build();
    }

    public static InsertCustomerDto getCustomerForInsert() {
        return InsertCustomerDto.builder()
                .name("TestCustomer")
                .document("123123")
                .birthdate(LocalDate.parse("1988-11-06"))
                .build();
    }

    public static List<CustomerEntity> getCustomers() {
        LocalDate localDateMinusYear = LocalDate.now();
        localDateMinusYear = localDateMinusYear.minusYears(10);
        return List.of(
                CustomerEntity.builder()
                        .id(new ObjectId())
                        .name("Customer1")
                        .document("100100")
                        .birthdate(localDateMinusYear)
                        .build(),
                CustomerEntity.builder()
                        .id(new ObjectId())
                        .name("Customer2")
                        .document("100200")
                        .birthdate(LocalDate.now())
                        .build(),
                CustomerEntity.builder()
                        .id(new ObjectId())
                        .name("Customer3")
                        .document("100300")
                        .birthdate(LocalDate.now())
                        .build()
        );
    }
}
