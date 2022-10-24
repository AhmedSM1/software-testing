package com.amigoscode.testing.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) throws IllegalStateException {
        String phoneNumber = request.getCustomer().getPhoneNumber();
        Optional<Customer> customerByPhoneNumber = customerRepository
                .selectCustomerByPhoneNumber(phoneNumber);
        if (customerByPhoneNumber.isPresent()){
            if (customerByPhoneNumber.get().getName().equals(request.getCustomer().getName())){
                return;
            }
            throw new IllegalStateException(String.format("Phone number: %s is taken",phoneNumber));
        }

        if (request.getCustomer().getId() == null){
            request.getCustomer().setId(UUID.randomUUID());
        }
        customerRepository.save(request.getCustomer());
    }




}
