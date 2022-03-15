package br.com.builders.customer.application.customer;

import br.com.builders.customer.application.customer.dto.CustomerDto;
import br.com.builders.customer.application.dto.GenericPaginatedResponseDTO;
import br.com.builders.customer.application.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.commons.dto.FiltersDataDTO;
import br.com.builders.customer.commons.dto.FiltersDataFieldsDTO;
import br.com.builders.customer.commons.dto.PageFiltersDataDTO;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.dto.FiltersCustomerDto;
import br.com.builders.customer.main.docs.FilterProcessorInfo;
import br.com.builders.customer.main.exceptions.AppErrorException;
import br.com.builders.customer.main.exceptions.InvalidConstraintException;
import br.com.builders.customer.main.exceptions.InvalidParameterException;
import br.com.builders.customer.main.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/customers")
@Tag(name = "orders", description = "Endpoints for orders operations")
public class GetCustomerController {
    private final FindCustomerService findCustomerService;

    @Autowired
    public GetCustomerController(final FindCustomerService findCustomerService) {
        this.findCustomerService = findCustomerService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Getting Customers")
    public GenericPaginatedResponseDTO<List<CustomerDto>> getCustomers(
            @ParameterObject Pageable pageable,
            @Parameter(description = FilterProcessorInfo.DESCRIPTION, example = FilterProcessorInfo.EXAMPLE)
                @RequestParam(required = false) List<String> filter,
            HttpServletRequest http) {
        try {
            List<Customer> customers = this.findCustomers(FiltersDataFieldsDTO.fromStringFilters(filter), pageable);
            List<CustomerDto> customersDto = this.mappingCustomers(customers);
            List<String> queryParameters = this.normalizeGetCustomersParameters(filter);
            return new GenericPaginatedResponseDTO<>(customersDto, http.getRequestURI(), queryParameters, pageable);
        } catch (InvalidParameterException | AppErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    @GetMapping(value = "{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Getting Customer by id")
    public ResponseEntity<?> getCustomerById(@PathVariable String customerId) {
        try {
            Customer customer = this.findCustomerService.findCustomerById(customerId);
            this.validateCustomer(customer);
            return ResponseEntity.ok(CustomerDto.fromCustomer(customer));
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ApiResponseNotFoundDTO.of("Customer"), HttpStatus.NOT_FOUND);
        } catch (AppErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    private List<Customer> findCustomers(List<FiltersDataFieldsDTO> filterFields, Pageable pageable) {
        PageFiltersDataDTO pageFilters = new PageFiltersDataDTO(pageable.getPageNumber(), pageable.getPageSize());
        return filterFields.isEmpty()
                ? this.findCustomerService.findCustomers(pageFilters)
                : this.findCustomerService.findCustomers(FiltersDataDTO.of(filterFields, FiltersCustomerDto.class),
                pageFilters);
    }

    private List<CustomerDto> mappingCustomers(List<Customer> customers) {
        return this.checkCustomers(customers)
                ? customers.stream()
                .map(CustomerDto::fromCustomer)
                .collect(Collectors.toList())
                : new ArrayList<>();
    }

    private List<String> normalizeGetCustomersParameters(List<String> filters) {
        return filters != null && !filters.isEmpty()
                ? filters.stream()
                    .map(filter -> "filter="+filter)
                    .collect(Collectors.toList())
                : null;
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }

    private void validateCustomer(Customer customer) throws ResourceNotFoundException {
        if (customer == null)
            throw new ResourceNotFoundException();
    }
}
