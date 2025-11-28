package ma.emsi.rentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private String color;
    private BigDecimal dailyPrice;
    private Boolean available;
}

