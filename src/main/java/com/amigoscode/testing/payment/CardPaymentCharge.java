package com.amigoscode.testing.payment;

public class CardPaymentCharge {
    private boolean isCharged;

    public CardPaymentCharge(boolean isCharged) {
        this.isCharged = isCharged;
    }

    public boolean isCharged() {
        return isCharged;
    }


}
