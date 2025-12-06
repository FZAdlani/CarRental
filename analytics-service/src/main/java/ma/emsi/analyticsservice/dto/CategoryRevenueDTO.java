package ma.emsi.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRevenueDTO {
    private String category;
    private Integer totalRentals;
    private Double totalRevenue;
    private Double averageRevenuePerRental;
}