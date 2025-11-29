package ma.emsi.rentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private String paymentId;
    private String status;
    private Double amount;
    private String currency;
    private String message;
    private Long rentalId;
}