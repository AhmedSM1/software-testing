package com.amigoscode.testing.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class PhoneNumberValidatorTest {

    @Autowired
    private PhoneNumberValidator underTest;


    @BeforeEach
    void setUp() {
        underTest = new PhoneNumberValidator();
    }

    @ParameterizedTest
    @CsvSource({"0505558844,true",
            "+966505558844,true",
            "1,false",
            "12,false",
            "234,false",
            "234,false",
    })
    void itShouldValidatePhoneNumber(String phoneNumber, boolean expected ){
        //When
        boolean isValid = underTest.test(phoneNumber);
        //Then
        assertThat(isValid).isEqualTo(expected);
    }




}
