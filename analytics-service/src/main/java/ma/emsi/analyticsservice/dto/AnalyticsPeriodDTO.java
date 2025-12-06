package ma.emsi.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsPeriodDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<CarOccupancyDTO> carOccupancies;
    private GlobalStatisticsDTO globalStatistics;
}