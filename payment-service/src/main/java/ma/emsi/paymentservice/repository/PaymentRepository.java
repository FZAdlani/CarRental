package ma.emsi.paymentservice.repository;

import ma.emsi.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByRentalId(Long rentalId);

    Optional<Payment> findByStripePaymentId(String stripePaymentId);

    List<Payment> findByStatus(Payment.PaymentStatus status);

    List<Payment> findByClientEmail(String clientEmail);
}