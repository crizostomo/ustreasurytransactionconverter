# US Treasury Transaction Converter

![Build status](https://github.com/ceccon-t/transactionconverter/actions/workflows/ga-pipeline.yml/badge.svg "Build status")

## Description

The Transaction Converter is a web service designed to facilitate the conversion of purchase transaction values recorded in U.S. dollars into various currencies. The system enables users to store new transactions, view existing transactions, and convert previously recorded transactions.

It utilizes the [Treasury Reporting Rates of Exchange API](https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange) as its data source, allowing access via HTTP requests. To ensure successful conversions, users must utilize currencies supported by the API, and conversions will only proceed if an exchange rate is available for a date within the last six months prior to the transaction date.

## Technical Summary

### Main Frameworks and Tools

Developed in Java, the project employs the Spring Boot framework alongside Maven as its build tool. For data persistence, it uses an H2 database, which is configured to store data in the host's filesystem, ensuring persistence across application restarts (data is saved to the `persistence` folder in the working directory).

### Automation

The project leverages GitHub Actions to run all tests and build the system whenever a push occurs on the `main` branch. An artifact containing the resulting JAR file is stored for each run of this workflow. The pipeline definition script is located at `.github/workflows/ga-pipeline.yml`.

### Code Organization

For an overview of the code structure, refer to `architecture.md`.

## How to Use

To explore the system and understand its capabilities, access the OpenAPI live documentation. If running locally, it can be found at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

The live documentation provides examples of the required requests and endpoints for interacting with the system using standard HTTP calls. Tools such as Postman or curl can be utilized to test these endpoints. The most relevant endpoints include:

- **GET** `/api/v1/transaction`: Lists all stored transactions.
  - **Example**:
      ```bash
      curl -X POST "localhost:8080/api/v1/1"

- **POST** `/api/v1/transaction`: Creates and stores a new transaction.
    - **Example**:
        ```bash
        curl -X POST 
      {
       "description": "Smart Watch Generation II",
       "date": "2024-10-02",
       "amountInUSDollars": 299.55
       }
- **GET** `/api/v1/{transactionId}/{country}/{currency}`: Converts the transaction with the specified `transactionId` to the desired currency of the selected country.
    - **Example**:
        ```bash
        curl -X GET "localhost:8080/api/v1/1/Brazil/Real"

- **GET** `/api/v1/{transactionId}/{baseCurrency}`: Converts the transaction with the specified `transactionId` to the desired currency.
    - **Example**:
        ```bash
        curl -X GET "localhost:8080/api/v1/1/Real"

## How to Run the Application

There are multiple ways to run the application:

### With Maven

Clone or download the repository, then navigate to the root of the project and execute the command `./mvnw spring-boot:run` (use `mvnw.cmd` for Windows).

### With Docker

A Dockerfile is included to create a Docker image for the application. Build the image with the command `docker build -t transactionconverter .` and then run it using `docker run -it -p 8080:8080 transactionconverter`.

### Other Alternatives

As a Spring Boot project, the application can also be run directly from your preferred Integrated Development Environment (IDE).

## How to Build the Project

Being a Maven project, the simplest method to build it is to run `./mvnw clean package` in the root folder. The generated JAR file will be located inside the `target` folder.

## How to Run Automated Tests

To execute all automated tests, run `./mvnw verify` in the main folder of the application (or use `mvnw.cmd` for Windows environments).
