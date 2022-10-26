package com.amigoscode.testing.payment;

import com.amigoscode.testing.customer.Customer;
import com.amigoscode.testing.customer.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PaymentService {
    private PaymentRepository paymentRepository;
    private CustomerRepository customerRepository;
    private CardPaymentCharger cardPaymentCharger;
    private static final List<Currency> ACCEPTED_CURRENCIES = List.of(Currency.USD,Currency.SAR);

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, CustomerRepository customerRepository, CardPaymentCharger cardPaymentCharger) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.cardPaymentCharger = cardPaymentCharger;
    }

    void chargeCard(PaymentRequest paymentRequest){
        Optional<Customer> customer = customerRepository.findById(paymentRequest.getPayment().getCustomerId());
        if (customer.isEmpty()){
            throw new IllegalStateException(String.format("Customer id : %s does not exist",
                    paymentRequest.getPayment().getCustomerId()));
        }

        boolean isCurrencyAccepted = ACCEPTED_CURRENCIES.stream()
                .anyMatch(c -> c.equals(paymentRequest.getPayment().getCurrency()));

        if (!isCurrencyAccepted){
            throw new IllegalStateException(
                    String.format("Currency: %s is not supported",
                    paymentRequest.getPayment().getCurrency()));
        }
        CardPaymentCharge cardPaymentCharge = cardPaymentCharger.chargeCard(
                paymentRequest.getPayment().getSource(),
                paymentRequest.getPayment().getAmount(),
                paymentRequest.getPayment().getCurrency(),
                paymentRequest.getPayment().getDescription()
        );
        if (!cardPaymentCharge.isCharged()){
            throw new IllegalStateException(
                    String.format("Card is not charged for customer: %s ",
                            paymentRequest.getPayment().getCustomerId()));
        }
        paymentRepository.save(paymentRequest.getPayment());
    }

    Payment getPaymentById(Long paymentId){
        Optional<Payment> payment = this.paymentRepository.findById(paymentId);
        if (payment.isEmpty()){
            throw new IllegalStateException(String.format("payment id : %s does not exist",
                    paymentId));
        }
        return payment.get();
    }


}
