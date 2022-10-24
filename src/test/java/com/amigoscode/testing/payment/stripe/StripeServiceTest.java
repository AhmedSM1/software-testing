package com.amigoscode.testing.payment.stripe;

import com.amigoscode.testing.payment.CardPaymentCharge;
import com.amigoscode.testing.payment.Currency;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;


class StripeServiceTest {
    private StripeService underTest;
    @Mock
    private StripeApi stripeApi;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        underTest = new StripeService(stripeApi);
    }

    @Test
    void itShouldChargeCard() throws StripeException {
        //Given
        String cardSource = "0x0x0";
        BigDecimal amount = new BigDecimal("10.00");
        String description = "Zakat";
        Currency currency = Currency.USD;
        Charge t = new Charge();
        t.setPaid(true);
        when(stripeApi.create(any())).thenReturn(t);
        //When
        CardPaymentCharge cardPaymentCharge = underTest.chargeCard(cardSource, amount, currency, description);
        //Then
        ArgumentCaptor<Map<String,Object>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        then(stripeApi).should().create(mapArgumentCaptor.capture());
        Map<String, Object> params  = mapArgumentCaptor.getValue();
        assertThat(params.keySet()).hasSize(4);
        assertThat(params.get("amount")).isEqualTo(amount);
        assertThat(params.get("currency")).isEqualTo(currency);
        assertThat(params.get("description")).isEqualTo(description);
        assertThat(params.get("source")).isEqualTo(cardSource);
        assertThat(cardPaymentCharge.isCharged()).isTrue();
    }


}
