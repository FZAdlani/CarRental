package ma.emsi.analyticsservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private Long id;
    private Long carId;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;
    private String status;
    private String paymentId;
}