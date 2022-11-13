package com.amigoscode.testing.customer;

import com.amigoscode.testing.utils.PhoneNumberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerRegistrationService {

    private final CustomerRepository customerRepository;
    private PhoneNumberValidator phoneNumberValidator;

    @Autowired
    public CustomerRegistrationService(CustomerRepository customerRepository,PhoneNumberValidator phoneNumberValidator) {
        this.customerRepository = customerRepository;
        this.phoneNumberValidator = phoneNumberValidator;
    }

    public void registerNewCustomer(CustomerRegistrationRequest request) throws IllegalStateException {
        String phoneNumber = request.getCustomer().getPhoneNumber();
        checkNumberValidity(phoneNumber);

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

    void checkNumberValidity(String phoneNumber){
        if (!phoneNumberValidator.test(phoneNumber)){
            throw new IllegalStateException(String.format("Phone number: %s is not valid",phoneNumber));
        }
    }
}
