package com.amigoscode.testing.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public void createPayment(
                              @RequestBody PaymentRequest paymentRequest){
        paymentService.chargeCard(paymentRequest);
    }

    @GetMapping("/{paymentId}")
    public Payment getPayment(@PathVariable Long paymentId){
        return paymentService.getPaymentById(paymentId);
    }



}
