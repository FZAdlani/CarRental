package ma.emsi.carservice.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false, name = "car_year")
    private Integer year;

    @Column(nullable = false, unique = true)
    private String licensePlate;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private BigDecimal dailyPrice;

    @Column(nullable = false)
    private Boolean available = true;

    // Constructeurs
    public Car() {}

    public Car(String brand, String model, Integer year, String licensePlate,
               String color, BigDecimal dailyPrice) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.color = color;
        this.dailyPrice = dailyPrice;
        this.available = true;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public BigDecimal getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(BigDecimal dailyPrice) { this.dailyPrice = dailyPrice; }

    public Boolean getAvailable() { return available; }
    public void setAvailable(Boolean available) { this.available = available; }
}