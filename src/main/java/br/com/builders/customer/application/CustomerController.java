package br.com.builders.customer.application;

import br.com.builders.customer.application.dto.CustomerDto;
import br.com.builders.customer.application.usecases.FindCustomerUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/customer")
public class CustomerController {

    private final FindCustomerUseCase findCustomerUseCase;

    @Autowired
    public CustomerController(final FindCustomerUseCase findCustomerUseCase) {
        this.findCustomerUseCase = findCustomerUseCase;
    }

    @GetMapping("/")
    public List<CustomerDto> getAll() {
        List<CustomerDto> customers = this.findCustomerUseCase.findCustomers();
        return this.checkCustomers(customers)
                ? customers
                : new ArrayList<>();
    }

    private boolean checkCustomers(List<CustomerDto> customers) {
        return customers != null && !customers.isEmpty();
    }
}