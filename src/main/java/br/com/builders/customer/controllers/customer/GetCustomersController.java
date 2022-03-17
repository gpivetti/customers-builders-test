package br.com.builders.customer.controllers.customer;

import br.com.builders.customer.controllers.customer.dto.CustomerDTO;
import br.com.builders.customer.controllers.dto.GenericPaginatedResponseDTO;
import br.com.builders.customer.controllers.dto.ApiResponseNotFoundDTO;
import br.com.builders.customer.controllers.helper.FiltersHelper;
import br.com.builders.customer.commons.dto.FieldsDataDTO;
import br.com.builders.customer.commons.dto.PageDataDTO;
import br.com.builders.customer.domain.customer.FindCustomerService;
import br.com.builders.customer.domain.customer.Customer;
import br.com.builders.customer.domain.customer.adapters.CustomerCacheAdapter;
import br.com.builders.customer.domain.customer.dto.FiltersCustomerDto;
import br.com.builders.customer.main.docs.FilterProcessorInfo;
import br.com.builders.customer.main.exceptions.AppErrorException;
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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/customers")
@Tag(name = "orders", description = "Endpoints for orders operations")
public class GetCustomersController {
    private final FindCustomerService findCustomerService;
    private final CustomerCacheAdapter customerCacheAdapter;

    @Autowired
    public GetCustomersController(final FindCustomerService findCustomerService,
                                  final CustomerCacheAdapter customerCacheAdapter) {
        this.findCustomerService = findCustomerService;
        this.customerCacheAdapter = customerCacheAdapter;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Getting Customers")
    public GenericPaginatedResponseDTO<List<CustomerDTO>> getCustomers(
            @ParameterObject Pageable pageable,
            @Parameter(description = FilterProcessorInfo.DESCRIPTION, example = FilterProcessorInfo.EXAMPLE)
            @RequestParam(required = false) List<String> filter,
            HttpServletRequest http) {
        try {
            List<CustomerDTO> customersDto = this.mappingCustomers(this.findCustomers(filter, pageable));
            List<String> queryParameters = FiltersHelper.buildQueryParametersFromStringFilter(filter);
            return new GenericPaginatedResponseDTO<>(customersDto, http.getRequestURI(), queryParameters, pageable);
        } catch (InvalidParameterException | AppErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    @GetMapping(value = "{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Getting Customer by id")
    public ResponseEntity<?> getCustomerById(@NotNull @PathVariable String customerId) {
        try {
            CustomerDTO customerDTO = this.findCustomer(customerId);
            this.validateCustomer(customerId, customerDTO);
            return new ResponseEntity<>(this.findCustomer(customerId), HttpStatus.OK);
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity<>(ApiResponseNotFoundDTO.of("Customer"), HttpStatus.NOT_FOUND);
        } catch (AppErrorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AppErrorException(ex);
        }
    }

    private List<Customer> findCustomers(List<String> filter, Pageable pageable) {
        return filter == null && pageable.getSort().isEmpty()
                ? this.findCustomerService.findCustomers(PageDataDTO.fromPageable(pageable))
                : this.findCustomerService.findCustomers(
                        FieldsDataDTO.fromClass(
                                FiltersHelper.buildFilterList(filter), pageable.getSort(), FiltersCustomerDto.class),
                        PageDataDTO.fromPageable(pageable));
    }

    private CustomerDTO findCustomer(String customerId) {
        CustomerDTO customerDTO = this.findCustomerFromCache(customerId);
        if (customerDTO != null) return customerDTO;
        customerDTO = this.findCustomerFromService(customerId);
        if (customerDTO != null) this.saveCustomerOnCache(customerDTO);
        return customerDTO;
    }

    private CustomerDTO findCustomerFromCache(String customerId) {
        return this.customerCacheAdapter.findById(customerId);
    }

    private CustomerDTO findCustomerFromService(String customerId) {
        Customer customer = this.findCustomerService.findCustomerById(customerId);
        return customer != null
                ? CustomerDTO.fromCustomer(customer)
                : null;
    }

    private void saveCustomerOnCache(CustomerDTO customerDTO) {
        this.customerCacheAdapter.save(customerDTO);
    }

    private List<CustomerDTO> mappingCustomers(List<Customer> customers) {
        return this.checkCustomers(customers)
                ? customers.stream().map(CustomerDTO::fromCustomer).collect(Collectors.toList())
                : new ArrayList<>();
    }

    private boolean checkCustomers(List<Customer> customers) {
        return customers != null && !customers.isEmpty();
    }

    private void validateCustomer(String customerId, CustomerDTO customer) throws ResourceNotFoundException {
        if (customer == null)
            throw new ResourceNotFoundException("Customer", this.normalizeNotFoundCustomerExceptionFilters(customerId));
    }

    private Map<String, String> normalizeNotFoundCustomerExceptionFilters(String customerId) {
        return new HashMap<>(){{ put("customerId", customerId); }};
    }
}
