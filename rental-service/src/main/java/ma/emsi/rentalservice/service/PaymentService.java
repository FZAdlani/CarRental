package ma.emsi.rentalservice.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import ma.emsi.rentalservice.dto.PaymentRequestDTO;
import ma.emsi.rentalservice.dto.PaymentResponseDTO;
import ma.emsi.rentalservice.model.Rental;
import ma.emsi.rentalservice.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalService rentalService;

    /**
     * Méthode 1 : Utilisation de Stripe SDK (recommandé)
     */
    public PaymentResponseDTO processPaymentWithSDK(PaymentRequestDTO request) {
        try {
            // Vérifier que la réservation existe
            Rental rental = rentalRepository.findById(request.getRentalId())
                    .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

            if (rental.getStatus() != Rental.RentalStatus.PENDING) {
                throw new RuntimeException("Cette réservation ne peut pas être payée");
            }

            // Créer un PaymentIntent avec Stripe SDK
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount((long) (request.getAmount() * 100)) // Stripe utilise les centimes
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

            // Mettre à jour la réservation
            rentalService.confirmRental(request.getRentalId(), paymentIntent.getId());

            return new PaymentResponseDTO(
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
     * Méthode 2 : Utilisation de WebClient (pour démonstration)
     */
    public Mono<PaymentResponseDTO> processPaymentWithWebClient(PaymentRequestDTO request) {
        // Vérifier que la réservation existe
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (rental.getStatus() != Rental.RentalStatus.PENDING) {
            throw new RuntimeException("Cette réservation ne peut pas être payée");
        }

        // Appel à l'API Stripe via WebClient
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
                    // Parser la réponse (simplifié pour la démo)
                    String paymentId = "pi_" + System.currentTimeMillis();

                    // Mettre à jour la réservation
                    rentalService.confirmRental(request.getRentalId(), paymentId);

                    return new PaymentResponseDTO(
                            paymentId,
                            "succeeded",
                            request.getAmount(),
                            request.getCurrency(),
                            "Paiement traité avec succès via WebClient",
                            request.getRentalId()
                    );
                })
                .onErrorResume(error -> {
                    return Mono.just(new PaymentResponseDTO(
                            null,
                            "failed",
                            request.getAmount(),
                            request.getCurrency(),
                            "Erreur: " + error.getMessage(),
                            request.getRentalId()
                    ));
                });
    }

    /**
     * Méthode de simulation pour les tests (sans appel réel à Stripe)
     */
    public PaymentResponseDTO simulatePayment(PaymentRequestDTO request) {
        // Vérifier que la réservation existe
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));

        if (rental.getStatus() != Rental.RentalStatus.PENDING) {
            throw new RuntimeException("Cette réservation ne peut pas être payée");
        }

        // Simuler un paiement réussi
        String simulatedPaymentId = "sim_" + System.currentTimeMillis();

        // Mettre à jour la réservation
        rentalService.confirmRental(request.getRentalId(), simulatedPaymentId);

        return new PaymentResponseDTO(
                simulatedPaymentId,
                "succeeded",
                request.getAmount(),
                request.getCurrency(),
                "Paiement simulé avec succès (Mode Test)",
                request.getRentalId()
        );
    }
}