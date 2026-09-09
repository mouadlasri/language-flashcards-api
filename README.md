# Language Flashcards API

Spring Boot practice project for managing private language-learning decks and
flashcards. The project focuses on integrating Supabase Auth with Spring
Security, validating externally issued JWTs, deriving resource ownership from
the authenticated user, and using Supabase PostgreSQL through JPA and Flyway.

## Stack

- Java 21
- Spring Boot 4.1
- Spring Web MVC
- Spring Security OAuth2 Resource Server
- Spring Data JPA and Hibernate
- Supabase Auth
- Supabase PostgreSQL
- Flyway
- Maven

## Domains

- `User`: local application profile linked to a Supabase Auth identity
- `Deck`: private collection owned by one user
- `Flashcard`: front/back content belonging to one deck

```text
Supabase Auth user
        |
        | JWT sub
        v
Application user
        |
        | 1:N
        v
Deck
        |
        | 1:N
        v
Flashcard
```

## Authentication Architecture

Supabase owns authentication:

- signup and login
- password storage and reset
- access-token and refresh-token issuance
- email identity

Spring Boot acts as an OAuth2 Resource Server:

- reads the bearer token from each request
- verifies the ES256 signature through Supabase JWKS
- validates the issuer, audience, and expiration
- exposes the validated JWT as the authenticated principal

```text
Client
  |
  | signup/login
  v
Supabase Auth
  |
  | ES256 access-token JWT
  v
Client
  |
  | Authorization: Bearer <access-token>
  v
Spring Security
  |
  | validated JWT sub
  v
Application service and ownership checks
```

There is no application password column, custom login controller, token
generator, or custom JWT filter.

## Authorization Model

- Every endpoint requires a valid Supabase access token.
- The local profile ID is the UUID from the JWT `sub` claim.
- The local email is copied from the verified JWT `email` claim.
- Clients never submit a user ID when operating on their own profile or data.
- Deck queries are scoped to the authenticated user.
- Flashcard operations first verify that the parent deck belongs to the
  authenticated user.
- Profiles, decks, and flashcards use soft deletion.
- A deleted local profile does not delete or disable the Supabase Auth account.

## Database

Flyway manages the application schema:

- `V1__create_tables.sql`: creates users, decks, flashcards, foreign keys, and
  indexes
- `V2__enable_rls.sql`: enables Row Level Security on application tables
- `V3__add_updated_at_triggers.sql`: adds database-managed `updated_at`
  timestamps

The Spring Boot backend connects directly to PostgreSQL using the Supabase
session pooler. RLS has no client-facing policies, so the application tables
cannot be accessed through Supabase's anonymous or authenticated Data API
roles. Authorization remains in the Spring service layer.

After Flyway creates its metadata table, enable RLS on that table once through
the Supabase SQL editor:

```sql
ALTER TABLE flyway_schema_history ENABLE ROW LEVEL SECURITY;
```

This is intentionally performed outside a Flyway migration because Flyway may
lock its own history table while migrations are running.

## Configuration

Committed `application.yml` configuration reads these environment variables:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
DB_POOL_SIZE
SUPABASE_JWT_ISSUER
SUPABASE_JWKS_URI
SUPABASE_JWT_AUDIENCE
SUPABASE_JWT_ALGORITHM
SERVER_PORT
```

For local development, create the ignored file:

```text
src/main/resources/application-local.yml
```

Example:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://<session-pooler-host>:5432/postgres?sslmode=require
    username: postgres.<project-ref>
    password: <database-password>
    hikari:
      maximum-pool-size: 5

  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://<project-ref>.supabase.co/auth/v1
          jwk-set-uri: https://<project-ref>.supabase.co/auth/v1/.well-known/jwks.json
          audiences:
            - authenticated
          jws-algorithms: ES256
```

Run locally:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Using Supabase Auth

Create or register a user through Supabase Auth, then sign in to obtain an
`access_token`. Send that access token to the API:

```http
Authorization: Bearer <access-token>
```

Do not send the refresh token, publishable key, database password, or Supabase
secret key as API bearer credentials.

## Endpoints

All endpoints below are protected.

### Profile

- `POST /api/v1/me/profile`
- `GET /api/v1/me/profile`
- `PATCH /api/v1/me/profile`
- `DELETE /api/v1/me/profile`

Create-profile body:

```json
{
  "displayName": "Mouad"
}
```

The profile ID and email come from the verified JWT rather than the request
body.

### Decks

- `POST /api/v1/decks`
- `GET /api/v1/decks?page=0&size=20`
- `GET /api/v1/decks/{deckId}`
- `PATCH /api/v1/decks/{deckId}`
- `DELETE /api/v1/decks/{deckId}`

Create-deck body:

```json
{
  "title": "Korean Vocabulary",
  "description": "Words collected during daily study"
}
```

### Flashcards

- `POST /api/v1/decks/{deckId}/flashcards`
- `GET /api/v1/decks/{deckId}/flashcards?page=0&size=20`
- `GET /api/v1/decks/{deckId}/flashcards/{flashcardId}`
- `PATCH /api/v1/decks/{deckId}/flashcards/{flashcardId}`
- `DELETE /api/v1/decks/{deckId}/flashcards/{flashcardId}`

Create-flashcard body:

```json
{
  "frontText": "안녕하세요",
  "backText": "Hello"
}
```

Flashcard responses include the parent deck ID and title.

## Error Handling

The API returns consistent JSON errors for:

- request validation failures: `400 Bad Request`
- invalid PATCH values: `400 Bad Request`
- missing or inaccessible resources: `404 Not Found`
- duplicate local profiles: `409 Conflict`
- missing or invalid access tokens: `401 Unauthorized`

Returning `404` for a deck or flashcard that is not owned by the authenticated
user avoids revealing whether another user's resource exists.
