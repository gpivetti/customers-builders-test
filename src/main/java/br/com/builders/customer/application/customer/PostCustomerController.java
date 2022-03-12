package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.customer.dto.InsertUpdateCustomerDto;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.main.exceptions.AppErrorException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1/customers")
@Tag(name = "orders", description = "Endpoints for orders operations")
public class PostCustomerController {
    private final SaveCustomerService saveCustomerService;

    @Autowired
    public PostCustomerController(final SaveCustomerService saveCustomerService) {
        this.saveCustomerService = saveCustomerService;
    }

    @PostMapping(value = "",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Inserting new Customer")
    public ResponseEntity<?> postCustomers(@Valid @RequestBody InsertUpdateCustomerDto customerDto) {
        try {
            Customer customer = this.saveCustomerService.insert(CustomerMapper.toSaveCustomerDto(customerDto));
            this.validateInsertedCustomer(customer);
            return new ResponseEntity<>(CustomerDto.fromCustomer(customer), HttpStatus.OK);
        } catch (AppErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    private void validateInsertedCustomer(Customer customer) throws AppErrorException {
        if (customer == null) {
            throw new AppErrorException("Error on Save Customer");
        }
    }
}
