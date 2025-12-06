package ma.emsi.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String registrationNumber;
    private String color;
    private Double pricePerDay;
    private Boolean available;
    private String category;
}