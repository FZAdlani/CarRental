package ma.emsi.paymentservice.service;

import ma.emsi.paymentservice.client.RentalFeignClient;
import ma.emsi.paymentservice.dto.PaymentRequestDTO;
import ma.emsi.paymentservice.dto.PaymentResponseDTO;
import ma.emsi.paymentservice.dto.RentalDTO;
import ma.emsi.paymentservice.entity.Payment;
import ma.emsi.paymentservice.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RentalFeignClient rentalFeignClient;

    @Autowired
    private WebClient webClient;

    /**
     * Traiter un paiement avec Stripe SDK
     */
    public PaymentResponseDTO processPaymentWithStripe(PaymentRequestDTO request) {
        try {
            // Vérifier que la réservation existe
            RentalDTO rental = rentalFeignClient.getRentalById(request.getRentalId());

            if (!"PENDING".equals(rental.getStatus())) {
                throw new RuntimeException("Cette réservation ne peut pas être payée");
            }

            // Créer un PaymentIntent Stripe
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (request.getAmount() * 100))
                    .setCurrency(request.getCurrency())
                    .setDescription("Paiement pour location #" + request.getRentalId())
                    .putMetadata("rentalId", request.getRentalId().toString())
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Enregistrer le paiement dans la base
            Payment payment = new Payment();
            payment.setRentalId(request.getRentalId());
            payment.setStripePaymentId(paymentIntent.getId());
            payment.setAmount(request.getAmount());
            payment.setCurrency(request.getCurrency());
            payment.setStatus(Payment.PaymentStatus.PROCESSING);
            payment.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
            payment.setClientEmail(request.getClientEmail());
            payment.setCreatedAt(LocalDateTime.now());

            Payment savedPayment = paymentRepository.save(payment);

            // Confirmer la réservation
            rentalFeignClient.confirmRental(request.getRentalId(), paymentIntent.getId());

            return new PaymentResponseDTO(
                    savedPayment.getId(),
                    paymentIntent.getId(),
                    paymentIntent.getStatus(),
                    request.getAmount(),
                    request.getCurrency(),
                    "Paiement créé avec succès",
                    request.getRentalId()
            );

        } catch (StripeException e) {
            throw new RuntimeException("Erreur Stripe: " + e.getMessage());
        }
    }

    /**
     * Traiter un paiement avec WebClient
     */
    public Mono<PaymentResponseDTO> processPaymentWithWebClient(PaymentRequestDTO request) {
        // Vérifier la réservation
        RentalDTO rental = rentalFeignClient.getRentalById(request.getRentalId());

        if (!"PENDING".equals(rental.getStatus())) {
            throw new RuntimeException("Cette réservation ne peut pas être payée");
        }

        return webClient.post()
                .uri("/payment_intents")
                .bodyValue(String.format(
                        "amount=%d&currency=%s&description=Location%%20%%23%d",
                        (long) (request.getAmount() * 100),
                        request.getCurrency(),
                        request.getRentalId()
                ))
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    String paymentId = "pi_" + System.currentTimeMillis();

                    // Enregistrer le paiement
                    Payment payment = new Payment();
                    payment.setRentalId(request.getRentalId());
                    payment.setStripePaymentId(paymentId);
                    payment.setAmount(request.getAmount());
                    payment.setCurrency(request.getCurrency());
                    payment.setStatus(Payment.PaymentStatus.SUCCEEDED);
                    payment.setPaymentMethod(Payment.PaymentMethod.CREDIT_CARD);
                    payment.setClientEmail(request.getClientEmail());
                    payment.setCompletedAt(LocalDateTime.now());

                    Payment savedPayment = paymentRepository.save(payment);

                    // Confirmer la réservation
                    rentalFeignClient.confirmRental(request.getRentalId(), paymentId);

                    return new PaymentResponseDTO(
                            savedPayment.getId(),
                            paymentId,
                            "succeeded",
                            request.getAmount(),
                            request.getCurrency(),
                            "Paiement traité avec WebClient",
                            request.getRentalId()
                    );
                });
    }

    /**
     * Simuler un paiement (pour tests)
     */
    public PaymentResponseDTO simulatePayment(PaymentRequestDTO request) {
        // Vérifier la réservation
        RentalDTO rental = rentalFeignClient.getRentalById(request.getRentalId());

        if (!"PENDING".equals(rental.getStatus())) {
            throw new RuntimeException("Cette réservation ne peut pas être payée");
        }

        String simulatedPaymentId = "sim_" + System.currentTimeMillis();

        // Enregistrer le paiement simulé
        Payment payment = new Payment();
        payment.setRentalId(request.getRentalId());
        payment.setStripePaymentId(simulatedPaymentId);
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus(Payment.PaymentStatus.SUCCEEDED);
        payment.setPaymentMethod(Payment.PaymentMethod.SIMULATION);
        payment.setClientEmail(request.getClientEmail());
        payment.setCompletedAt(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        // Confirmer la réservation
        rentalFeignClient.confirmRental(request.getRentalId(), simulatedPaymentId);

        return new PaymentResponseDTO(
                savedPayment.getId(),
                simulatedPaymentId,
                "succeeded",
                request.getAmount(),
                request.getCurrency(),
                "Paiement simulé avec succès",
                request.getRentalId()
        );
    }

    /**
     * Récupérer un paiement par ID de réservation
     */
    public Payment getPaymentByRentalId(Long rentalId) {
        return paymentRepository.findByRentalId(rentalId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé pour la réservation: " + rentalId));
    }

    /**
     * Récupérer tous les paiements
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}