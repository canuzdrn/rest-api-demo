# Springboot project for my internship
#### RESTful API that includes customers, transactions and networks. Kotlin and Spring technologies are used. I used PostgreSQL database with JPA functionality as my database. Also included JUnit5 unit tests using Mockk library. You can clone this project in IntelliJ IDEA and the application will be running at port 8080 as default. (which can be altered by editing application.properties file)
##### server.port= REQUESTED_PORT* 
##### *: 2-byte signed integer
---
#### It is simple RESTful API that has elements which are conceptually similar to blockchain and blockchain networks such as users (customers), transactions, networks (ETH, SOL, etc.) In order to increase the readablity of methods, models and properties of the project on a web environment I also configured a SwaggerUI (Springfox) which can be visited via http://localhost:8080/swagger-ui/ while the application is running.
### The purpose of this project is to learn and practice concepts related to:
> - Kotlin
> - Springboot
> - Building a REST API
> - JPA Relations
### In more detail, I used the following:
> - REST API guidelines
> - HTTP (GET, POST, PUT, DELETE, responses, status codes)
> - Repository design pattern
> - Dependency injection
> - Data Transfer Objects (DTOs) & ObjectMapper (mostly in tests)
> - Testing API Endpoints (SwaggerUI & Postman)
> - JUnit 5 Testing with Mockk library
> - PostgreSQL
> - Docker (Setting up a Docker PostgreSQL environment)
### Application Architecture:

![image](https://user-images.githubusercontent.com/99494301/185875698-b488c5af-0936-4099-b839-d652b4e6b522.png)

### Website Look:

![image](https://user-images.githubusercontent.com/99494301/185875786-280b7153-e7ae-4811-8ca5-276584ab4d76.png)

### Controller Example:

![image](https://user-images.githubusercontent.com/99494301/185875911-2b17c2a1-39e8-4be2-be12-c059be0f5776.png)

### Model Example:

![image](https://user-images.githubusercontent.com/99494301/185876023-9329e767-cfeb-40d3-81ca-cf213ff7eb1f.png)

## Sample endpoints using Postman:
### [POST] Creates a new customer and returns created customer, as well as the '201 Created' status code.
![image](https://user-images.githubusercontent.com/99494301/185877739-06ceab54-762e-412f-bf33-01db789f5f0e.png)

### [DELETE] Deletes the customer and returns the '204 No Content' status code. (Code makes sure that requested customer doesn't have any active transactions)
![image](https://user-images.githubusercontent.com/99494301/185878180-418e90bc-325a-4386-be5e-4d40226e4554.png)

*Other HTTPS request methods are also available for customers, transactions and networks. You can check their functionalities and status codes in http://localhost:8080/swagger-ui/ *
---
### In order to compose docker-compose file
#### `docker-compose -f docker-compose.yml up -d`


