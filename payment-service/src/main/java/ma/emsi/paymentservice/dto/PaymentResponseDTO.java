package ma.emsi.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long paymentId;
    private String stripePaymentId;
    private String status;
    private Double amount;
    private String currency;
    private String message;
    private Long rentalId;
}