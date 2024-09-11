# SecuringAPI

This project is a Spring Boot application that provides a secured REST API using JWT (JSON Web Token) for authentication and role-based access control.

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven (for building the project)
- An IDE or text editor of your choice (e.g., IntelliJ IDEA, Eclipse, VSCode)
- Postman or any other API testing tool

## Getting Started

### 1. Clone the Repository

Clone the repository to your local machine:

```bash
https://github.com/PatrickxStar/SecuringApi.git
cd securingapi
2. Configure the Application
Make sure the application.properties file under src/main/resources contains the following configuration:

properties

spring.application.name=securingapi
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret-key=Xav7UDImTLjeCZwidWbTeMgiQf4rBbyzxqL4/Ht5Ax2/T+jehNp/JoWFpJyKvsDN6ZwmkoAsfqolhwEfPKtCSA==

logging.level.org.springframework=DEBUG
This configuration uses an in-memory H2 database for testing and sets the JWT secret key.

3. Build and Run the Application
Use Maven to build and run the application:

bash
Copy code
mvn clean install
mvn spring-boot:run
The application will start on http://localhost:8080.

4. Test the APIs
You can use Postman or any other API testing tool to test the APIs.

a. Register a User
URL: POST http://localhost:8080/api/register
Body (JSON):
json
Copy code
{
    "username": "rick",
    "password": "patrick",
    "role": "ADMIN"
}
Expected Response:
json
Copy code
{
    "message": "User registered successfully.",
    "token": "<your-jwt-token>"
}
b. Login with the User
URL: POST http://localhost:8080/api/login
Body (JSON):
json
Copy code
{
    "username": "rick",
    "password": "patrick"
}
Expected Response:
json
Copy code
{
    "message": "Login successful",
    "token": "<your-jwt-token>"
}
Copy the token from the response. You will use this token for subsequent requests.

c. Access the Secured API Endpoint
URL: GET http://localhost:8080/api/users
Authorization: Bearer Token (Use the token obtained from the login response)
Expected Response: A list of users, if the authenticated user has the ADMIN role.
Troubleshooting
If you encounter a 403 Forbidden or 401 Unauthorized error:

Ensure you are using the correct JWT token from the login response.
Make sure the user you registered has the appropriate role (ADMIN) to access the endpoint.
Check the server logs for any error messages and ensure the application is running correctly.
Additional Notes
The application uses JWT for stateless authentication.
Roles are required to access certain endpoints (e.g., only ADMIN can access /api/users).
Use the /api/register endpoint to create users and assign roles.
The in-memory H2 database is used for testing purposes; consider using a persistent database (e.g., MySQL, PostgreSQL) for production.