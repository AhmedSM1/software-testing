package com.amigoscode.testing.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PhoneNumberValidatorTest {

    @Autowired
    private PhoneNumberValidator underTest;


    @BeforeEach
    void setUp() {
        underTest = new PhoneNumberValidator();
    }

    @Test
    void itShouldValidatePhoneNumber(){
        //Given
        String phoneNumber = "0505558844";
        //When
        boolean isValid = underTest.test(phoneNumber);
        //Then
        assertThat(isValid).isTrue();
    }


    @Test
    void itShouldThrowWithInvalidPhoneNumber(){
        //Given
        String phoneNumber = "1111111";
        //When
        //Then
        assertThatThrownBy(() -> underTest.test(phoneNumber))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("phone number: %s is invalid",
                        phoneNumber));
    }
}
