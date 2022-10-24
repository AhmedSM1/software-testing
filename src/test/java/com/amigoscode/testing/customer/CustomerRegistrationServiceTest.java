package com.amigoscode.testing.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class CustomerRegistrationServiceTest {

    private CustomerRegistrationService underTest;
    @Mock
    private CustomerRepository customerRepository;

    @Captor
    private ArgumentCaptor<Customer> customerArgumentCaptor;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new CustomerRegistrationService(customerRepository);
    }

    @Test
    void itShouldRegisterNewCustomer(){
        //Given
        Customer mockCustomer = new Customer(UUID.randomUUID(),"Ahmed","0505558844");
        when(customerRepository.selectCustomerByPhoneNumber("0505558844"))
                .thenReturn(Optional.empty());
        //When
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(mockCustomer);
        underTest.registerNewCustomer(request);
        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue).isEqualToComparingFieldByField(mockCustomer);
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberExists() {
        String phoneNumber = "0505558844";
        Customer mockCustomer = new Customer(UUID.randomUUID(),"Ahmed", phoneNumber);
        when(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .thenReturn(Optional.of(mockCustomer));
        underTest.registerNewCustomer(new CustomerRegistrationRequest(mockCustomer));
        //make sure its Not exucted
        then(customerRepository).should(never()).save(any());
    }

    @Test
    void itShouldThrowWhenPhoneNumberExists() {
        //Given
        String phoneNumber = "0505558844";
        Customer customer = new Customer(UUID.randomUUID(),"Ahmed", phoneNumber);
        Customer customerTwo = new Customer(UUID.randomUUID(),"Ali", phoneNumber);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(customer);
        //When
        when(customerRepository.selectCustomerByPhoneNumber(phoneNumber))
                .thenReturn(Optional.of(customerTwo));
        //Then
        assertThatThrownBy(() -> underTest.registerNewCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(String.format("Phone number: %s is taken",phoneNumber));

        then(customerRepository).should(never()).save(any());
    }


    @Test
    void itShouldSaveCustomerWhenIdIsNull() {
        //Given
        Customer mockCustomer = new Customer(null,"Ahmed","0505558844");
        when(customerRepository.selectCustomerByPhoneNumber("0505558844"))
                .thenReturn(Optional.empty());
        //When
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(mockCustomer);
        underTest.registerNewCustomer(request);
        //Then
        then(customerRepository).should().save(customerArgumentCaptor.capture());
        Customer customerArgumentCaptorValue = customerArgumentCaptor.getValue();
        assertThat(customerArgumentCaptorValue)
                .isEqualToComparingFieldByField(mockCustomer);
    }
}
