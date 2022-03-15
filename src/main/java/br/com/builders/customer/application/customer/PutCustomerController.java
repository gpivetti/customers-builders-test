package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.customer.dto.InsertUpdateCustomerDto;
import br.com.builders.customer.application.customer.mapper.CustomerMapper;
import br.com.builders.customer.application.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.SaveCustomerService;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("v1/customers")
@Tag(name = "orders", description = "Endpoints for orders operations")
public class PutCustomerController {
    private final SaveCustomerService saveCustomerService;

    @Autowired
    public PutCustomerController(final SaveCustomerService saveCustomerService) {
        this.saveCustomerService = saveCustomerService;
    }

    @PutMapping(value = "{customerId}",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updating Customer by Id")
    public ResponseEntity<?> putCustomer(@NotNull @PathVariable String customerId,
                                         @RequestBody InsertUpdateCustomerDto customerDto) {
        try {
            this.validateRequest(customerDto);
            Customer customer = this.saveCustomerService.update(customerId,
                    CustomerMapper.toSaveCustomerDto(customerDto));
            this.validateInsertedCustomer(customer);
            return new ResponseEntity<>(CustomerDto.fromCustomer(customer), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ApiResponseNotFoundDTO.of("Customer"), HttpStatus.NOT_FOUND);
        } catch (AppErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    private void validateRequest(InsertUpdateCustomerDto customerDto) throws AppErrorException {
        if (customerDto == null) {
            throw new AppErrorException("Error on Update Customer [Null Request]");
        }
    }

    private void validateInsertedCustomer(Customer customer) throws AppErrorException {
        if (customer == null) {
            throw new AppErrorException("Error on Update Customer [Null Response]");
        }
    }
}
