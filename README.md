# ClearSolutions Test Task

## Overview
This project is developed as a test task for ClearSolutions Company. It is a Spring Boot-based application designed to manage user information for a hypothetical system. The application provides a REST API to perform CRUD (Create, Read, Update, Delete) operations on user data.

## Features
- **User Registration**: Allows new users to register by providing their personal details including email, name, birth date, address, and phone number. The system validates all input data to ensure compliance with the required formats and constraints.
  
- **User Update**: Existing users can update their information. Fields such as email, phone number, and birth date are validated against specific rules to maintain data integrity.
  
- **User Deletion**: Users can be removed from the system using their unique identifier.
  
- **Search Functionality**: The API provides functionality to search for users based on a range of birth dates, helping to filter users efficiently.
  
- **Data Validation**: Comprehensive data validation is implemented to ensure that all user inputs meet the organization's standards before being processed.

## Technologies Used
- **Spring Boot**: Framework for creating stand-alone, production-grade Spring-based applications easily.
- **Spring Data JPA**: To persist data in SQL databases using Java Persistence API and Hibernate.
- **Spring Validation**: To validate user data before processing it in the database.
- **PostgreSQL**: As the relational database to store user data.
- **Flyway**: For version control for the database.
- **Lombok**: To reduce boilerplate code in Java.
- **Swagger (SpringDoc)**: For API documentation and to provide an interactive REST API user interface.

## API Documentation
- **Swagger UI**: The project integrates Swagger for API documentation. It provides an interactive user interface to explore and test available API endpoints directly from the browser. You can access the Swagger UI at: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Getting Started
To run the application:
1. Ensure Java 17 and Maven are installed on your machine.
2. Clone the repository and navigate to the project directory.
3. Run the command: `mvn spring-boot:run`
4. Access the application through ([http://localhost:8080/](http://localhost:8080/swagger-ui/index.html))

## Testing
The application includes a suite of unit and integration tests to ensure the API behaves as expected. Tests are run using Maven with the command: `mvn test`

For further details on the API endpoints and their specifications, please refer to the Swagger documentation provided by the running application.

SonarQube
![image](https://github.com/n0rb33rt/ClearSolutionsTestTask/assets/107361913/32be3ad2-436a-4439-8ce8-2325c0b73946)
