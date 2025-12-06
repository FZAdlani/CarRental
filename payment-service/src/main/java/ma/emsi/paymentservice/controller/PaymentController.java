package ma.emsi.paymentservice.controller;

import ma.emsi.paymentservice.dto.PaymentRequestDTO;
import ma.emsi.paymentservice.dto.PaymentResponseDTO;
import ma.emsi.paymentservice.entity.Payment;
import ma.emsi.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
    
    @Autowired
    private PaymentService paymentService;

    /**
     * Paiement avec Stripe SDK
     */
    @PostMapping("/stripe")
    public ResponseEntity<?> processPaymentWithStripe(@RequestBody PaymentRequestDTO request) {
        try {
            PaymentResponseDTO response = paymentService.processPaymentWithStripe(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur: " + e.getMessage());
        }
    }

    /**
     * Paiement avec WebClient
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
     * Simulation de paiement
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

    /**
     * Récupérer un paiement par rental ID
     */
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<?> getPaymentByRentalId(@PathVariable Long rentalId) {
        try {
            Payment payment = paymentService.getPaymentByRentalId(rentalId);
            return ResponseEntity.ok(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Erreur: " + e.getMessage());
        }
    }

    /**
     * Lister tous les paiements
     */
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }
}