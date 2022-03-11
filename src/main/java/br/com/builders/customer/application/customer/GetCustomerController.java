package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.commons.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/customers")
public class GetCustomerController {
    private final FindCustomerService findCustomerService;

    @Autowired
    public GetCustomerController(final FindCustomerService findCustomerService) {
        this.findCustomerService = findCustomerService;
    }

    @GetMapping("")
    public List<CustomerDto> getCustomer() {
        try {
            List<Customer> customers = this.findCustomerService.findCustomers();
            return this.checkCustomers(customers)
                    ? customers.stream()
                        .map(CustomerDto::fromCustomer)
                        .collect(Collectors.toList())
                    : new ArrayList<>();
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    @GetMapping("{customerId}")
    public ResponseEntity<?> getCustomersById(@PathVariable String customerId) {
        try {
            Customer customer = this.findCustomerService.findCustomerById(customerId);
            this.validateCustomer(customer);
            return ResponseEntity.ok(CustomerDto.fromCustomer(customer));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ApiResponseNotFoundDTO.of("Customer"), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }

    private void validateCustomer(Customer customer) throws ResourceNotFoundException {
        if (customer == null)
            throw new ResourceNotFoundException();
    }
}
