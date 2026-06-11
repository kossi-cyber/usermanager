# UserManager Application - API REST Sécurisée (JWT)

Cette application est un système de gestion des utilisateurs développé avec **Spring Boot 4 / Java 21** (ou supérieur), sécurisé via **Spring Security** et **JWT (JSON Web Tokens)**, et adossé à une base de données **PostgreSQL** conteneurisée.

---

## 🛠️ Prérequis

Avant de lancer l'application, assurez-vous d'avoir installé :
- **Java 21** ou une version supérieure.
- **Maven** (intégré à votre IDE ou en ligne de commande).
- **Docker** et **Docker Compose** (pour la base de données).

---

## 🚀 Guide de démarrage rapide

### 1. Lancer la base de données PostgreSQL
La base de données tourne dans un conteneur Docker nommé `schoollink-postgres`. Si le conteneur n'est pas actif, démarrez-le.

Si la base de données `usermanager` n'existe pas encore à l'intérieur, vous pouvez la créer en exécutant la commande suivante dans votre terminal :
```bash
docker exec -it schoollink-postgres psql -U schoollink -d postgres -c "CREATE DATABASE usermanager;"

2. Configuration (application.properties)

L'application est configurée pour écouter sur le port 8085. Assurez-vous que les identifiants de connexion à PostgreSQL correspondent à votre environnement :
Properties

server.port=8085
spring.datasource.url=jdbc:postgresql://localhost:5432/usermanager
spring.datasource.username=schoollink
spring.datasource.password=YOUR_PASSWORD
spring.jpa.hibernate.ddl-auto=update

3. Compiler et lancer l'application Spring Boot

Depuis la racine du projet, exécutez la commande suivante pour compiler et démarrer le serveur :
Bash

mvn spring-boot:run

(Ou lancez simplement la classe principale UsermanagerApplication depuis votre IDE comme IntelliJ).
📖 Documentation & Tests de l'API (Swagger)

Une fois l'application démarrée, une interface interactive Swagger UI est disponible pour tester directement tous les endpoints :

👉 URL de Swagger : http://localhost:8085/swagger-ui/index.html
Processus de test recommandé :

    Inscription : Utilisez l'endpoint POST /api/auth/register pour créer un compte utilisateur.

    Connexion : Utilisez l'endpoint POST /api/auth/login avec les mêmes identifiants pour générer un jeton JWT.

    Accès sécurisé : Copiez le jeton reçu, cliquez sur le bouton "Authorize" en haut de la page Swagger, saisissez Bearer <votre_token> et validez. Vous pourrez ensuite interroger les routes protégées (comme les requêtes PUT ou GET sur /api/users/).