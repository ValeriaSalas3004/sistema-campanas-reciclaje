# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

The Maven module lives in `recycling-campaign-system/`, not at the `backend/` root. Run all commands from there (or pass `-f recycling-campaign-system`):

```bash
./mvnw clean install        # build
./mvnw test                 # run tests
./mvnw spring-boot:run       # run the app (port 8080)
```

The app requires three environment variables for the PostgreSQL connection — there are no defaults in `application.properties`, so it will fail to start without them:

```
URL, USERNAME, PASSWORD
```

## Architecture

Five feature modules, each following a consistent **Controller → Service → Repository** layering: `User`, `Campaign`, `Report`, `Waste`, `Zone`.

`Report` is the central aggregate entity — it has a `@ManyToOne` FK to each of `User`, `Campaign`, `WasteType`, and `RecollectionZone`. When adding a new module that needs to relate to existing data, it likely hangs off `Report` rather than the other entities directly.

Packages follow standard lowercase Spring convention: `controller/`, `model/`, `repository/`, `service/`, under `com.example.recycling_campaign_system`.

Only the `User` module uses DTOs (`model/dto/`: `UserRequestDTO`, `UserResponseDTO`, `UserLoginDTO`). Every other module's controllers accept and return JPA entities directly — keep that in mind when changing entity fields, since it changes the API response shape too.

There is no Spring Security configured. `UserController` does manual email/password regex validation and login by direct comparison; there's no hashing, token issuance, or role enforcement despite a `role` field on `User`.

A Postman collection with working example requests for all endpoints is at `../postman/collection.json`.
