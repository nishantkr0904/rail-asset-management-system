# rail-asset-management-system

[![CI](https://github.com/nishantkr0904/rail-asset-management-system/actions/workflows/ci.yml/badge.svg)](https://github.com/nishantkr0904/rail-asset-management-system/actions/workflows/ci.yml)

Backend-focused Rail Asset Management System built with Spring Boot, MySQL, and AWS EC2 featuring role-based access control, audit tracking, 85%+ test coverage, and indexed relational schema optimization.

## Local Development (dev profile)

1. Ensure JDK 17 and Maven are installed.
2. Run the application with the default dev profile (H2 in-memory database):

```bash
mvn spring-boot:run
```

> The dev profile wires the in-memory datasource automatically; no extra variables are needed.

## Production Profile Usage

1. Package the application:

```bash
mvn clean package
```

2. Run the executable jar with the `prod` profile enabled (requires external MySQL credentials provided via environment variables):

```bash
java -jar target/rail-asset-management-system-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Docker Deployment

1. Build the container image:

```bash
docker build -t rail-asset-api .
```

2. Run the container with the production profile (Spring Boot will detect env vars automatically):

```bash
docker run -p 8080:8080 \
	-e DB_URL="jdbc:mysql://<host>:<port>/<database>?useSSL=false&serverTimezone=UTC" \
	-e DB_USERNAME="rail_user" \
	-e DB_PASSWORD="secure-password" \
	-e SPRING_PROFILES_ACTIVE=prod \
	rail-asset-api
```

## Environment Variables

| Variable      | Description                                                                                              |
| ------------- | -------------------------------------------------------------------------------------------------------- |
| `DB_URL`      | Fully qualified JDBC URL for the production MySQL instance (e.g., `jdbc:mysql://host:3306/rail_assets`). |
| `DB_USERNAME` | Database user with read/write privileges.                                                                |
| `DB_PASSWORD` | Password for the database user.                                                                          |

These variables are consumed by the `prod` profile as defined in `application-prod.properties`. When running via Docker, pass them with `-e`; when running the jar directly, export them before invoking Java.
