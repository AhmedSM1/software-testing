package com.amigoscode.testing.payment.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeApi {

    private final static RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey("sk_test_2344234323432432")
            .build();



    public Charge create(Map<String, Object> params) throws StripeException {
        return Charge.create(params,requestOptions);
    }



}
