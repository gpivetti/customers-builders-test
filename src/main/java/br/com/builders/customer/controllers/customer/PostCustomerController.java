package br.com.builders.customer.controllers.customer;

import br.com.builders.customer.controllers.customer.dto.CustomerDTO;
import br.com.builders.customer.controllers.customer.dto.InsertCustomerDto;
import br.com.builders.customer.controllers.customer.mapper.CustomerMapper;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;
import br.com.builders.customer.main.exceptions.ObjectValidationException;
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
    public ResponseEntity<?> postCustomer(@Valid @RequestBody InsertCustomerDto customerDto) {
        try {
            Customer customer = this.insertCustomer(customerDto);
            this.validateInsertedCustomer(customer);
            return new ResponseEntity<>(CustomerDTO.fromCustomer(customer), HttpStatus.OK);
        } catch (InvalidConstraintException | ObjectValidationException | AppErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    private Customer insertCustomer(InsertCustomerDto insertCustomer) {
        return this.saveCustomerService.insert(CustomerMapper.toSaveCustomerDto(insertCustomer));
    }

    private void validateInsertedCustomer(Customer customer) throws AppErrorException {
        if (customer == null) {
            throw new AppErrorException("Error on Insert Customer [Null Response]");
        }
    }
}
