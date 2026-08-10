package com.example.equispedia.Controllers

import com.example.equispedia.Services.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.CrossOrigin

data class PaymentIntentRequest(val amount: Long, val currency: String = "usd")
data class PaymentIntentResponse(val clientSecret: String)

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = ["http://localhost:5173", "http://127.0.0.1:5173"])
class PaymentController(private val paymentService: PaymentService) {

    @PostMapping("/create-payment-intent")
    fun createPaymentIntent(@RequestBody request: PaymentIntentRequest): ResponseEntity<PaymentIntentResponse> {
        val clientSecret = paymentService.createPaymentIntent(request.amount, request.currency)
        return ResponseEntity.ok(PaymentIntentResponse(clientSecret))
    }
}
