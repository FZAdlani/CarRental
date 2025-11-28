# Rental Service

Service de gestion des locations de voitures.

## Prérequis

- Java 17+
- Maven 3.6+
- Car Service en cours d'exécution sur le port 8081

## Configuration

Le service s'exécute sur le port **8082** par défaut.

### Base de données

Le service utilise H2 en mémoire. Console H2 accessible à : `http://localhost:8082/h2-console`

- URL JDBC: `jdbc:h2:mem:rentaldb`
- Username: `sa`
- Password: (vide)

## Démarrage

```bash
cd rental-service
mvn spring-boot:run
```

Ou depuis la racine du projet :

```bash
mvn -pl rental-service spring-boot:run
```

## API Endpoints

### Créer une location

```http
POST /api/rentals
Content-Type: application/json

{
  "carId": 1,
  "customerName": "Ahmed Bennani",
  "customerEmail": "ahmed@example.com",
  "customerPhone": "0612345678",
  "startDate": "2025-12-01",
  "endDate": "2025-12-10"
}
```

### Récupérer toutes les locations

```http
GET /api/rentals
```

### Récupérer les locations par email client

```http
GET /api/rentals?customerEmail=ahmed@example.com
```

### Récupérer une location par ID

```http
GET /api/rentals/{id}
```

### Mettre à jour le statut d'une location

```http
PATCH /api/rentals/{id}/status?status=ACTIVE
```

Statuts possibles :
- `PENDING` - En attente
- `CONFIRMED` - Confirmée
- `ACTIVE` - Active
- `COMPLETED` - Terminée
- `CANCELLED` - Annulée

### Supprimer une location

```http
DELETE /api/rentals/{id}
```

## Modèle de données

### Rental

```json
{
  "id": 1,
  "carId": 1,
  "car": {
    "id": 1,
    "brand": "Toyota",
    "model": "Camry",
    "year": 2023,
    "licensePlate": "ABC-123",
    "color": "White",
    "dailyPrice": 350.00,
    "available": false
  },
  "customerName": "Ahmed Bennani",
  "customerEmail": "ahmed@example.com",
  "customerPhone": "0612345678",
  "startDate": "2025-12-01",
  "endDate": "2025-12-10",
  "totalPrice": 3150.00,
  "status": "CONFIRMED",
  "createdAt": "2025-11-22"
}
```

## Communication avec Car Service

Le service communique avec le car-service via REST :
- Récupère les informations de voiture : `GET http://localhost:8081/cars/{id}`
- Met à jour la disponibilité : `PUT http://localhost:8081/cars/{id}`

URL du car-service configurable dans `application.properties` :
```properties
car.service.url=http://localhost:8081
```

## Gestion des erreurs

Le service retourne des codes HTTP appropriés :
- `200 OK` - Succès
- `201 CREATED` - Ressource créée
- `204 NO CONTENT` - Suppression réussie
- `400 BAD REQUEST` - Données invalides
- `404 NOT FOUND` - Ressource non trouvée
- `409 CONFLICT` - Voiture non disponible
- `500 INTERNAL SERVER ERROR` - Erreur serveur

Exemple de réponse d'erreur :
```json
{
  "status": 404,
  "message": "Car not found with ID: 10",
  "timestamp": "2025-11-22T10:30:00"
}
```

## Fonctionnalités

- ✅ Création de locations avec validation des dates
- ✅ Calcul automatique du prix total
- ✅ Vérification de la disponibilité des voitures
- ✅ Mise à jour automatique de la disponibilité des voitures
- ✅ Gestion des statuts de location
- ✅ Recherche par email client
- ✅ Gestion d'erreurs complète
- ✅ Logging détaillé

