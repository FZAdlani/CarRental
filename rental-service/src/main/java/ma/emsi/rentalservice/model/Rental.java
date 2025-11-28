package ma.emsi.rentalservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long carId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String customerPhone;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RentalStatus status;

    @Column(updatable = false)
    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        if (status == null) {
            status = RentalStatus.PENDING;
        }
    }

    public enum RentalStatus {
        PENDING,
        CONFIRMED,
        ACTIVE,
        COMPLETED,
        CANCELLED
    }
}

