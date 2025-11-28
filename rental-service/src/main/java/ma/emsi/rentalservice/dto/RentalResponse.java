package ma.emsi.rentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.rentalservice.model.Rental;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalResponse {
    private Long id;
    private Long carId;
    private CarDto car;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private Rental.RentalStatus status;
    private LocalDate createdAt;
}

