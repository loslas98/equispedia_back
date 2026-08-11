package com.example.equispedia.Services

import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import jakarta.annotation.PostConstruct

@Service
class PaymentService {

    @Value("\${stripe.secret.key}")
    private lateinit var stripeSecretKey: String

    @PostConstruct
    fun init() {
        Stripe.apiKey = stripeSecretKey
    }

    fun createPaymentIntent(amount: Long, currency: String): String {
        val params = PaymentIntentCreateParams.builder()
            .setAmount(amount)
            .setCurrency(currency)
            .setAutomaticPaymentMethods(
                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                    .setEnabled(true)
                    .build()
            )
            .build()

        val paymentIntent = PaymentIntent.create(params)
        return paymentIntent.clientSecret
    }
}
