package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CardPaymentCharger cardPaymentCharger;
    @Autowired
    private PaymentService underTest;

    @Captor
    private ArgumentCaptor<Payment> paymentArgumentCaptor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new PaymentService(paymentRepository,customerRepository,cardPaymentCharger);
    }

    @Test
    void itShouldSaveNewPayment() {
        //Given
        UUID customerId = UUID.randomUUID();
        Payment payment = new Payment(null,
                customerId,
                new BigDecimal("10.00"),
                Currency.USD,
                "visa",
                "donation");
        when(customerRepository.findById(any()))
                .thenReturn(Optional.of(mock(Customer.class))
                );
        when(cardPaymentCharger.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()
        )).thenReturn(new CardPaymentCharge(true));
        //When
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        underTest.chargeCard(paymentRequest);
        //Then
        then(paymentRepository).should().save(paymentArgumentCaptor.capture());
        Payment paymentArgumentCaptorValue = paymentArgumentCaptor.getValue();
        assertThat(paymentArgumentCaptorValue).isEqualToComparingFieldByField(payment);
    }

    @Test
    void itShouldThrowWhenCustomerIsNotFound() {
        //Given
        UUID customerId = UUID.randomUUID();
        Payment payment = new Payment(null,
                customerId,
                new BigDecimal("10.00"),
                Currency.EURO,
                "visa",
                "donation");
        when(customerRepository.findById(any()))
                .thenReturn(Optional.empty()
                );

        //When
        //Then
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        assertThatThrownBy(() -> underTest.chargeCard(paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("Customer id : %s does not exist",
                        paymentRequest.getPayment().getCustomerId()));
        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldThrowWhenCardIsNotCharged() {
        //Given
        UUID customerId = UUID.randomUUID();
        Payment payment = new Payment(null,
                customerId,
                new BigDecimal("10.00"),
                Currency.USD,
                "visa",
                "donation");
        when(customerRepository.findById(any()))
                .thenReturn(Optional.of(mock(Customer.class))
                );
        when(cardPaymentCharger.chargeCard(
                payment.getSource(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getDescription()
        )).thenReturn(new CardPaymentCharge(false));
        //When
        //Then
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        assertThatThrownBy(() -> underTest.chargeCard(paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(
                        String.format("Card is not charged for customer: %s ",
                                paymentRequest.getPayment().getCustomerId()));
        then(paymentRepository).should(never()).save(any());
    }


    @Test
    void itShouldThrowWhenCurrencyIsNotSupported() {
        //Given
        UUID customerId = UUID.randomUUID();
        Payment payment = new Payment(null,
                customerId,
                new BigDecimal("10.00"),
                Currency.EURO,
                "visa",
                "donation");
        when(customerRepository.findById(any()))
                .thenReturn(Optional.of(mock(Customer.class))
                );

        //When
        //Then
        PaymentRequest paymentRequest = new PaymentRequest(payment);
        assertThatThrownBy(() -> underTest.chargeCard(paymentRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(
                        String.format("Currency: %s is not supported",
                                paymentRequest.getPayment().getCurrency()));
        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }


    @Test
    void itShouldGetPaymentById() {
        //Given
        Long paymentId = 1L;
        Payment payment = new Payment(paymentId,
                UUID.randomUUID(),
                new BigDecimal("10.00"),
                Currency.EURO,
                "visa",
                "donation");
        when(paymentRepository.findById(any()))
                .thenReturn(Optional.of(payment));
        //When
        Payment actual = underTest.getPaymentById(paymentId);
        //Then
        assertThat(actual).isEqualToComparingFieldByField(payment);
    }

    @Test
    void itShouldThrowWhenPaymentDoesntExistsInGetPaymentById() {
        //Given
        Long paymentId = 1L;
        when(paymentRepository.findById(any()))
                .thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() -> underTest.getPaymentById(paymentId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(String.format("payment id : %s does not exist",paymentId));
    }
}
