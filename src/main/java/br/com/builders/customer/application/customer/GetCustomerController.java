package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.customer.helpers.FindCustomerParamsHelper;
import br.com.builders.customer.commons.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.dto.FindCustomersParamsDTO;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;
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

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CustomerDto> getCustomers(@RequestParam(required = false) String limit,
                                          @RequestParam(required = false) String offset,
                                          @RequestParam(required = false) String filter,
                                          @RequestParam(required = false) String sort) {
        FindCustomersParamsDTO findParams = FindCustomerParamsHelper.generateParams(filter, sort, limit, offset);
        try {
            List<Customer> customers = this.findCustomers(findParams);
            return this.checkCustomers(customers)
                    ? customers.stream()
                        .map(CustomerDto::fromCustomer)
                        .collect(Collectors.toList())
                    : new ArrayList<>();
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    @GetMapping(value = "{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCustomerById(@PathVariable String customerId) {
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

    private List<Customer> findCustomers(FindCustomersParamsDTO findCustomersParams) {
        return findCustomersParams != null
                ? this.findCustomerService.findCustomers(findCustomersParams)
                : this.findCustomerService.findCustomers();
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }

    private void validateCustomer(Customer customer) throws ResourceNotFoundException {
        if (customer == null)
            throw new ResourceNotFoundException();
    }
}
