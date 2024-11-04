# Transaction Converter - Architecture

## Overview

This project is organized into layered architecture, with core packages for controllers, services, repositories, and integrations. It supports currency conversion for purchase transactions initially recorded in U.S. dollars by interfacing with external APIs, including the U.S. Treasury Rates API. Below is an outline of the architecture, testing strategy, and key tools used.

### Libraries and Tools

The project uses several essential libraries and frameworks:

- **[Spring Boot](https://spring.io/guides/gs/spring-boot/)**: Provides dependency injection and inversion of control.
- **[H2](https://www.h2database.com/html/tutorial.html)**: An in-memory database for lightweight data persistence during development and testing.
- **[JUnit](https://junit.org/junit5/docs/current/user-guide/)**: Supports automated testing.
- **[Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)**: Manages build automation and dependencies.
- **[OpenAPI](https://swagger.io/tools/open-source/getting-started/)**: Enables live API documentation and testing.
- **[GitHub Actions](https://docs.github.com/en/actions/learn-github-actions)**: Automates tests and manages the continuous integration pipeline.

### Code Structure

The code is organized by layer, with each package serving a distinct purpose:

- **Entity Layer**: The main entity is `Transaction`, representing a purchase recorded in U.S. dollars and stored in the database. Another key concept is `ExchangedTransaction`, which is an enriched version of `Transaction` that includes currency conversion data. While only `Transaction` is persisted, the conversion logic resides in the `ExchangedTransaction` service layer.

- **Integration Layer**: The `govtreasuryapi` package manages communication with the U.S. Treasury API. It filters results by selecting rates up to the transaction date, excluding rates older than six months. Rates are sorted by date, with the latest displayed first. If no rate meets the criteria, users are notified, and the conversion is not performed.

- **Controller Layer**: API endpoints are defined in the `controller` package, serving as entry points to the application.

### Continuous Integration

This project uses GitHub Actions for continuous integration (CI). Each commit to the `main` branch triggers the CI pipeline to:

- Run all automated tests.
- Build the project.
- Save the executable JAR file.

The CI configuration can be found at `.github/workflows/ga-pipeline.yml`. Failed tests are marked with a red icon next to the commit hash, while successful tests show a green icon. The README includes a status badge that indicates the latest build status on the `main` branch.

### Automated Testing

Automated tests are created with JUnit 5 and can be found under `src/test` directory. Each application layer has its own unit tests, with dependencies mocked using Mockito.

To run all tests, execute this command in the project’s root directory:
```bash
./mvnw verify
