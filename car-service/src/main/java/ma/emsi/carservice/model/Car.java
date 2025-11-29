package ma.emsi.carservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "cars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private Double pricePerDay;

    @Column(nullable = false)
    private Boolean available = true;

    @Enumerated(EnumType.STRING)
    private CarCategory category;

    private String imageUrl;

    public enum CarCategory {
        ECONOMY,
        COMPACT,
        SEDAN,
        SUV,
        LUXURY,
        VAN
    }
}