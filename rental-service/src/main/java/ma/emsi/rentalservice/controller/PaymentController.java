package ma.emsi.rentalservice.controller;

import ma.emsi.rentalservice.dto.PaymentRequestDTO;
import ma.emsi.rentalservice.dto.PaymentResponseDTO;
import ma.emsi.rentalservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    /**
     * Endpoint principal : Paiement avec Stripe SDK
     */
    @PostMapping("/stripe")
    public ResponseEntity<?> processPaymentWithStripe(@RequestBody PaymentRequestDTO request) {
        try {
            PaymentResponseDTO response = paymentService.processPaymentWithSDK(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur: " + e.getMessage());
        }
    }

    /**
     * Endpoint alternatif : Paiement avec WebClient
     */
    @PostMapping("/webclient")
    public Mono<ResponseEntity<PaymentResponseDTO>> processPaymentWithWebClient(
            @RequestBody PaymentRequestDTO request) {

        return paymentService.processPaymentWithWebClient(request)
                .map(ResponseEntity::ok)
                .onErrorResume(error ->
                        Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build())
                );
    }

    /**
     * Endpoint de simulation (pour tests sans Stripe réel)
     */
    @PostMapping("/simulate")
    public ResponseEntity<?> simulatePayment(@RequestBody PaymentRequestDTO request) {
        try {
            PaymentResponseDTO response = paymentService.simulatePayment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur: " + e.getMessage());
        }
    }
}