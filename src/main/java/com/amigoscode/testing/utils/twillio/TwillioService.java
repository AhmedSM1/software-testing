package com.amigoscode.testing.utils.twillio;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(
        value = "stripe.enabled",
        havingValue = "true"
)
public class TwillioService implements SmsSenderService{

    private TwillioApi twillioApi;


    @Override
    public SmsSentResponse sendSms(String phoneNumber, String name) {
        return new SmsSentResponse(true);
    }
}
