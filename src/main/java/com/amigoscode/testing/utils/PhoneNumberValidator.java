package com.amigoscode.testing.utils;



import org.springframework.stereotype.Component;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PhoneNumberValidator implements Predicate<String> {

    private static final Pattern PATTERN = Pattern.compile(
            "\\+9665\\d{8}|05\\d{8}|\\+1\\(\\d{3}\\)\\d{3}-\\d{4}|\\+1\\d{10}|\\d{3}-\\d{3}-\\d{4}"
    );
    @Override
    public boolean test(String phoneNumber) {
        Matcher matcher = PATTERN.matcher(phoneNumber.trim());
        return matcher.matches();
    }

}
