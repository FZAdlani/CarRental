package ma.emsi.analyticsservice.dto;

import lombok.Data;
import java.util.List;

// Wrapper pour la réponse HAL de Spring Data REST (cars)
@Data
public class CarsHalResponse {
    private Embedded _embedded;

    @Data
    public static class Embedded {
        private List<CarDTO> cars;
    }

    public List<CarDTO> getCars() {
        return _embedded != null ? _embedded.getCars() : List.of();
    }
}