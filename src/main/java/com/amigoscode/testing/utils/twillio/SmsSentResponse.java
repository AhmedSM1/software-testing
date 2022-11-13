package com.amigoscode.testing.utils.twillio;

public class SmsSentResponse {

    private boolean sent;

    public SmsSentResponse(boolean sent) {
        this.sent = sent;
    }

    public boolean isSent() {
        return sent;
    }
}
