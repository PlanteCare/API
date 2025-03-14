# API PlanteCare

Une API RESTful construite avec le framework Ktor pour supporter l'application mobile PlanteCare. Cette API gère l'authentification des utilisateurs, l'enregistrement, et d'autres fonctionnalités backend.

Ce projet a été créé à l'aide du [Générateur de Projet Ktor](https://start.ktor.io).

## Fonctionnalités

Voici la liste des fonctionnalités incluses dans ce projet :

| Nom                                                                     | Description                                                                       |
| -----------------------------------------------------------------------|----------------------------------------------------------------------------------- |
| [Routing](https://start.ktor.io/p/routing)                             | Fournit une DSL structurée pour le routage                                        |
| [kotlinx.serialization](https://start.ktor.io/p/kotlinx-serialization) | Gère la sérialisation JSON en utilisant la bibliothèque kotlinx.serialization    |
| [Content Negotiation](https://start.ktor.io/p/content-negotiation)     | Fournit une conversion automatique du contenu selon les en-têtes Content-Type et Accept |
| [Exposed](https://start.ktor.io/p/exposed)                             | Ajoute la base de données Exposed à votre application                             |
| [Swagger](https://start.ktor.io/p/swagger)                             | Sert l'interface Swagger UI pour votre projet                                     |
| [Authentication](https://start.ktor.io/p/auth)                         | Fournit un point d'extension pour gérer l'en-tête Authorization                   |
| [Authentication JWT](https://start.ktor.io/p/auth-jwt)                 | Gère le schéma d'authentification par porteur de token JWT                        |
| [CORS](https://start.ktor.io/p/cors)                                   | Active le partage de ressources entre origines (CORS)                             |
| [OpenAPI](https://start.ktor.io/p/openapi)                             | Sert la documentation OpenAPI                                                     |
| [GSON](https://start.ktor.io/p/ktor-gson)                              | Gère la sérialisation JSON en utilisant la bibliothèque GSON                      |

Autres fonctionnalités implémentées dans ce projet :

- **Gestion des Utilisateurs**: Inscription, connexion et gestion des profils utilisateurs
- **Intégration Base de Données**: Connexion à une base de données MySQL avec l'ORM Exposed

## Stack Technique

- **Ktor**: Framework web asynchrone basé sur Kotlin
- **Exposed**: Bibliothèque SQL Kotlin pour l'accès à la base de données
- **MySQL**: Base de données pour le stockage persistant
- **JWT**: JSON Web Tokens pour l'authentification
- **Kotlin Serialization**: Sérialisation/désérialisation JSON
- **Gradle**: Système de build

## Structure du Projet

```
src/
├── main/
│   ├── kotlin/
│   │   ├── Application.kt            # Point d'entrée de l'application
│   │   ├── database/                 # Configuration de la base de données
│   │   ├── exception/                # Gestion des erreurs
│   │   ├── models/                   # Modèles de données (DTOs et DAOs)
│   │   ├── plugins/                  # Plugins Ktor (HTTP, Sécurité, Sérialisation)
│   │   ├── repositories/             # Accès aux données
│   │   ├── routing/                  # Définition des routes API
│   │   ├── services/                 # Logique métier
│   │   └── utils/                    # Utilitaires
│   └── resources/
│       ├── application.yaml          # Configuration de l'application
│       ├── application-prod.yaml     # Configuration pour la production
│       ├── logback.xml               # Configuration des logs
│       └── openapi/                  # Documentation OpenAPI
└── test/
    └── kotlin/                       # Tests unitaires
```

## Configuration

La configuration de l'application se trouve dans le fichier `src/main/resources/application.yaml`. Pour la production, utilisez `application-prod.yaml`.

### Base de Données

```yaml
database:
  url: "jdbc:mysql://localhost:3306/plantecare"
  user: "root"
  password: "root"
  driver: "com.mysql.cj.jdbc.Driver"
```

### JWT

```yaml
jwt:
  audience: "plantecare-api"
  issuer: "http://localhost"
  realm: "Plante Care API"
  secret: "secret"
```

## Endpoints API

### Authentification

- `POST /api/auth`: Connexion utilisateur
- `POST /api/user/register`: Inscription utilisateur

### Utilisateurs

- `GET /api/user`: Récupérer tous les utilisateurs
- `GET /api/user/{id}`: Récupérer un utilisateur par ID
- `POST /api/user/searchByEmail`: Rechercher un utilisateur par email
- `POST /api/user/searchByUsername`: Rechercher un utilisateur par nom d'utilisateur

## Compilation et Exécution

Pour compiler ou exécuter le projet, utilisez l'une des tâches suivantes :

| Tâche                         | Description                                                           |
| -------------------------------|----------------------------------------------------------------------- |
| `./gradlew test`              | Exécuter les tests                                                    |
| `./gradlew build`             | Compiler tout le projet                                               |
| `buildFatJar`                 | Construire un JAR exécutable du serveur avec toutes les dépendances   |
| `buildImage`                  | Construire l'image docker à utiliser avec le fat JAR                  |
| `publishImageToLocalRegistry` | Publier l'image docker localement                                     |
| `run`                         | Exécuter le serveur                                                   |
| `runDocker`                   | Exécuter en utilisant l'image docker locale                           |

Si le serveur démarre correctement, vous verrez la sortie suivante :

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

## Démarrage Rapide

1. Clonez le dépôt
2. Configurez la base de données MySQL
3. Modifiez les paramètres de configuration dans `application.yaml`
4. Exécutez l'application:

```bash
./gradlew run
```

L'API sera disponible à l'adresse `http://localhost:8080`

## Documentation API

La documentation Swagger est disponible à l'adresse `http://localhost:8080/openapi` une fois l'application démarrée.

## Liens Utiles

Voici quelques liens utiles pour vous aider à démarrer :

- [Documentation Ktor](https://ktor.io/docs/home.html)
- [Page GitHub de Ktor](https://github.com/ktorio/ktor)
- [Chat Slack de Ktor](https://app.slack.com/client/T09229ZC6/C0A974TJ9) (vous devrez [demander une invitation](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) pour rejoindre)
