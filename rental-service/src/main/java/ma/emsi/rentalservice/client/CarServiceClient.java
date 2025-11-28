package ma.emsi.rentalservice.client;

import ma.emsi.rentalservice.dto.CarDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CarServiceClient {

    private final RestTemplate restTemplate;
    private final String carServiceUrl;

    public CarServiceClient(RestTemplate restTemplate,
                           @Value("${car.service.url}") String carServiceUrl) {
        this.restTemplate = restTemplate;
        this.carServiceUrl = carServiceUrl;
    }

    public CarDto getCarById(Long carId) {
        // Spring Data REST endpoint
        String url = carServiceUrl + "/cars/" + carId;
        return restTemplate.getForObject(url, CarDto.class);
    }

    public void updateCarAvailability(Long carId, Boolean available) {
        // Get the car, update availability, and put it back
        CarDto car = getCarById(carId);
        if (car != null) {
            car.setAvailable(available);
            String url = carServiceUrl + "/cars/" + carId;
            restTemplate.put(url, car);
        }
    }
}

