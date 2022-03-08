package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.customer.mappers.CustomerDtoMapper;
import br.com.builders.customer.application.customer.services.FindCustomerService;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.log.LogService;
import br.com.builders.customer.main.exceptions.AppErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/customer")
public class GetCustomerController {
    private final FindCustomerService findCustomerService;
    private final LogService logService;

    @Autowired
    public GetCustomerController(final FindCustomerService findCustomerService,
                                 final LogService logService) {
        this.findCustomerService = findCustomerService;
        this.logService = logService;
    }

    @GetMapping("")
    public List<CustomerDto> getAll() {
        try {
            List<Customer> customers = this.findCustomerService.findCustomers();
            return this.checkCustomers(customers)
                    ? customers.stream()
                        .map(CustomerDtoMapper::toCustomerDto)
                        .collect(Collectors.toList())
                    : new ArrayList<>();
        } catch (Exception ex) {
            this.logService.sendLogError("GetAllCustomersError", ex.getMessage());
            throw new AppErrorException(ex);
        }
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }
}
