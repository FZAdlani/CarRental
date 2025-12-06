package ma.emsi.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalDTO {
    private Long id;
    private Long carId;
    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private String startDate;
    private String endDate;
    private Double totalPrice;
    private String status;
    private String paymentId;
}