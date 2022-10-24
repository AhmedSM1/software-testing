package com.amigoscode.testing.customer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(
        properties = {
                "spring.jpa.properties.javax.persistence.validation.mode=none"
        }
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository underTest;

    @Test
    void itShouldSelectCustomerByPhoneNumber() {
        //Given
        String phoneNumber = "0505558844";
        Customer mock = new Customer(UUID.randomUUID(),"Ahmed", phoneNumber);
        //When
        underTest.save(mock);
        //Then
        Optional<Customer> actual = underTest.selectCustomerByPhoneNumber(phoneNumber);
        assertThat(actual)
                .isPresent()
                .hasValueSatisfying(c ->
                        assertThat(c).isEqualToComparingFieldByField(mock)
                );
    }


    @Test
    void itShouldSaveNewCustomer() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id,"Ahmed","0505558844");

        //When
        underTest.save(customer);
        //Then
        Optional<Customer> optionalCustomer = underTest.findById(id);
        assertThat(optionalCustomer)
                .isPresent()
                .hasValueSatisfying(c -> assertThat(c).isEqualToComparingFieldByField(customer));

    }

    @Test
    void itShouldNotSaveNewCustomerWhenNameIsNull() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id,null,"0505558844");
        //When
        //Then
        assertThatThrownBy(() -> underTest.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.name");

    }

    @Test
    void itShouldNotSaveNewCustomerWhenPhoneNumberIsNull() {
        //Given
        UUID id = UUID.randomUUID();
        Customer customer = new Customer(id,"Ahmed",null);
        //When
        //Then
        assertThatThrownBy(() -> underTest.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("not-null property references a null or transient value : com.amigoscode.testing.customer.Customer.phoneNumber");

    }
}
