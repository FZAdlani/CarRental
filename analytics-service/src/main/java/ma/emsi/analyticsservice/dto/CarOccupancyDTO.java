package ma.emsi.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarOccupancyDTO {
    private Long carId;
    private String brand;
    private String model;
    private String registrationNumber;
    private Integer totalRentals;
    private Integer totalDaysRented;
    private Double occupancyRate;
    private Double totalRevenue;
    private String status;
}