package ma.emsi.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalStatisticsDTO {
    private Integer totalCars;
    private Integer availableCars;
    private Integer rentedCars;
    private Integer totalRentals;
    private Integer activeRentals;
    private Integer completedRentals;
    private Double totalRevenue;
    private Double averageOccupancyRate;
}