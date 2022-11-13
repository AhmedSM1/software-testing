package com.amigoscode.testing.utils.twillio;

import org.springframework.stereotype.Service;

@Service
public interface SmsSenderService {

    SmsSentResponse sendSms(String phoneNumber,String name);

}
