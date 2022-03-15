package br.com.builders.customer.domain.customer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(value = "default")
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DisplayName("[UT] On Handle DefaultCustomerBusinessValidatorTests")
public class CustomerTests {

    @Test
    @DisplayName("On Customer: Should calculate correct age of customer when year between birthdate is greater than 1")
    public void shouldCalculateCorrectAgeOfCostumerWhenYearBetweenBirthdateIsGreaterThanOne() {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusYears(1);

        Customer customer = new Customer.Builder().birthdate(localDate).build();
        assertEquals(customer.getAge(), 1);
    }

    @Test
    @DisplayName("On Customer: Should return zero years old of costumer when year between birthdate is less than 1")
    public void shouldReturnZeroYearsOldOfCostumerWhenYearBetweenBirthdateIsLessThanOne() {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.minusMonths(1);

        Customer customer = new Customer.Builder().birthdate(localDate).build();
        assertEquals(customer.getAge(), 0);
    }

    @Test
    @DisplayName("On Customer: Should return zero years old of costumer when birthdate is greater than current date")
    public void shouldReturnZeroYearsOldOfCostumerWhenBirthdateIsGreaterThanCurrentDate() {
        LocalDate localDate = LocalDate.now();
        localDate = localDate.plusDays(1);

        Customer customer = new Customer.Builder().birthdate(localDate).build();
        assertEquals(customer.getAge(), 0);
    }
}
