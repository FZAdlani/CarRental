package ma.emsi.rentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequestDTO {
    private Long carId;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private LocalDate startDate;
    private LocalDate endDate;
}