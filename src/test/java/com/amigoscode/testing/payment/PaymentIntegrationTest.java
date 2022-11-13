package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRegistrationRequest;
import com.amigoscode.testing.payment.Currency;
import com.amigoscode.testing.payment.Payment;
import com.amigoscode.testing.payment.PaymentRepository;
import com.amigoscode.testing.payment.PaymentRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void itShouldCreatePaymentSuccessfully() throws Exception {
        //Given
        UUID customerId = UUID.randomUUID();
        Long paymentId = 1L;
        Customer customer = new Customer(customerId,"Ahmed","0505558844");
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest(customer);
        ResultActions customerReqResult = mockMvc.perform(put("/api/v1/customer-registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(registrationRequest)))
        );

        //When
        Payment payment = new Payment(paymentId,
                customerId, new BigDecimal("10.00"), Currency.SAR,"Mada","Donation");
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        ResultActions paymentReqResult = mockMvc.perform(post("/api/v1/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectToJson(paymentRequest))));

        //Then both customer registration and payment requests are 200 status code
        customerReqResult.andExpect(status().isOk());
        paymentReqResult.andExpect(status().isOk());


        //Then
        MvcResult mvcResult = mockMvc.perform(get("/api/v1/payment/" + paymentId))
                .andExpect(status().isOk())
                .andReturn();
        String actualJson = mvcResult.getResponse().getContentAsString();
        String expected = objectToJson(payment);
        assertThat(actualJson).isEqualTo(expected);
    }


    private String objectToJson(Object o)  {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            fail("Cant convert  to Json"+ e.getMessage());
            return null;
        }

    }


}
