package br.com.builders.customer.application;

import br.com.builders.customer.application.dto.CustomerDto;
import br.com.builders.customer.application.usecases.FindCustomerUseCase;
import br.com.builders.customer.domain.log.LogService;
import br.com.builders.customer.main.exceptions.AppErrorException;
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
    private final LogService logService;

    @Autowired
    public CustomerController(final FindCustomerUseCase findCustomerUseCase,
                              final LogService logService) {
        this.findCustomerUseCase = findCustomerUseCase;
        this.logService = logService;
    }

    @GetMapping("/")
    public List<CustomerDto> getAll() {
        try {
            List<CustomerDto> customers = this.findCustomerUseCase.findCustomers();
            return this.checkCustomers(customers)
                    ? customers
                    : new ArrayList<>();
        } catch (Exception ex) {
            this.logService.sendLogError("GetAllCustomersError", ex.getMessage());
            throw new AppErrorException(ex);
        }
    }

    private boolean checkCustomers(List<CustomerDto> customers) {
        return customers != null && !customers.isEmpty();
    }
}
