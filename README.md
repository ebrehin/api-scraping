# api-scraping

API REST Java (Spring Boot) déployée avec une base de données **MariaDB**, le tout conteneurisé via **Docker Compose**.

## Stack technique

| Composant | Version |
|---|---|
| Java | 17 |
| Framework | Spring Boot 3.4.2 |
| Base de données | MariaDB 10.x |
| Build | Gradle 8.x |
| Documentation | OpenAPI (Swagger) 2.3.0 |

## Structure du projet

```
api-scraping/
├── 📁 src
│   └── 📁 main
│       ├── 📁 java
│       │   └── 📁 com
│       │       ├── 📁 controllers
│       │       │   └── ☕ ScrapingController.java  # Point d'entrée de l'API
│       │       ├── 📁 entities
│       │       │   ├── ☕ Film.java                # Entité principale
│       │       │   ├── ☕ Artist.java              # Entité artiste
│       │       │   └── ☕ Poster.java              # Entité poster
│       │       ├── 📁 services                   # Logique métier
│       │       │   └── ☕ FilmService.java
│       │       └── 📁 scrapper                   # Logique de scraping OMDb
│       │           └── ☕ FilmScraper.java
│       └── 📁 resources
│           ├── 📄 application.properties         # Configuration Spring Boot
│           └── ⚙️ openapi.yaml
├── ⚙️ .gitignore
├── 📝 README.md
├── 🐳 Dockerfile                                 # Image Docker de l'application
├── ⚙️ docker-compose.yaml                        # Orchestration des conteneurs
├── 🐘 gradlew
└── 🐘 build.gradle
```

## Lancer l'application

### Prérequis

- Docker Desktop installé et démarré

### Démarrage

```bash
docker compose up --build
```

Cette commande :
1. Compile le projet Java avec Gradle (dans un container ou via build local selon config)
2. Lance un container **MariaDB** (`scrapping`)
3. Attend que MariaDB soit prêt
4. Lance l'application **Spring Boot**

L'API est accessible sur : **http://localhost:8080**  
La documentation Swagger UI est accessible sur : **http://localhost:8080/swagger-ui.html**

### Arrêt

```bash
docker compose down
```

Pour supprimer aussi le volume de données MariaDB (pour repartir d'une base vide) :

```bash
docker compose down -v
```

## Endpoints disponibles

| Méthode | Route | Description |
|---|---|---|
| GET | `/api/scrap/film?query={titre}` | Scrape les infos d'un film depuis OMDb et les sauvegarde |

### Exemples curl

```bash
# Scraper un film (ex: Inception)
curl "http://localhost:8080/api/scrap/film?query=Inception"
```

### Exemples de requêtes (Powershell)

```ps
# Scraper un film
Invoke-WebRequest -Uri "http://localhost:8080/api/scrap/film?query=Inception" -Method GET -UseBasicParsing
```

## Configuration

### Base de données

Les paramètres de connexion sont définis dans `application.properties` et synchronisés avec `docker-compose.yaml` :

| Propriété | Variable Env Docker (Service DB) | Valeur par défaut |
|---|---|---|
| `spring.datasource.url` | - | `jdbc:mariadb://localhost:3306/test` |
| `spring.datasource.password` | `MARIADB_ROOT_PASSWORD` | `monMotDePasseSuperSecret` |
| - | `MARIADB_DATABASE` | `test` |

### API OMDb

La clé API pour OMDb est configurée dans `application.properties` :

```properties
omdb.api-key=ab5f54b5
omdb.base-url=https://www.omdbapi.com/
```

## Développement sans Docker

Il est possible de tester localement en lançant une instance MariaDB sur votre machine (port 3306), puis en exécutant l'application via Gradle ou votre IDE. Assurez-vous que les identifiants dans `application.properties` correspondent à votre base locale.
