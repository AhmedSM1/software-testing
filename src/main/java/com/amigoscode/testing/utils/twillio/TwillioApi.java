package com.amigoscode.testing.utils.twillio;

import com.stripe.net.RequestOptions;
import org.springframework.stereotype.Service;

@Service
public class TwillioApi {

    private final static RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey("sk_test_2344234323432432")
            .build();






}
