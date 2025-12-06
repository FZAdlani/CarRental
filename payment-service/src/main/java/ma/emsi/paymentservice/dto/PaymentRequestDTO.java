package ma.emsi.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private Long rentalId;
    private Double amount;
    private String currency;
    private String clientEmail;
    private String paymentMethod;
}