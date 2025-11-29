package ma.emsi.rentalservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Rental {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long carId;

    @Column(nullable = false)
    private String clientName;

    @Column(nullable = false)
    private String clientEmail;

    @Column(nullable = false)
    private String clientPhone;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status = RentalStatus.PENDING;

    private String paymentId;

    public enum RentalStatus {
        PENDING,
        CONFIRMED,
        ACTIVE,
        COMPLETED,
        CANCELLED
    }
}
