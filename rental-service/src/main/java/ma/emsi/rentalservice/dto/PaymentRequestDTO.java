package ma.emsi.rentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    private Long rentalId;
    private Double amount;
    private String currency = "usd";
    private String cardNumber;
    private String expMonth;
    private String expYear;
    private String cvc;
}