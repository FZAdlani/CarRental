package ma.emsi.carservice.repository;

import ma.emsi.carservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(path = "cars")
@CrossOrigin(origins = "*")
public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByAvailable(Boolean available);

    List<Car> findByCategory(Car.CarCategory category);

    List<Car> findByBrandAndModel(String brand, String model);

    List<Car> findByPricePerDayLessThanEqual(Double maxPrice);
}