# Car Rental - Système de Location de Voitures

Application de gestion de location de voitures basée sur une architecture microservices avec Spring Boot.

## Architecture

Le projet est composé de deux microservices :

1. **car-service** (Port 8081) - Gestion du catalogue de voitures
2. **rental-service** (Port 8082) - Gestion des locations

## Prérequis

- Java 17+
- Maven 3.6+

## Démarrage rapide

### 1. Démarrer le car-service

```bash
cd car-service
mvn spring-boot:run
```

Le service sera accessible sur `http://localhost:8081`

### 2. Démarrer le rental-service

```bash
cd rental-service
mvn spring-boot:run
```

Le service sera accessible sur `http://localhost:8082`

## Services

### Car Service (Port 8081)

Service de gestion des voitures utilisant Spring Data REST.

**Endpoints principaux :**
- `GET /cars` - Liste toutes les voitures
- `GET /cars/{id}` - Détails d'une voiture
- `POST /cars` - Ajouter une voiture
- `PUT /cars/{id}` - Mettre à jour une voiture
- `DELETE /cars/{id}` - Supprimer une voiture
- `GET /cars/search/findByAvailableTrue` - Voitures disponibles

**Console H2 :** `http://localhost:8081/h2-console`
- JDBC URL: `jdbc:h2:mem:cardb`
- Username: `sa`
- Password: (vide)

### Rental Service (Port 8082)

Service de gestion des locations de voitures.

**Endpoints principaux :**
- `POST /api/rentals` - Créer une location
- `GET /api/rentals` - Liste toutes les locations
- `GET /api/rentals/{id}` - Détails d'une location
- `GET /api/rentals?customerEmail=email` - Locations par client
- `PATCH /api/rentals/{id}/status` - Mettre à jour le statut
- `DELETE /api/rentals/{id}` - Annuler une location

**Console H2 :** `http://localhost:8082/h2-console`
- JDBC URL: `jdbc:h2:mem:rentaldb`
- Username: `sa`
- Password: (vide)

## Exemples d'utilisation

### 1. Lister les voitures disponibles

```bash
curl http://localhost:8081/cars
```

### 2. Créer une location

```bash
curl -X POST http://localhost:8082/api/rentals \
  -H "Content-Type: application/json" \
  -d '{
    "carId": 1,
    "customerName": "Ahmed Bennani",
    "customerEmail": "ahmed@example.com",
    "customerPhone": "0612345678",
    "startDate": "2025-12-01",
    "endDate": "2025-12-10"
  }'
```

### 3. Lister toutes les locations

```bash
curl http://localhost:8082/api/rentals
```

### 4. Mettre à jour le statut d'une location

```bash
curl -X PATCH "http://localhost:8082/api/rentals/1/status?status=ACTIVE"
```

## Technologies utilisées

- **Spring Boot 3.2.0**
- **Spring Data JPA** - Couche de persistance
- **Spring Data REST** - Exposition REST automatique (car-service)
- **Spring Web** - API REST
- **H2 Database** - Base de données en mémoire
- **Lombok** - Réduction du code boilerplate
- **Bean Validation** - Validation des données

## Fonctionnalités

### Car Service
- ✅ CRUD complet sur les voitures
- ✅ Recherche par disponibilité
- ✅ Recherche par marque
- ✅ Exposition REST automatique avec Spring Data REST
- ✅ Données de test préchargées

### Rental Service
- ✅ Création de locations avec validation
- ✅ Calcul automatique du prix total
- ✅ Vérification de disponibilité des voitures
- ✅ Mise à jour automatique de la disponibilité
- ✅ Gestion des statuts (PENDING, CONFIRMED, ACTIVE, COMPLETED, CANCELLED)
- ✅ Recherche par email client
- ✅ Communication inter-services (RestTemplate)
- ✅ Gestion d'erreurs complète
- ✅ Validation des dates de location

## Structure du projet

```
CarRental/
├── car-service/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── ma/emsi/carservice/
│   │       │       ├── model/Car.java
│   │       │       ├── repository/CarRepository.java
│   │       │       └── config/
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
│
├── rental-service/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── ma/emsi/rentalservice/
│   │       │       ├── model/Rental.java
│   │       │       ├── repository/RentalRepository.java
│   │       │       ├── service/RentalService.java
│   │       │       ├── controller/RentalController.java
│   │       │       ├── client/CarServiceClient.java
│   │       │       ├── dto/
│   │       │       ├── exception/
│   │       │       └── config/
│   │       └── resources/
│   │           └── application.properties
│   └── pom.xml
│
└── pom.xml
```

## Améliorations futures possibles

- 🔄 Service Discovery (Eureka)
- 🔄 API Gateway (Spring Cloud Gateway)
- 🔄 Configuration centralisée (Spring Cloud Config)
- 🔄 Circuit Breaker (Resilience4j)
- 🔄 Communication asynchrone (Kafka/RabbitMQ)
- 🔄 Authentification et autorisation (Spring Security)
- 🔄 Base de données PostgreSQL/MySQL
- 🔄 Containerisation (Docker)
- 🔄 Tests unitaires et d'intégration

## Documentation API

Pour une documentation interactive de l'API, vous pouvez ajouter Swagger/OpenAPI :

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

Puis accéder à : `http://localhost:8082/swagger-ui.html`

## Auteur

Projet de démonstration d'architecture microservices

