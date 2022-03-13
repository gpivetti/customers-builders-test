package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.domain.customer.DeleteCustomerService;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/customers")
@Tag(name = "orders", description = "Endpoints for orders operations")
public class DeleteCustomerController {
    private final DeleteCustomerService deleteCustomerService;

    @Autowired
    public DeleteCustomerController(final DeleteCustomerService deleteCustomerService) {
        this.deleteCustomerService = deleteCustomerService;
    }

    @DeleteMapping("{customerId}")
    @Operation(summary = "Removing Customer by Id")
    public ResponseEntity<?> deleteCustomers(@PathVariable String customerId) {
        try {
            this.deleteCustomerService.deleteCustomer(customerId);
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ApiResponseNotFoundDTO.of("Customer"), HttpStatus.NOT_FOUND);
        } catch (AppErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }
}