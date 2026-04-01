package com.Ecommerce.ECommerceApp.controller;

import com.Ecommerce.ECommerceApp.entity.Order;
import com.Ecommerce.ECommerceApp.entity.User;
import com.Ecommerce.ECommerceApp.service.AuthService;
import com.Ecommerce.ECommerceApp.service.OrderService;
import com.Ecommerce.ECommerceApp.service.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/paypal")
@RequiredArgsConstructor
public class PaypalController {

private final PaypalService paypalService;

    private static final String SUCCESS_URL  = "http://localhost:8080/paypal/success";
    private static final String CANCEL_URL   = "http://localhost:8080/paypal/cancel";


    @PostMapping("/pay")
    public ResponseEntity<?> makePayment(@RequestParam double amount){
    try {
        Payment payment = paypalService.createPayment(
                amount, "USD", "paypal", "sale",
                "payment description", CANCEL_URL, SUCCESS_URL);

        for(Links links : payment.getLinks()){
            if("approval_url".equals(links.getRel())){
                return ResponseEntity.ok(
                        Map.of("redirectUrl", links.getHref())
                );
            }
        }
    } catch (PayPalRESTException e) {
        return ResponseEntity.status(500).body("Error creating payment");
    }

    return ResponseEntity.badRequest().body("Payment failed");
}
    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("paymentId") String paymentId , @RequestParam("PayerID") String payerId){


        try {
            Payment payment = paypalService.execute(paymentId, payerId);
            if(payment.getState().equals("approved")){
                return "payment is successfully done";
            }
        } catch (PayPalRESTException e) {
            throw new RuntimeException(e);
        }
        return  "payment failed";
    }


    @GetMapping("/cancel")
    public String cancelPay() {
        return "cancel";
    }

}
