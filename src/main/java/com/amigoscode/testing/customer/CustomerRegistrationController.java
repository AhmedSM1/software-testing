package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/customer-registration")
public class CustomerRegistrationController {

    @Autowired
    private CustomerRegistrationService service;

    @PutMapping
    public void registerCustomer(
          @Valid @RequestBody  CustomerRegistrationRequest request){
        service.registerNewCustomer(request);
    }

}
